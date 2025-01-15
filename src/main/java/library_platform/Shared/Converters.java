package library_platform.Shared;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/** klasa przechowuje konwertery używane do konwersji observable na serializable i vice versa */
public class Converters {

    /** konwertuje listę observable [Password] na listę przystosowaną do komunikacji
     * @param booksObservableList observable list do konwersji [Password]
     * @return lista do komunikacji [CommPsswd]
     */
    public static ArrayList<Book> convertToArrayList(ObservableList<Book> booksObservableList) {
        ArrayList<Book> booksArrayList = new ArrayList<>();
        for (Book b : booksObservableList) {
            Book book = new Book(b.getTitle(), b.getAuthor(), b.getYear(), b.getPublisher(), b.getDate());
            book.setId(b.getId());
            book.setCategory(b.getCategory());
            booksArrayList.add(book);
        }
        return booksArrayList;
    }


    /** konwertuje listę seralizowalną [CommPsswd] na listę observable
     * @param booksArrayList - lista przygotowana do komunikacji
     * @return - lista observable do wyświetlania
     */
    public static ObservableList<Book> convertToObservable(ArrayList<Book> booksArrayList) {
        ObservableList<Book> books = FXCollections.observableArrayList();
        for (Book book : booksArrayList) {
            Book bk = new Book(book.getTitle(), book.getAuthor(), book.getYear(), book.getPublisher(), book.getDate());
            bk.setCategory(book.getCategory());
            bk.setId(book.getId());
            books.add(bk);
        }
        return FXCollections.observableArrayList(books);
    }
}
