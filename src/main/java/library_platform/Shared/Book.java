package library_platform.Shared;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serial;
import java.io.Serializable;

public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 21372137;

    private Integer bookId;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private String category;
    private String date;
    private String ISBN;
    private int numOfCopies;

    // to było simpleBooleanProperty ale zmieniłem na boolean bo sie nie serializowało
    // jeśli checkbox sie wyjebie to mozliwe ze przez to
    private boolean selected;



    public Book(Integer bookId ,String title, String author, String year, String publisher, String date) {
        this.bookId = bookId;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.title = title;
        this.date = date;
    }

    public Book(String title) {
        this.title = title;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public Integer getId() {
        return bookId;
    }

    public void setId(Integer id) {
        this.bookId = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumOfCopies() {
        return numOfCopies;
    }

    public void setNumOfCopies(int numOfCopies) {
        this.numOfCopies = numOfCopies;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!title.equals(book.title)) return false;
        if (!author.equals(book.author)) return false;
        if (!year.equals(book.year)) return false;
        if (!publisher.equals(book.publisher)) return false;
        return category != null ? category.equals(book.category) : book.category == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + publisher.hashCode();
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;

    }
}


