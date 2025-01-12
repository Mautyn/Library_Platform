package library_platform.Server;

import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Client.view.LoginController;
import library_platform.Client.view.MainPageController;
import library_platform.Shared.Book;
import library_platform.Shared.DatabaseConnection;
import library_platform.Shared.LoginCredentials;
import library_platform.Shared.Request;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class LibraryServer {
    private static List<Book> books = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(54321)) {
            System.out.println("Server started on port 54321...");

            books.add(new Book("The Great Gatsby"));
            books.add(new Book("1984"));
            books.add(new Book("To Kill a Mockingbird"));

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

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.loggedIn = false;
        }

        private ObjectInputStream in;
        private ObjectOutputStream out;

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                LoginCredentials credentials;
                Request request;
                while ((request = (Request) in.readObject()) != null) {
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
                            out.writeObject(books);
                            break;
                        case "BORROW_BOOK":
                            String title = (String) in.readObject();
                            boolean success = borrowBook(title);
                            out.writeObject(success);
                            break;
                        default:
                            out.writeObject(new Request("UNKNOWN_REQUEST"));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private boolean borrowBook(String title) {
            synchronized (books) {
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title) && !book.isBorrowed()) {
                        book.borrow();
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean checkLogin(LoginCredentials credentials) {
            String query = "SELECT * FROM uzytkownik WHERE E_mail = ? AND Haslo = ?";
            Request ans;
            boolean loggedIn;
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, credentials.getLogin());
                preparedStatement.setString(2, credentials.getHaslo());
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    ans = new Request("SUCCESS");
                    out.writeObject(ans);
                    loggedIn = true;
                } else {
                    ans = new Request("INVALID");
                    out.writeObject(ans);
                    loggedIn = false;
                }
            } catch (Exception e) {
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
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, credentials.getLogin());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    ans = new Request("LOGIN_UNAVAILABLE");
                    loggedIn = false;
                } else {
                    String insertQuery = "INSERT INTO uzytkownik (Imie, Nazwisko, E_mail, Haslo, Typ_uzytkownika, Data_rejestracji, Status_konta) VALUES (?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, credentials.getFirstName());
                    insertStatement.setString(2, credentials.getLastName());
                    insertStatement.setString(3, credentials.getLogin());
                    insertStatement.setString(4, credentials.getHaslo());
                    insertStatement.setString(5, "petent");
                    insertStatement.setDate(6, Date.valueOf(LocalDate.now()));
                    insertStatement.setString(7, "aktywne");

                    int rowsAffected = insertStatement.executeUpdate();

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
    }
}

