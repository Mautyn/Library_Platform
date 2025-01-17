package library_platform.Client.view;

import javafx.scene.control.PasswordField;
import library_platform.Client.ConnectionHandler;
import com.jfoenix.controls.JFXButton;
import library_platform.Client.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library_platform.Client.alert.AlertBuilder;
import javafx.scene.control.TextField;
import library_platform.Shared.LoginCredentials;
import library_platform.Shared.Request;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXButton Log_InButton;

    @FXML
    private JFXButton ToRegisterButton;

    @FXML
    private TextField LoginTextField;

    @FXML
    private PasswordField PasswordTextField;


    public static boolean isLoggedIn = false;
    public static String loggedInUserEmail = "";
    public static String user_id = "";

    public void onLog_inClick(ActionEvent event) throws IOException {
        String login = LoginTextField.getText();
        String password = PasswordTextField.getText();

        Request request = new Request("LOG_IN");
        LoginCredentials loginCredentials = new LoginCredentials(login, password);
        ConnectionHandler serverConnection = ConnectionHandler.getInstance();

        serverConnection.sendObjectToServer(request);
        serverConnection.sendObjectToServer(loginCredentials);
        Request answer = (Request) serverConnection.readObjectFromServer();

        if(answer.getContent().equals("SUCCESS")) {
            isLoggedIn = true;
            loggedInUserEmail = login;
            SceneController.setScene(event, "/library_platform/mainpageScene.fxml");
        } else if (answer.getContent().equals("INVALID")) {
            isLoggedIn = false;
            loggedInUserEmail = null;
            AlertBuilder.showAlert("Login failed", "Invalid login od password", javafx.scene.control.Alert.AlertType.ERROR);
        } else {
            isLoggedIn = false;
            loggedInUserEmail = null;
            AlertBuilder.showAlert("Error", "An error occurred while logging in.", javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    public void onToRegisterClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/registerScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
