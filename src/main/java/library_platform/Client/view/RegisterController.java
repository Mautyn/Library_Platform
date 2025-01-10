package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Shared.DatabaseConnection;

import java.sql.*;
import java.io.IOException;
import java.time.LocalDate;

public class RegisterController {

    @FXML
    private CheckBox TermsCheckBox;

    @FXML
    private JFXButton Register_Button;

    @FXML
    private TextField RegisterLoginTextField;

    @FXML
    private TextField ConfirmLoginTextField;

    @FXML
    private TextField RegisterPasswordTextField;

    @FXML
    private TextField ConfirmPasswordTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    public void onRegisterClick(ActionEvent event) throws IOException {
        String login = RegisterLoginTextField.getText();
        String confirmLogin = ConfirmLoginTextField.getText();
        String password = RegisterPasswordTextField.getText();
        String confirmPassword = ConfirmPasswordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        if (!login.equals(confirmLogin)) {
            showAlert("Login Error", "The login fields do not match.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Error", "The password fields do not match.");
            return;
        }

        if (!TermsCheckBox.isSelected()) {
            showAlert("Terms Error", "You must accept the terms and conditions.");
            return;
        }

        String query = "SELECT * FROM uzytkownik WHERE E_mail = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                showAlert("Registration Error", "A user with this email already exists.");
            } else {
                String insertQuery = "INSERT INTO uzytkownik (Imie, Nazwisko, E_mail, Haslo, Typ_uzytkownika, Data_rejestracji, Status_konta) VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, firstName);
                    insertStatement.setString(2, lastName);
                    insertStatement.setString(3, login);
                    insertStatement.setString(4, password);
                    insertStatement.setString(5, "petent");
                    insertStatement.setDate(6, Date.valueOf(LocalDate.now()));
                    insertStatement.setString(7, "aktywne");

                    int rowsAffected = insertStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        showAlert("Registration Successful", "You have successfully registered.");
                        SceneController.setScene(event, "/library_platform/loginScene.fxml");
                    } else {
                        showAlert("Error", "An error occurred during registration.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while interacting with the database.");
        }
    }

    private void showAlert(String title, String message) {
        AlertBuilder alertBuilder = new AlertBuilder(AlertType.ERROR);
        alertBuilder.setTitle(title)
                .setHeaderText(message);
        alertBuilder.getAlert().showAndWait();
    }
}
