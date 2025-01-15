package library_platform.Client.view;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.ConnectionHandler;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
import library_platform.Shared.Book;
import library_platform.Shared.Converters;
import library_platform.Shared.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class SearchController implements Initializable {
    /**
     * Pole tekstowe wyszukiwania
     */
    @FXML
    private TextField searchField;
    /**
     * Kolumna tabeli z nazwami serwisów we wpisach
     */
    @FXML
    private TableColumn<Book, String> titleCol;
    /**
     * Kolumna tabeli z url do serwisu we wpisach
     */
    @FXML
    private TableColumn<Book, String> authorCol;
    /**
     * Kolumna tabeli z hasłami
     */
    @FXML
    private TableColumn<Book, String> categoryCol;
    /**
     * Tabela z wpisami
     */
    @FXML
    private TableColumn<Book, CheckBox> checkboxColumn;

    @FXML
    private TableView<Book> booksTable;
    private ObservableList<Book> books = FXCollections.observableArrayList();
    public static ArrayList<Book> wishlistBooks = new ArrayList<>();


    public void initialize(URL url, ResourceBundle rb) {
        // ustawianie nagłówków dla kolumn
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchFieldChange(newValue);
        });

        checkboxColumn.setCellValueFactory(param -> {
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    System.out.println("Selected: " + param.getValue().getTitle());
                    UserController.selectedBooks.add(new Book(param.getValue().getTitle(), param.getValue().getAuthor(),
                            param.getValue().getYear(), param.getValue().getPublisher(), param.getValue().getCategory()));
                } else {
                    System.out.println("Deselected: " + param.getValue().getTitle());
                    UserController.selectedBooks.remove(new Book(param.getValue().getTitle(), param.getValue().getAuthor(),
                            param.getValue().getYear(), param.getValue().getPublisher(), param.getValue().getCategory()));
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        // obsługa wyszukiwania
        FilteredList<Book> filteredBooks = new FilteredList<>(books, p -> true);

        // obsługa sortowania
        SortedList<Book> sortedBooks = new SortedList<>(filteredBooks);
        sortedBooks.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedBooks);

        onSearchFieldChange("");

    }

    public void onSearchFieldChange(String searchQuery) {
        ConnectionHandler serverConnection = ConnectionHandler.getInstance();
        Request request = new Request("GET_BOOKS");
        Request searchMode = new Request("FULL");
        Request query = new Request(searchQuery);

        serverConnection.sendObjectToServer(request);
        serverConnection.sendObjectToServer(searchMode);
        serverConnection.sendObjectToServer(query);

        ArrayList<Book> booksArrayList = (ArrayList<Book>) serverConnection.readObjectFromServer();
        ObservableList<Book> booksObservableList = FXCollections.observableList(Converters.convertToObservable(booksArrayList));
        booksTable.setItems(booksObservableList);
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
            SceneController.setScene(actionEvent, "/library_platform/mainpageScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAddToWistlistClick(ActionEvent actionEvent) {
        if(LoginController.isLoggedIn){
            wishlistBooks = UserController.selectedBooks;
        }
        AlertBuilder.showAlert("SUCCES!", "Books added to wishlist", Alert.AlertType.INFORMATION);

    }
}