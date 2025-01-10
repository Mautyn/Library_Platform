package library_platform.Client.view;


import com.jfoenix.controls.JFXButton;
import library_platform.Client.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import library_platform.Client.alert.AlertBuilder;

import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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



    public void onLog_inClick(ActionEvent event) {
        System.out.println("onLog_inClick");
        String login = LoginTextField.getText();
        String password = PasswordTextField.getText();

        System.out.println("Login: " + login);
        System.out.println("Password: " + password);

        String url = "jdbc:mysql://localhost:3306/biblioteka";
        String dbUser = "piotr";
        String dbPassword = "twoje_haslo";

        String query = "SELECT * FROM uzytkownik WHERE E_mail = ? AND Haslo = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                SceneController.setScene(event, "/library_platform/hello-view.fxml");
            } else {
                AlertBuilder alertBuilder = new AlertBuilder(AlertType.ERROR);
                alertBuilder
                        .setTitle("Login Failed")
                        .setHeaderText("Invalid login or password.");
                alertBuilder.getAlert().showAndWait();
            }
        } catch (Exception e) {
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
