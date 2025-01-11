package library_platform.Client.view;

import library_platform.Client.SceneController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import java.io.IOException;

public class MainPageController {

    @FXML
    private JFXButton user_button;

    @FXML
    private JFXTextArea search_textArea;

    @FXML
    public void onLoginClick(ActionEvent event) {
        if (LoginController.isLoggedIn) {
            try {
                SceneController.setScene(event, "/library_platform/userScene.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                SceneController.setScene(event, "/library_platform/loginScene.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




}