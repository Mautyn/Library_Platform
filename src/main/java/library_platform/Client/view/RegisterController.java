package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library_platform.Client.SceneController;

import java.awt.*;
import java.io.IOException;

public class RegisterController {
    @FXML
    private Checkbox TermsCheckBox;

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

    public void onRegisterClick(ActionEvent event) {
        try {
            SceneController.setScene(event, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
