/*
package library_platform.Client.view.book_categories;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.SceneController;
import javafx.fxml.FXML;
import library_platform.Client.view.CategoriesController;
import library_platform.Client.view.LoginController;
import library_platform.Shared.Book;

import java.io.IOException;

    public class adventureController {
        @FXML
        private TableView<Book> tableView;

        @FXML
        private TableColumn<Book, String> titleColumn;

        @FXML
        private TableColumn<Book, String> authorColumn;

        @FXML
        private TableColumn<Book, String> publisherColumn;

        @FXML
        private TableColumn<Book, String> yearColumn;

        @FXML
        private TableColumn<Book, CheckBox> selectColumn;

        @FXML
        private JFXButton addWishlistButton;

    public void initialize() {
        initialize(titleColumn, authorColumn, publisherColumn, yearColumn, selectColumn, tableView, "Przygodowe");
    }

    static void initialize(TableColumn<Book, String> titleColumn, TableColumn<Book, String> authorColumn,
                           TableColumn<Book, String> publisherColumn, TableColumn<Book, String> yearColumn, TableColumn<Book, CheckBox> selectColumn, TableView<Book> tableView, String sortBy) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));


        selectColumn.setCellValueFactory(param -> {
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    System.out.println("Selected: " + param.getValue().getTitle());
                } else {
                    System.out.println("Deselected: " + param.getValue().getTitle());
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        CategoriesController controller = new CategoriesController();

        controller.loadBooksFromDatabase(tableView, sortBy);
    }

    public void onWishListClick(ActionEvent actionEvent) {
        if (LoginController.isLoggedIn) {

            //dodanie do wishlisty dla danego użytkownika

        } else {
            try {
                SceneController.setScene(actionEvent, "/library_platform/loginScene.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
            SceneController.setScene(actionEvent, "/library_platform/hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
*/
