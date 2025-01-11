package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library_platform.Client.SceneController;

import java.io.IOException;

public class CategoriesController {
    private JFXButton adventureButton;

    @FXML
    private JFXButton classicButton;

    @FXML
    private JFXButton crimeButton;

    @FXML
    private JFXButton fantasyButton;

    @FXML
    private JFXButton philosophyButton;

    @FXML
    private JFXButton poetryButton;

    @FXML
    private JFXButton psychologyButton;

    @FXML
    private JFXButton religionButton;

    @FXML
    private JFXButton scienceButton;

    @FXML
    private JFXButton thrillerButton;

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
