package library_platform.Client.view;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import library_platform.Client.SceneController;
import library_platform.Shared.Book;
import library_platform.Shared.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void onAdventureClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/adventureScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClassicClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/classicScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCrimeClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/crimeScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFantasyClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/fantasyScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHistoryClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/historyScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onBiographyClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/biographyScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHorrorClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/horrorScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRomanticClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/romanticScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onScienceClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/scienceFictionScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onThrillerClick(ActionEvent actionEvent) {
        try {
            SceneController.setScene(actionEvent, "/library_platform/book_categories/thrillerScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBooksFromDatabase(TableView<Book> tableView, String sortBy) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Tytul, Autor, Rok_wydania, Wydawnictwo FROM ksiazka where Gatunek = '" + sortBy + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ObservableList<Book> bookList;
            bookList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                bookList.add(new Book(
                        resultSet.getString("Tytul"),
                        resultSet.getString("Autor"),
                        resultSet.getString("Rok_wydania"),
                        resultSet.getString("Wydawnictwo")
                ));
            }

            tableView.setItems(bookList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
