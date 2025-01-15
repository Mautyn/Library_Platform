package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.SceneController;
import library_platform.Shared.Book;

import java.io.IOException;

public class borrowedController {
    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> bookIdColumn;

    @FXML
    private TableColumn<Book, String> rentalDateColumn;

    @FXML
    private JFXButton backButton;

    public void initialize() {
        initialize(bookIdColumn ,titleColumn, authorColumn, rentalDateColumn, tableView, LoginController.loggedInUserEmail);
    }

    static void initialize(TableColumn<Book, String> bookIdColumn,
                           TableColumn<Book, String> titleColumn,
                           TableColumn<Book, String> authorColumn,
                           TableColumn<Book, String> rentalDateColumn,
                           TableView<Book> tableView,
                           String sortBy)
    {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        rentalDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));


        CategoriesController controller = new CategoriesController();

        controller.loadBooksFromDatabase(tableView, sortBy, "BORROWED");
    }

    public void onBackClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/userScene.fxml");
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
            SceneController.setScene(actionEvent, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSearchClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/searchScene.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}