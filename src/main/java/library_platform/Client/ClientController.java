package library_platform.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onConnectButtonClick() {
        welcomeText.setText("Connecting to the server...");
        // Tutaj dodaj logikę łączenia z serwerem
    }
}