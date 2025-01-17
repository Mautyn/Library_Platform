package library_platform.Shared;


import java.sql.Date;

public class Notification {
    private int notifiactionID;
    private int userID;
    private Date date;
    private String notificationType;
    private String content;
    private boolean isPublic;

    public Notification(int notifiactionID, int userID, Date date, String notificationType, String content) {
        this.notifiactionID = notifiactionID;
        this.userID = userID;
        this.date = date;
        this.notificationType = notificationType;
        this.content = content;
        if(notificationType.equals("nowa_ksiazka") || notificationType.equals("ogolne")) {
            isPublic = true;
        } else {
            isPublic = false;
        }
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getNotifiactionID() {
        return notifiactionID;
    }

    public void setNotifiactionID(int notifiactionID) {
        this.notifiactionID = notifiactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
