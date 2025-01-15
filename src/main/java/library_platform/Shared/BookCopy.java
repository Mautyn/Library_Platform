package library_platform.Shared;

import java.io.Serial;
import java.io.Serializable;

public class BookCopy extends Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 2137692137;

    private boolean isBorrowed;
    private Integer copyId;

}