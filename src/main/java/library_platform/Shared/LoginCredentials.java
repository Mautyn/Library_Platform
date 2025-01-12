package library_platform.Shared;
import java.io.Serializable;

/** klasa serializująca dane logowania w celu wysłania ich do serwera */
public class LoginCredentials implements Serializable {
    private String login;
    private String haslo;
    private String firstName;
    private String lastName;

    public LoginCredentials(String login, String haslo) {
        this.login = login;
        this.haslo = haslo;
    }

    public LoginCredentials(String login, String haslo, String firstName, String lastName) {
        this.login = login;
        this.haslo = haslo;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

}
