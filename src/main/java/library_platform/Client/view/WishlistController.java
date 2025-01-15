package library_platform.Client.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.SceneController;
import library_platform.Shared.Book;
import library_platform.Shared.Converters;

import java.io.IOException;
import java.util.ArrayList;

public class WishlistController {

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableColumn<Book, CheckBox> selectColumn;

    public static ArrayList<Book> wishlistBooks = new ArrayList<>();
    public static ArrayList<Book> selectedBooks = new ArrayList<>();



    public void initialize() {
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

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

        ObservableList<Book> booksObservableList = FXCollections.observableList(Converters.convertToObservable(WishlistController.wishlistBooks));
        tableView.setItems(booksObservableList);
    }

    public void onBackClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/mainpageScene.fxml");
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
