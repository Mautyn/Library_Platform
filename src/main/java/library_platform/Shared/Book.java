package library_platform.Shared;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private boolean isBorrowed;

    public Book(String title) {
        this.title = title;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
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

    @Override
    public String toString() {
        return title + (isBorrowed ? " (Borrowed)" : "");
    }
}
