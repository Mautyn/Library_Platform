package library_platform.Client.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import library_platform.Client.SceneController;
import org.controlsfx.control.action.Action;

import java.io.IOException;

public class HelloViewController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onConnectButtonClick() {
        welcomeText.setText("Connecting to the server...");
        // Tutaj dodaj logikę łączenia z serwerem
    }

    @FXML
    public void onUserClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "login-register.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onFacilitiesClick(ActionEvent actionEvent) {

    }

    @FXML
    public void onSearchClick(ActionEvent actionEvent) {

    }

    @FXML
    public void onRecentActClick(ActionEvent actionEvent) {

    }

    @FXML
    public void onCategoriesClick(ActionEvent actionEvent) {

    }
}