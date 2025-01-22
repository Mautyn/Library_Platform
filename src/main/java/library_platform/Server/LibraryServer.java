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
import java.util.stream.Collectors;


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
        User me;

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
                            searchMode = ((Request) in.readObject()).getContent();
                            searchQuery = ((Request) in.readObject()).getContent();
                            if(loggedIn) {
                                queryBooks = getBooks(searchMode, searchQuery);
                                out.writeObject(queryBooks);
                            }
                            break;
                        case "GET_RESERVED":
                            searchMode = ((Request) in.readObject()).getContent();
                            searchQuery = ((Request) in.readObject()).getContent();
                            if(loggedIn) {
                                queryBooks = getBooks(searchMode, searchQuery);
                                out.writeObject(queryBooks);
                            }
                            break;
                        // ONLY USER
                        case "RESERVE_BOOK":
                            try {
                                ArrayList<Book> booksToReserve = (ArrayList<Book>) in.readObject();
                                for (int i=0; i<booksToReserve.size(); i++) {
                                    boolean success = ReserveBook(booksToReserve.get(i));
                                    System.out.println("Reservation status for book " + booksToReserve.get(i).getTitle() + ": " + success);
                                }
                                out.writeObject(new Request("SUCCESS"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                out.writeObject(new Request("ERROR"));
                            }
                            break;
                        // ONLY USER
                        case "UNDO_RESERVE_BOOK":
                            if(loggedIn) {
                                //
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_BOOK_BORROWED":
                            if(loggedIn) {
                                //
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_BOOK_RETURNED":
                            if(loggedIn) {
                                //
                            }
                            break;
                        case "DELETE_USER":
                            String userEmail = ((Request) in.readObject()).getContent();
                            if(loggedIn && !loggedAsAdmin) {
                                loggedIn = !deleteUser(userEmail, true);
                            } else if(loggedIn && loggedAsAdmin) {
                                if(userEmail.equals(credentials.getLogin())) {
                                    loggedIn = !deleteUser(userEmail, true);
                                } else {
                                    loggedIn = !deleteUser(userEmail, false);
                                }
                            }
                            if(!loggedIn) {
                                credentials = null;
                            }
                            break;
                        // ONLY ADMIN
                        case "SET_ADMIN_PRIVILEGE":
                            userEmail = ((Request) in.readObject()).getContent();
                            if(loggedIn) {
                                setAdminPrivilege(userEmail);
                            }
                            break;
                        // ONLY ADMIN
                        case "TAKE_ADMIN_PRIVILEGE":
                            userEmail = ((Request) in.readObject()).getContent();
                            if(loggedIn) {
                                takeAdminPrivilege(userEmail);
                            }
                            break;
                        // ONLY ADMIN
                        case "ADD_BOOK":
                            Book newBook = (Book) in.readObject();
                            if(loggedIn) {
                                addBook(newBook);
                            }
                            break;
                        // ONLY ADMIN
                        case "GET_USERS":
                            if(loggedIn) {
                                String userSearchQuery = ((Request) in.readObject()).getContent();
                                ArrayList<User> userList = getUsers(userSearchQuery);
                                out.writeObject(userList);
                            }
                            break;
                        // ALWAYS AVAILABLE (ALSO SIGNED OUT)
                        case "GET_NOTIFICATIONS":
                            if(loggedIn) {
                                ArrayList<Notification> notifications = getNotification("LOGGED_IN");
                                out.writeObject(notifications);
                            } else {
                                ArrayList<Notification> notifications = getNotification("LOGGED_OUT");
                                out.writeObject(notifications);
                            }
                            break;
                        case "CREATE_NOTIFICATION":
                            if(loggedIn) {
                                Notification newNoti = (Notification) in.readObject();
                                addNotification(newNoti);
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

        private boolean addBook(Book newBook) {
            Request ans;
            boolean success;
            String insertQuery = "INSERT INTO ksiazka (Tytul, Autor, ISBN, Rok_wydania, Getunek, Wydawnictwo, Liczba_egzemplarzy) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                if(!loggedAsAdmin) {
                    ans = new Request("ACCESS_DENIED");
                    out.writeObject(ans);
                    return false;
                }
                Connection connection = DatabaseConnection.getConnection();
                int rowsAffected = 0;
                synchronized (this) {
                    PreparedStatement ps = connection.prepareStatement(insertQuery);
                    ps.setString(1, newBook.getTitle());
                    ps.setString(2, newBook.getAuthor());
                    ps.setString(3, newBook.getISBN());
                    ps.setString(4, newBook.getYear());
                    ps.setString(5, newBook.getCategory());
                    ps.setString(6, newBook.getPublisher());
                    ps.setInt(7, newBook.getNumOfCopies());
                    rowsAffected = ps.executeUpdate();
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
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }

        private boolean ReserveBook(Book newBook) {
            Request ans1;
            Request ans2;
            boolean success;

            String selectMaxIdQuery = "SELECT COALESCE(MAX(ID_wypozyczenia), 0) + 1 AS NewID FROM wypozyczenie";

            String insertQuery = "INSERT INTO wypozyczenie (ID_wypozyczenia, ID_uzytkownika, ID_egzemplarza, " +
                    "Data_wypozyczenia, Data_zwrotu, Status, Kara_za_opoznienie) VALUES (?, " +
                    "(SELECT ID_uzytkownika FROM uzytkownik WHERE uzytkownik.E_mail = ?), " +
                    "(SELECT ID_egzemplarza FROM egzemplarz WHERE ID_ksiazki = ? AND Stan = 'w bibliotece' LIMIT 1), " +
                    "CURRENT_DATE, NULL, 'rezerwacja', 0)";

            String updateQuery = "UPDATE egzemplarz SET Stan = 'wypożyczona', Lokalizacja = NULL " +
                    "WHERE ID_egzemplarza = (SELECT ID_egzemplarza FROM egzemplarz WHERE ID_ksiazki = ? AND Stan = 'w bibliotece' LIMIT 1)";

            try (Connection connection = DatabaseConnection.getConnection()) {
                connection.setAutoCommit(false);

                int newId;
                try (PreparedStatement selectStatement = connection.prepareStatement(selectMaxIdQuery)) {
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        newId = resultSet.getInt("NewID");
                    } else {
                        throw new SQLException("Failed to retrieve new ID_wypozyczenia.");
                    }
                }

                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    System.out.println(LoginController.loggedInUserEmail);

                    insertStatement.setInt(1, newId);
                    insertStatement.setString(2, LoginController.loggedInUserEmail);
                    insertStatement.setInt(3, newBook.getId());

                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted == 0) {
                        ans1 = new Request("ERROR");
                        throw new SQLException("Failed to insert reservation.");
                    }
                    else{
                        ans1 = new Request("SUCCES");
                    }
                }

                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, newBook.getId());
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated == 0) {
                        ans2 = new Request("ERROR");
                        throw new SQLException("Failed to update book status.");
                    }
                    else {
                        ans2 = new Request("SUCCESS");
                    }
                }
                if(ans2.equals("SUCCES") && ans1.equals("SUCCES")){
                    connection.commit();
                    success = true;
                    System.out.println("RESERVE SUCCESS");
                }
                else{
                    success = false;
                    connection.rollback();
                    }

            } catch (Exception e) {
                e.printStackTrace();
                ans1 = new Request("ERROR");
                success = false;

                try (Connection connection = DatabaseConnection.getConnection()) {
                    connection.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

            try {
                out.writeObject(ans1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }

        private boolean deleteUser(String userEmail, boolean me) {
            Request ans;
            boolean success;
            String query = "DELETE FROM uzytkownik WHERE E_mail = '" + userEmail + "';";
            try (Connection connection = DatabaseConnection.getConnection()) {
                if(!loggedAsAdmin) {
                    ans = new Request("ACCESS_DENIED");
                    out.writeObject(ans);
                    return false;
                }
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

        /**
         * Sets admin privileges to user
         * @param userEmail
         * @return true if success false is error
         */
        private boolean setAdminPrivilege(String userEmail) {
            Request ans;
            boolean success;
            try {
                if(!loggedAsAdmin) {
                    ans = new Request("ACCESS_DENIED");
                    out.writeObject(ans);
                    return false;
                }
                Connection connection = DatabaseConnection.getConnection();
                String updateStatement = "UPDATE uzytkownik SET Typ_uzytkownika = 'pracownik' WHERE E-mail = '" + userEmail + "' AND Typ_uzytkownika = 'petent';";
                int updatedRows;
                synchronized (this) {
                    PreparedStatement update = connection.prepareStatement(updateStatement);
                    updatedRows = update.executeUpdate();
                }
                if(updatedRows > 0) {
                    ans = new Request("SUCCESS");
                    success = true;
                } else {
                    ans = new Request("ERROR");
                    success = true;
                }
            } catch (Exception e) {
                System.out.println("SET ADMIN PRIVILEGE ERROR");
                e.printStackTrace();
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

        /**
         * Takes admin privileges from user
         * @param userEmail
         * @return true if success, false if error
         */
        private boolean takeAdminPrivilege(String userEmail) {
            Request ans;
            boolean success;
            try {
                if(!loggedAsAdmin) {
                    ans = new Request("ACCESS_DENIED");
                    out.writeObject(ans);
                    return false;
                }
                Connection connection = DatabaseConnection.getConnection();
                String updateStatement = "UPDATE uzytkownik SET Typ_uzytkownika = 'petent' WHERE E-mail = '" + userEmail + "' AND Typ_uzytkownika = 'pracownik';";
                int updatedRows;
                synchronized (this) {
                    PreparedStatement update = connection.prepareStatement(updateStatement);
                    updatedRows = update.executeUpdate();
                }
                if(updatedRows > 0) {
                    ans = new Request("SUCCESS");
                    success = true;
                } else {
                    ans = new Request("ERROR");
                    success = true;
                }
            } catch (Exception e) {
                System.out.println("TAKE ADMIN PRIVILEGE ERROR");
                e.printStackTrace();
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
                    me = new User();
                    me.setUserId(resultSet.getInt("ID_uzytkownika"));
                    me.setFirstName("Imie");
                    me.setLastName("Nazwisko");
                    me.setEmail("E_mail");
                    if(resultSet.getString("Typ_uzytkownika").equals("pracownik")) {
                        loggedAsAdmin = true;
                        me.setWorker();
                    } else {
                        loggedAsAdmin = false;
                        me.setCustomer();
                    }
                    System.out.println("LOGIN SUCCESS");
                    ans = new Request("SUCCESS");
                    loggedIn = true;
                } else {
                    me = null;
                    System.out.println("LOGIN INVALID");
                    ans = new Request("INVALID");
                    loggedIn = false;
                }
            } catch (Exception e) {
                me = null;
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
            String query = "SELECT ID_uzytkownika FROM uzytkownik WHERE E_mail = ?";
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
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, credentials.getLogin());
                        resultSet = preparedStatement.executeQuery();
                    }
                    if (rowsAffected > 0) {
                        me = new User();
                        me.setUserId(resultSet.getInt("ID_uzytkownika"));
                        me.setFirstName(credentials.getFirstName());
                        me.setLastName(credentials.getLastName());
                        me.setEmail(credentials.getLogin());
                        ans = new Request("SUCCESS");
                        loggedIn = true;
                    } else {
                        me = null;
                        ans = new Request("ERROR");
                        loggedIn = false;
                    }
                }
            } catch (Exception e) {
                me = null;
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
                        preparedStatement.setString(1, "%" + searchQuery + "%");
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
                } else if(searchMode.equals("RETURNED") || searchMode.equals("BORROWED")) {
                    String date;
                    List<String> statuses = new ArrayList<>();
                    if (searchMode.equals("RETURNED")) {
                        date = "Data_zwrotu";
                        statuses.add("zakończone");
                    } else { // "BORROWED"
                        date = "Data_wypozyczenia";
                        statuses.add("aktywne");
                        statuses.add("rezerwacja");
                    }
                    query = "SELECT ksiazka.*, wypozyczenie.data_wypozyczenia, wypozyczenie.data_zwrotu " +
                            "FROM ksiazka " +
                            "JOIN egzemplarz ON ksiazka.ID_ksiazki = egzemplarz.ID_ksiazki " +
                            "JOIN wypozyczenie ON egzemplarz.id_egzemplarza = wypozyczenie.id_egzemplarza " +
                            "LEFT JOIN uzytkownik ON uzytkownik.ID_uzytkownika = wypozyczenie.ID_uzytkownika " +
                            "WHERE uzytkownik.E_mail LIKE ? AND wypozyczenie.status IN (" +
                            statuses.stream().map(s -> "?").collect(Collectors.joining(",")) + ")";
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        int index = 1;
                        preparedStatement.setString(index++, "%" + searchQuery + "%");
                        for (String status : statuses) {
                            preparedStatement.setString(index++, status);
                        }
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

        private ArrayList<Notification> getNotification(String searchMode) {
            try {
                String query = "";
                Connection connection = DatabaseConnection.getConnection();
                if(searchMode.equals("LOGGED_IN")) {
                    query = "SELECT * FROM powiadomienia WHERE Typ_powiadomienia LIKE 'nowa_ksiazka' OR Typ_powiadomienia LIKE 'ogolne' OR ID_uzytkownika = " + me.getUserId();
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        rs = preparedStatement.executeQuery();
                    }
                    ArrayList<Notification> notificationList = new ArrayList<>();
                    while(rs.next()) {
                        Notification noti = new Notification(
                                rs.getInt("ID_powiadomienia"),
                                rs.getInt("ID_uzytkownika"),
                                rs.getDate("Data_wyslania"),
                                rs.getString("Typ_powiadomienia"),
                                rs.getString("Tresc"));
                        notificationList.add(noti);
                    }
                    return notificationList;
                } else if (searchMode.equals("LOGGED_OUT")) {
                    query = "SELECT * FROM powiadomienia WHERE Typ_powiadomienia LIKE 'nowa_ksiazka' OR Typ_powiadomienia LIKE 'ogolne'";
                    ResultSet rs;
                    synchronized (this) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        rs = preparedStatement.executeQuery();
                    }
                    ArrayList<Notification> notificationList = new ArrayList<>();
                    while(rs.next()) {
                        Notification noti = new Notification(
                                rs.getInt("ID_powiadomienia"),
                                rs.getInt("ID_uzytkownika"),
                                rs.getDate("Data_wyslania"),
                                rs.getString("Typ_powiadomienia"),
                                rs.getString("Tresc"));
                        notificationList.add(noti);
                    }
                    return notificationList;
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            ArrayList<Notification> notiList = new ArrayList<>();
            return notiList;
        }

        private boolean addNotification(Notification newNoti) {
            Request ans;
            boolean success;
            String insertQuery = "INSERT INTO powiadomienia (ID_powiadomienia, ID_uzytkownika, Typ_powiadomienia, Data_wyslania, Tresc, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                if(!loggedAsAdmin) {
                    ans = new Request("ACCESS_DENIED");
                    out.writeObject(ans);
                    return false;
                }
                Connection connection = DatabaseConnection.getConnection();
                int rowsAffected = 0;
                synchronized (this) {
                    PreparedStatement ps = connection.prepareStatement(insertQuery);
                    ps.setInt(1, newNoti.getNotifiactionID());
                    ps.setInt(2, newNoti.getUserID());
                    ps.setString(3, newNoti.getNotificationType());
                    ps.setDate(4, newNoti.getDate());
                    ps.setString(5, newNoti.getContent());
                    ps.setString(6, "nieodczytana");
                    rowsAffected = ps.executeUpdate();
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
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }
    }
}

