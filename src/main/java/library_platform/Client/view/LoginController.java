package library_platform.Client.view;

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
    private TextField PasswordTextField;

    // Statyczna zmienna, która będzie przechowywać stan logowania użytkownika
    public static boolean isLoggedIn = false; // Flaga, która będzie przechowywać informację o stanie logowania
    public static String loggedInUserEmail = ""; // E-mail zalogowanego użytkownika, można przechować inne dane użytkownika

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
                AlertBuilder alertBuilder = new AlertBuilder(AlertType.ERROR);
                alertBuilder
                        .setTitle("Login Failed")
                        .setHeaderText("Invalid login or password.");
                alertBuilder.getAlert().showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertBuilder alertBuilder = new AlertBuilder(AlertType.ERROR);
            alertBuilder
                    .setTitle("Error")
                    .setHeaderText("An error occurred while logging in.")
                    .setException(e);
            alertBuilder.getAlert().showAndWait();
        }
    }

    public void onToRegisterClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/registerScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
