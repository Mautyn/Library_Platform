package library_platform.Client.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library_platform.Client.SceneController;
import library_platform.Client.alert.AlertBuilder;
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
    public static ArrayList<Book> toRemoveList = new ArrayList<>();



    public void initialize() {
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        selectColumn.setCellValueFactory(param -> {
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    System.out.println("Selected to Remove: " + param.getValue().getTitle());
                    WishlistController.toRemoveList.add(new Book(param.getValue().getTitle(), param.getValue().getAuthor(),
                            param.getValue().getYear(), param.getValue().getPublisher(), param.getValue().getCategory()));
                } else {
                    System.out.println("Deselected to remove: " + param.getValue().getTitle());
                    WishlistController.toRemoveList.remove(new Book(param.getValue().getTitle(), param.getValue().getAuthor(),
                            param.getValue().getYear(), param.getValue().getPublisher(), param.getValue().getCategory()));
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

    public void onRemoveClick(ActionEvent actionEvent) {
        wishlistBooks.removeAll(toRemoveList);
        toRemoveList.clear();
        tableView.setItems(FXCollections.observableList(wishlistBooks));

        AlertBuilder.showAlert("SUCCES!", "Selected books removed from wishlist", Alert.AlertType.INFORMATION);
    }
}
