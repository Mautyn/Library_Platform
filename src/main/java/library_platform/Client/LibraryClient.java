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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library_platform/hello-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Client");
        primaryStage.show();

        ConnectionHandler connectionHandlerInstance = ConnectionHandler.getInstance();
        connectionHandlerInstance.establishConnection();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
