package library_platform.Client.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;

import java.io.IOException;

public class LoginRegisterController {

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField registerUsername;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private PasswordField registerRepeatPassword;


    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLoginClick(ActionEvent actionEvent) {
        String login = loginUsername.getText();
        String password = loginPassword.getText();

        // todo wyslij dane do serwera i sprawdz czy dane poprawne
    }

    @FXML
    public void onRegisterClick(ActionEvent actionEvent) {
        String password = registerPassword.getText();
        String repPassword = registerRepeatPassword.getText();
        if(password.equals(repPassword)) {
            String login = registerUsername.getText();

            // todo wyslij dane do serwera i stworz klienta, nastepnie zaloguj

        } else {
            AlertBuilder alertBuilder = new AlertBuilder(Alert.AlertType.WARNING);
            alertBuilder
                    .setTitle("Error")
                    .setHeaderText("Passwords are not identical");
            alertBuilder.getAlert().showAndWait();
        }

    }
}
