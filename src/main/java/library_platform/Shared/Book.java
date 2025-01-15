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

    // to było simpleBooleanProperty ale zmieniłem na boolean bo sie nie serializowało
    // jeśli checkbox sie wyjebie to mozliwe ze przez to
    private boolean selected;



    public Book(String title, String author, String year, String publisher, String date) {
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
}


