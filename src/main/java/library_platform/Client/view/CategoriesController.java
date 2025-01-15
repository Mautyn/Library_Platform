package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import library_platform.Client.ConnectionHandler;
import library_platform.Client.SceneController;
import library_platform.Shared.Book;
import library_platform.Shared.Converters;
import library_platform.Shared.Request;

import java.io.IOException;
import java.util.ArrayList;

public class CategoriesController {
    @FXML
    private JFXButton adventureButton;

    @FXML
    private JFXButton classicButton;

    @FXML
    private JFXButton crimeButton;

    @FXML
    private JFXButton fantasyButton;

    @FXML
    private JFXButton historyButton;

    @FXML
    private JFXButton biographyButton;

    @FXML
    private JFXButton horrorButton;

    @FXML
    private JFXButton romanticButton;

    @FXML
    private JFXButton scienceButton;

    @FXML
    private JFXButton thrillerButton;

    public static String category = "";
    public static String category2 = "";

    public void onSearchClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/searchScene.fxml");
        } catch (Exception e) {
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
            SceneController.setScene(actionEvent, "/library_platform/mainpageScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAdventureClick(ActionEvent actionEvent) {
        try {
            category = "Adventure";
            category2 = "Przygodowe";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClassicClick(ActionEvent actionEvent) {
        try {
            category = "Classic";
            category2 = "Literatura obyczajowa";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCrimeClick(ActionEvent actionEvent) {
        try {
            category = "Crime";
            category2 = "Kryminał";

            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFantasyClick(ActionEvent actionEvent) {
        try {
            category = "Fantasy";
            category2 = "Fantastyka";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHistoryClick(ActionEvent actionEvent) {
        try {
            category = "History";
            category2 = "Historyczne";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onBiographyClick(ActionEvent actionEvent) {
        try {
            category = "Biography";
            category2 = "Biografie i autobiografie";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHorrorClick(ActionEvent actionEvent) {
        try {
            category = "Horror";
            category2 = "Horror";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRomanticClick(ActionEvent actionEvent) {
        try {
            category = "Romantic";
            category2 = "Romans";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onScienceClick(ActionEvent actionEvent) {
        try {
            category = "Science Fiction";
            category2 = "Science Fiction";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onThrillerClick(ActionEvent actionEvent) {
        try {
            category = "Thriller";
            category2 = "Thriller";
            SceneController.setScene(actionEvent, "/library_platform/categListScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBooksFromDatabase(TableView<Book> tableView, String sortBy, String mode) {
        ConnectionHandler serverConnection = ConnectionHandler.getInstance();
        Request request = new Request("GET_BOOKS");
        Request searchMode = new Request(mode);
        Request searchQuery = new Request(sortBy);

        serverConnection.sendObjectToServer(request);
        serverConnection.sendObjectToServer(searchMode);
        serverConnection.sendObjectToServer(searchQuery);

        ArrayList<Book> booksArrayList = (ArrayList<Book>) serverConnection.readObjectFromServer();
        ObservableList<Book> booksObservableList = FXCollections.observableList(Converters.convertToObservable(booksArrayList));
        tableView.setItems(booksObservableList);
    }
}
