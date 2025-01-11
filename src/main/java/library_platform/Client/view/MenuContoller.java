package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library_platform.Client.SceneController;

import java.io.IOException;

public class MenuContoller {
    @FXML
    private JFXButton main_page_button;

    @FXML
    private JFXButton catogories_button;

    @FXML
    private JFXButton rActivity_button;

    @FXML
    private JFXButton search_button;

    @FXML
    private JFXButton facilities_button;


    public void onCategoriesClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/userScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
