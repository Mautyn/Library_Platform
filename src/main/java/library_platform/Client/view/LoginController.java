package library_platform.Client.view;

import library_platform.Client.SceneController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;

import java.awt.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

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
        try {
            SceneController.setScene(event, "/library_platform/loginScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRegisterClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/loginScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

