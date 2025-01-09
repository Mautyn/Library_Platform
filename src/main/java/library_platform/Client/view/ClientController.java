package library_platform.Client.view;

import library_platform.Client.SceneController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

import java.io.IOException;

public class ClientController {
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

    @FXML
    private JFXButton user_button;

    @FXML
    private JFXTextArea search_textArea;

    @FXML
    public void onLoginClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/loginScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}