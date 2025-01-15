package library_platform.Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library_platform.Client.ConnectionHandler;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Client.view.LoginController;
import library_platform.Client.view.MainPageController;
import library_platform.Shared.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class LibraryServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(54321)) {
            System.out.println("Server started on port 54321...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private boolean loggedIn;
        private boolean loggedAsAdmin;
        ArrayList<Book> queryBooks;
        ArrayList<Book> bookCopies;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.loggedIn = false;
            this.loggedAsAdmin = false;
        }

        private ObjectInputStream in;
        private ObjectOutputStream out;

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                LoginCredentials credentials = null;
                Request request;
                while ((request = (Request) in.readObject()) != null) {
                    System.out.println(request.getContent());
                    switch (request.getContent()) {
                        case "LOG_IN":
                            credentials = (LoginCredentials) in.readObject();
                            loggedIn = checkLogin(credentials);
                            break;
                        case "REGISTER":
                            credentials = (LoginCredentials) in.readObject();
                            loggedIn = registerUser(credentials);
                            break;
                        case "GET_BOOKS":
                            String searchMode = ((Request) in.readObject()).getContent();
                            String searchQuery = ((Request) in.readObject()).getContent();
                            queryBooks = getBooks(searchMode, searchQuery);
                            out.writeObject(queryBooks);
                            break;
                        case "LOG_OUT":
                            if(loggedIn) {
                                logOut();
                                credentials = null;
                                out.writeObject(new Request("SUCCESS"));
                            }
                            break;
                        case "GET_BORROWED":
                            if(loggedIn) {
                                searchMode = ((Request) in.readObject()).getContent();
                                searchQuery = ((Request) in.readObject()).getContent();
                                queryBooks = getBooks(searchMode, searchQuery);
                                out.writeObject(queryBooks);
                            }
                            break;
                        case "GET_RESERVED":
                            if(loggedIn) {
                                searchMode = ((Request) in.readObject()).getContent();
                                searchQuery = ((Request) in.readObject()).getContent();
                                queryBooks = getBooks(searchMode, searchQuery);
                                out.writeObject(queryBooks);
                            }
                            break;
                        // ONLY USER
                        case "RESERVE_BOOK":
                            if(loggedIn) {
//                                String title = (String) in.readObject();
//                                boolean success = borrowBook(title);
//                                out.writeObject(success);
                            }
                            break;
                        // ONLY USER
                        case "UNDO_RESERVE_BOOK":
                            if(loggedIn) {
//                                String title = (String) in.readObject();
//                                boolean success = borrowBook(title);
//                                out.writeObject(success);
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_BOOK_BORROWED":
                            if(loggedIn && loggedAsAdmin) {
//                                String title = (String) in.readObject();
//                                boolean success = borrowBook(title);
//                                out.writeObject(success);
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_BOOK_RETURNED":
                            if(loggedIn && loggedAsAdmin) {

                            }
                            break;
                        case "DELETE_USER":
                            if(loggedIn) {
                                String userEmail = credentials.getLogin();
                                loggedIn = !deleteUser(userEmail, true);
                            } else if(loggedIn && loggedAsAdmin) {
                                String userEmail = ((Request) in.readObject()).getContent();
                                if(userEmail.equals(credentials.getLogin())) {
                                    loggedIn = !deleteUser(userEmail, true);
                                } else {
                                    loggedIn = !deleteUser(userEmail, false);
                                }
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_ADMIN_PRIVILEGE":
                            if(loggedIn && loggedAsAdmin) {

                            }
                            break;
                        // ONLY ADMIN
                        case "GET_USERS":
                            if(loggedIn && loggedAsAdmin) {
                                String userSearchQuery = ((Request) in.readObject()).getContent();
                                ArrayList<User> userList = getUsers(userSearchQuery);
                                out.writeObject(userList);
                            }
                            break;
                        default:
                            System.out.println("UNKNOWN REQUEST: " + request.getContent());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

//        private boolean borrowBook(String title) {
//            synchronized (books) {
//                for (Book book : books) {
//                    if (book.getTitle().equalsIgnoreCase(title) && !book.isBorrowed()) {
//                        book.borrow();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }

//        private void setBookCopies() {
//            String query = "SELECT * FROM egzemplarz";
//            try {
//                Connection connection = DatabaseConnection.getConnection();
//                PreparedStatement ps = connection.prepareStatement(query);
//                ResultSet resultSet = ps.executeQuery();
//                if(resultSet.next()) {
//                    Book b = new Book;
//                    bookCopies.add();
//                }
//            }
//        }
        private void logOut() {
            loggedAsAdmin = false;
            loggedIn = false;
        }

        private boolean deleteUser(String userEmail, boolean me) {
            Request ans;
            boolean success;
            String query = "DELETE FROM uzytkownik WHERE E_mail = '" + userEmail + "';";
            try (Connection connection = DatabaseConnection.getConnection()) {
                int rowsAffected;
                synchronized (this) {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    rowsAffected = preparedStatement.executeUpdate();
                }
                if (rowsAffected > 0) {
                    ans = new Request("SUCCESS");
                    success = true;
                } else {
                    ans = new Request("ERROR");
                    success = false;
                }
            } catch (Exception e) {
                ans = new Request("ERROR");
                success = false;
            }
            try {
                out.writeObject(ans);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }

        private boolean checkLogin(LoginCredentials credentials) {
            String query = "SELECT * FROM uzytkownik WHERE E_mail = ? AND Haslo = ? AND Status_konta LIKE 'aktywne'";
            Request ans;
            boolean loggedIn;
            try (Connection connection = DatabaseConnection.getConnection()) {
                ResultSet resultSet;
                synchronized (this) {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, credentials.getLogin());
                    preparedStatement.setString(2, credentials.getHaslo());
                    resultSet = preparedStatement.executeQuery();
                }
                if (resultSet.next()) {
                    if(resultSet.getString("Typ_uzytkownika").equals("pracownik")) {
                        loggedAsAdmin = true;
                    } else {
                        loggedAsAdmin = false;
                    }
                    System.out.println("LOGIN SUCCESS");
                    ans = new Request("SUCCESS");
                    loggedIn = true;
                } else {
                    System.out.println("LOGIN INVALID");
                    ans = new Request("INVALID");
                    loggedIn = false;
                }
            } catch (Exception e) {
                System.out.println("LOGIN ERROR");
                ans = new Request("ERROR");
                loggedIn = false;
            }

            try {
                out.writeObject(ans);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loggedIn;
        }

        private boolean registerUser(LoginCredentials credentials) {
            String query = "SELECT * FROM uzytkownik WHERE E_mail = ?";
            Request ans;
            boolean loggedIn;
            try (Connection connection = DatabaseConnection.getConnection()) {
                ResultSet resultSet;
                synchronized (this) {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, credentials.getLogin());
                    resultSet = preparedStatement.executeQuery();
                }

                if (resultSet.next()) {
                    ans = new Request("LOGIN_UNAVAILABLE");
                    loggedIn = false;
                    credentials = null;
                } else {
                    String insertQuery = "INSERT INTO uzytkownik (Imie, Nazwisko, E_mail, Haslo, Typ_uzytkownika, Data_rejestracji, Status_konta) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    int rowsAffected = 0;
                    synchronized (this) {
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setString(1, credentials.getFirstName());
                        insertStatement.setString(2, credentials.getLastName());
                        insertStatement.setString(3, credentials.getLogin());
                        insertStatement.setString(4, credentials.getHaslo());
                        insertStatement.setString(5, "petent");
                        insertStatement.setDate(6, Date.valueOf(LocalDate.now()));
                        insertStatement.setString(7, "aktywne");
                        rowsAffected = insertStatement.executeUpdate();
                    }

                    if (rowsAffected > 0) {
                        ans = new Request("SUCCESS");
                        loggedIn = true;
                    } else {
                        ans = new Request("ERROR");
                        loggedIn = false;
                    }
                }
            } catch (Exception e) {
                ans = new Request("ERROR");
                loggedIn = false;
            }

            try {
                out.writeObject(ans);
                return loggedIn;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loggedIn;
        }

        private ArrayList<Book> getBooks(String searchMode, String searchQuery) {
            try {
                String query = "";
                Connection connection = DatabaseConnection.getConnection();
                if(searchMode.equals("CATEGORIES")) {
                    query = "SELECT * FROM ksiazka WHERE Gatunek LIKE '" + searchQuery + "'";
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        rs = preparedStatement.executeQuery();
                    }
                    ArrayList<Book> bookList = new ArrayList<>();
                    while(rs.next()) {
                        Book book = new Book(rs.getString("Tytul"));
                        book.setId(rs.getInt("ID_ksiazki"));
                        book.setCategory(rs.getString("Gatunek"));
                        book.setAuthor(rs.getString("Autor"));
                        book.setPublisher(rs.getString("Wydawnictwo"));
                        book.setYear(rs.getString("Rok_wydania"));
                        bookList.add(book);
                    }
                    return bookList;
                } else if (searchMode.equals("FULL")) {
                    if(searchQuery.isEmpty()) {

                        query = "SELECT * FROM ksiazka";
                        ResultSet rs;
                        synchronized (this) {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            rs = preparedStatement.executeQuery();
                        }
                        ArrayList<Book> bookList = new ArrayList<>();

                        while(rs.next()) {
                            Book book = new Book(rs.getString("Tytul"));
                            book.setId(rs.getInt("ID_ksiazki"));
                            book.setCategory(rs.getString("Gatunek"));
                            book.setAuthor(rs.getString("Autor"));
                            book.setPublisher(rs.getString("Wydawnictwo"));
                            book.setYear(rs.getString("Rok_wydania"));
                            bookList.add(book);
                        }
                        return bookList;
                    }
                    else if(searchMode.equals("RETURNED") || searchMode.equals("BORROWED")) {
                        String date;
                        String status;
                        if(searchMode.equals("RETURNED")){
                            date = "Data_zwrotu";
                            status = "zakończone";
                        }
                        else{
                            date = "Data_wypozyczenia";
                            status = "aktywne";
                        }
                        query = "SELECT ksiazka.*, wypozyczenie.data_wypozyczenia, wypozyczenie.data_zwrotu FROM ksiazka " +
                                "JOIN egzemplarz on ksiazka.ID_ksiazki = egzemplarz.ID_ksiazki " +
                                "JOIN wypozyczenie on egzemplarz.id_egzemplarza = wypozyczenie.id_egzemplarza " +
                                "LEFT JOIN uzytkownik ON uzytkownik.ID_uzytkownika = wypozyczenie.ID_uzytkownika " +
                                "WHERE uzytkownik.E_mail LIKE '" + searchQuery + "' AND wypozyczenie.status like '" + status + "'"  ;
                        ResultSet rs;
                        synchronized (this) {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            rs = preparedStatement.executeQuery();
                        }
                        ArrayList<Book> bookList = new ArrayList<>();
                        while(rs.next()) {
                            Book book = new Book(rs.getString("Tytul"));
                            book.setId(rs.getInt("ID_ksiazki"));
                            book.setCategory(rs.getString("Gatunek"));
                            book.setAuthor(rs.getString("Autor"));
                            book.setPublisher(rs.getString("Wydawnictwo"));
                            book.setYear(rs.getString("Rok_wydania"));
                            book.setDate(rs.getString(date));
                            bookList.add(book);
                        }
                        return bookList;
                    } else {
                        query = "SELECT * FROM ksiazka WHERE Tytul LIKE '%" +searchQuery + "%' OR Autor LIKE '%" +searchQuery + "%' OR Gatunek LIKE '%" +searchQuery + "%'";
                        ResultSet rs;
                        synchronized (this) {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            rs = preparedStatement.executeQuery();

                        }
                        ArrayList<Book> bookList = new ArrayList<>();

                        while(rs.next()) {
                            Book book = new Book(rs.getString("Tytul"));
                            book.setId(rs.getInt("ID_ksiazki"));
                            book.setCategory(rs.getString("Gatunek"));
                            book.setAuthor(rs.getString("Autor"));
                            book.setPublisher(rs.getString("Wydawnictwo"));
                            book.setYear(rs.getString("Rok_wydania"));
                            bookList.add(book);
                        }
                        return bookList;
                    }
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            ArrayList<Book> bookList = new ArrayList<>();
            return bookList;
        }

        private ArrayList<User> getUsers(String searchQuery) {
            try {
                String query = "";
                Connection connection = DatabaseConnection.getConnection();
                if(searchQuery.isEmpty()) {

                    query = "SELECT * FROM uzytkownik";
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        rs = preparedStatement.executeQuery();
                    }
                    ArrayList<User> userList = new ArrayList<>();

                    while(rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("ID_uzytkownika"));
                        user.setFirstName(rs.getString("Imie"));
                        user.setLastName(rs.getString("Nazwisko"));
                        user.setEmail(rs.getString("E_mail"));
                        if(rs.getString("Typ_uzytkownika").equals("pracownik")) {
                            user.setWorker();
                        } else {
                            user.setCustomer();
                        }
                        if(rs.getString("Status_konta").equals("aktywne")) {
                            user.setActive();
                        } else {
                            user.setInactive();
                        }
                        userList.add(user);
                    }
                    return userList;

                } else {
                    query = "SELECT * FROM uzytkownik WHERE Imie LIKE '%" +searchQuery + "%' OR Nazwisko LIKE '%" +searchQuery + "%' OR E_mail LIKE '%" +searchQuery + "%'";
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        rs = preparedStatement.executeQuery();

                    }
                    ArrayList<User> userList = new ArrayList<>();

                    while(rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("ID_uzytkownika"));
                        user.setFirstName(rs.getString("Imie"));
                        user.setLastName(rs.getString("Nazwisko"));
                        user.setEmail(rs.getString("E_mail"));
                        if(rs.getString("Typ_uzytkownika").equals("pracownik")) {
                            user.setWorker();
                        } else {
                            user.setCustomer();
                        }
                        if(rs.getString("Status_konta").equals("aktywne")) {
                            user.setActive();
                        } else {
                            user.setInactive();
                        }
                        userList.add(user);
                    }
                    return userList;
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}

