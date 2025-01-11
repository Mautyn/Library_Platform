package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;

import java.io.IOException;

public class UserController {
    @FXML
    private JFXButton logOutButton;

    @FXML
    private JFXButton returnedBooksButton;

    @FXML
    private JFXButton borrowedBooksButton;


    public void onLogOutClick(ActionEvent event) {
        try {
            LoginController.isLoggedIn = false;
            AlertBuilder alert = new AlertBuilder(Alert.AlertType.INFORMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText("You are logged out");
            alert.getAlert().showAndWait();
            SceneController.setScene(event, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
