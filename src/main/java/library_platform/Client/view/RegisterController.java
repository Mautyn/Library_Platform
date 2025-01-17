package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import library_platform.Client.ConnectionHandler;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Shared.LoginCredentials;
import library_platform.Shared.Request;

import java.io.IOException;

public class RegisterController {

    @FXML
    private CheckBox TermsCheckBox;

    @FXML
    private JFXButton Register_Button;

    @FXML
    private TextField RegisterLoginTextField;


    @FXML
    private PasswordField RegisterPasswordTextField;

    @FXML
    private PasswordField ConfirmPasswordTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    public void onRegisterClick(ActionEvent event) throws IOException {
        String login = RegisterLoginTextField.getText();
        String password = RegisterPasswordTextField.getText();
        String confirmPassword = ConfirmPasswordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();


        if (!password.equals(confirmPassword)) {
            AlertBuilder.showAlert("Password Error", "Password fields do not match.",
                    javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        if (!TermsCheckBox.isSelected()) {
            AlertBuilder.showAlert("Terms Error", "You must accept the terms and conditions.",
                    javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        ConnectionHandler serverConnection = ConnectionHandler.getInstance();
        Request request = new Request("REGISTER");
        LoginCredentials credentials = new LoginCredentials(login, password, firstName, lastName);
        serverConnection.sendObjectToServer(request);
        serverConnection.sendObjectToServer(credentials);
        Request ans = (Request) serverConnection.readObjectFromServer();

        if(ans.getContent().equals("SUCCESS")) {
            AlertBuilder.showAlert("Registration Successful", "You have successfully registered.",
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            LoginController.isLoggedIn = true;
            LoginController.loggedInUserEmail = login;
            SceneController.setScene(event, "/library_platform/userScene.fxml");
        } else if(ans.getContent().equals("LOGIN_UNAVAILABLE")) {
            AlertBuilder.showAlert("Registration Error", "A user with this email already exists.",
                    javafx.scene.control.Alert.AlertType.ERROR);
        } else {
            AlertBuilder.showAlert("Error", "An error occurred during registration.",
                    javafx.scene.control.Alert.AlertType.ERROR);
        }


    }
    public void onFacilitiesClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/facilitiesScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void onRecentClick(ActionEvent actionEvent) {
    }
    public void onCategoriesClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/categoriesScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onMainPageClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/mainpageScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSearchClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/searchScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
