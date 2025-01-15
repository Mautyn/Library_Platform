package library_platform.Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class LibraryClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library_platform/mainpageScene.fxml"));
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
