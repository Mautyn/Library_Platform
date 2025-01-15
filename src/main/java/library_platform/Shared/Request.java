package library_platform.Shared;

import java.io.Serial;
import java.io.Serializable;

/** klasa serializująca request w celu wysłania go do serwera */
public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 420692137;

    private String content;
    private boolean success;
    private String searchMode;
    private String searchQuery;

    public Request(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess() {
        this.success = true;
    }

    public void setFail() {
        this.success = false;
    }
}
