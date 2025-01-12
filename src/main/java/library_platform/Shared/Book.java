package library_platform.Shared;

import java.io.Serializable;

public class Book implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private String category;
    private boolean isBorrowed;

    public Book(String title, String author, String year, String publisher) {

        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.title = title;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow() {
        this.isBorrowed = true;
    }

    public void returnBook() {
        this.isBorrowed = false;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    @Override
    public String toString() {
        return title + (isBorrowed ? " (Borrowed)" : "");
    }
}
