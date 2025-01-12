package library_platform.Client.view;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.ConnectionHandler;
import library_platform.Client.SceneController;
import library_platform.Shared.Book;
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
    private TableView<Book> booksTable;
    private ObservableList<Book> books = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle rb) {
        // ustawianie nagłówków dla kolumn
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        // obsługa wyszukiwania
        FilteredList<Book> filteredBooks = new FilteredList<>(books, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBooks.setPredicate(book -> {
                // If filter text is empty, display none.
                if (newValue == null || newValue.isEmpty()) {
                    return false;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        // obsługa sortowania
        SortedList<Book> sortedBooks = new SortedList<>(filteredBooks);
        sortedBooks.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedBooks);
    }
    //todo
    private void getBooks() {
        ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
        //connectionHandler.sendObjectToServer(new Request("update"));
        //commBooks = (ArrayList<CommBook>) connectionHandler.readObjectFromServer();
        //books.setAll(FXCollections.observableArrayList(Converters.convertToObservable(commBooks)));
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