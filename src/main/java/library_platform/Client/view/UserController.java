package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Shared.Book;

import java.io.IOException;

public class UserController {
    @FXML
    private JFXButton logOutButton;

    @FXML
    private JFXButton returnedBooksButton;

    @FXML
    private JFXButton borrowedBooksButton;

    @FXML
    private Text showLoginText;

    public static String buttonType;

    public void initialize() {
        if (LoginController.isLoggedIn) {
            showLoginText.setText(LoginController.loggedInUserEmail);
        }
    }

    public void onLogOutClick(ActionEvent event) {
        try {
            LoginController.isLoggedIn = false;
            AlertBuilder.showAlert("Log out", "You have been logged out.",
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            SceneController.setScene(event, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onborrowedBooksClick(ActionEvent event) {
        try {
            buttonType = "borrowedBooks";
            SceneController.setScene(event, "/library_platform/returnBorrowScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onreturnedBooksClick(ActionEvent event) {
        try {
            buttonType = "returnedBooks";
            SceneController.setScene(event, "/library_platform/returnBorrowScene.fxml");
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

    public void onSearchClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/searchScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
