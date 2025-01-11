package library_platform.Client.view;

import javafx.scene.control.PasswordField;
import library_platform.Shared.DatabaseConnection;
import com.jfoenix.controls.JFXButton;
import library_platform.Client.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import library_platform.Client.alert.AlertBuilder;
import javafx.scene.control.TextField;

import java.sql.*;
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

    public void onLog_inClick(ActionEvent event) throws IOException {
        String login = LoginTextField.getText();
        String password = PasswordTextField.getText();

        String query = "SELECT * FROM uzytkownik WHERE E_mail = ? AND Haslo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isLoggedIn = true;
                loggedInUserEmail = login;

                SceneController.setScene(event, "/library_platform/hello-view.fxml");
            } else {
                AlertBuilder.showAlert("Login failed", "Invalid login od password", javafx.scene.control.Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            SceneController.setScene(actionEvent, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
