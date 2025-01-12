package library_platform.Server;

import library_platform.Client.view.MainPageController;
import library_platform.Shared.Book;

import java.io.*;
import java.net.*;
import java.util.*;


public class LibraryServer {
    private static List<Book> books = Collections.synchronizedList(new ArrayList<>());

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

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String request;
                while ((request = (String) in.readObject()) != null) {
                    switch (request) {
                        case "GET_BOOKS":
                            out.writeObject(books);
                            break;
                        case "BORROW_BOOK":
                            String title = (String) in.readObject();
                            boolean success = borrowBook(title);
                            out.writeObject(success);
                            break;
                        default:
                            out.writeObject("UNKNOWN_REQUEST");
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
    }
}

