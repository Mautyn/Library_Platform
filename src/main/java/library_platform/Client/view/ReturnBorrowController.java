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

public class ReturnBorrowController {
    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> bookIdColumn;

    @FXML
    private TableColumn<Book, String> dateColumn;

    @FXML
    private JFXButton backButton;

    public void initialize() {
        initialize(bookIdColumn ,titleColumn, authorColumn, dateColumn, tableView, LoginController.loggedInUserEmail);
    }

    static void initialize(TableColumn<Book, String> bookIdColumn,
                           TableColumn<Book, String> titleColumn,
                           TableColumn<Book, String> authorColumn,
                           TableColumn<Book, String> dateColumn,
                           TableView<Book> tableView,
                           String sortBy)
    {
        String mode = "";

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        CategoriesController controller = new CategoriesController();

        if(UserController.buttonType == "borrowedBooks") {
            dateColumn.setText("rental date");
            mode = "BORROWED";
        }
        if(UserController.buttonType == "returnedBooks") {
            dateColumn.setText("returned date");
            mode = "RETURNED";
        }
        controller.loadBooksFromDatabase(tableView, sortBy, mode);
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
            SceneController.setScene(actionEvent, "/library_platform/mainpageScene.fxml");
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
