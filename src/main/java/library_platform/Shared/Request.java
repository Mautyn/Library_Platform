package library_platform.Shared;

import java.io.Serial;
import java.io.Serializable;

/** klasa serializująca request w celu wysłania go do serwera */
public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 420692137;

    private String content;

    public Request(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
