package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;

import java.io.IOException;

public class UserController {
    @FXML
    private JFXButton logOutButton;

    @FXML
    private JFXButton returnedBooksButton;

    @FXML
    private JFXButton borrowedBooksButton;


    public void onLogOutClick(ActionEvent event) {
        try {
            LoginController.isLoggedIn = false;
            AlertBuilder.showAlert("Log out", "You have been logged out.", javafx.scene.control.Alert.AlertType.INFORMATION);
            SceneController.setScene(event, "/library_platform/hello-view.fxml");
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
