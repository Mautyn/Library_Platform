package library_platform.Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import library_platform.Shared.Book;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class LibraryClient extends Application {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ListView<String> booksListView = new ListView<>();
    private Button borrowButton = new Button("Borrow");

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Wczytaj FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library_platform/hello-view.fxml"));
        Parent root = loader.load();

        // Stwórz scenę
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Client");
        primaryStage.show();
    }


    private void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 12345);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void fetchBooks() {
        try {
            out.writeObject("GET_BOOKS");
            out.flush();

            List<Book> books = (List<Book>) in.readObject();
            booksListView.getItems().clear();
            for (Book book : books) {
                booksListView.getItems().add(book.getTitle() + (book.isBorrowed() ? " (Borrowed)" : ""));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void borrowSelectedBook() {
        String selected = booksListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String title = selected.split(" \\(")[0];
                out.writeObject("BORROW_BOOK");
                out.writeObject(title);
                out.flush();

                boolean success = (Boolean) in.readObject();
                if (success) {
                    fetchBooks();
                    showAlert("Success", "You borrowed the book: " + title);
                } else {
                    showAlert("Error", "Failed to borrow the book.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
