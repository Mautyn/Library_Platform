package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
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

//    @FXML
//    private TextField ConfirmLoginTextField;

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
//        String confirmLogin = ConfirmLoginTextField.getText();
        String password = RegisterPasswordTextField.getText();
        String confirmPassword = ConfirmPasswordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        // wyjebalbym to, bez sensu potwiedzać login, przecież jest widoczny
//        if (!login.equals(confirmLogin)) {
//            AlertBuilder.showAlert("Login Error", "The login fields do not match.");
//            return;
//        }

        if (!password.equals(confirmPassword)) {
            AlertBuilder.showAlert("Password Error", "The password fields do not match.", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        if (!TermsCheckBox.isSelected()) {
            AlertBuilder.showAlert("Terms Error", "You must accept the terms and conditions.", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        String query = "SELECT * FROM uzytkownik WHERE E_mail = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                AlertBuilder.showAlert("Registration Error", "A user with this email already exists.", javafx.scene.control.Alert.AlertType.ERROR);
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
                        AlertBuilder alertBuilder = new AlertBuilder(AlertType.INFORMATION);
                        alertBuilder.setTitle("Registration Successful")
                                .setHeaderText("You have successfully registered.");
                        alertBuilder.getAlert().showAndWait();
                        SceneController.setScene(event, "/library_platform/loginScene.fxml");
                    } else {
                        AlertBuilder.showAlert("Error", "An error occurred during registration.", javafx.scene.control.Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertBuilder.showAlert("Database Error", "An error occurred while interacting with the database.", javafx.scene.control.Alert.AlertType.ERROR);
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

    public void onSearchClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/searchScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
