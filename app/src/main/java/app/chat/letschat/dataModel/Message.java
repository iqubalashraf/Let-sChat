package app.chat.letschat.dataModel;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class Message {
    String text, createdAt, name, friend_socket_id, socket_id, country;
    boolean notification;
    int gender, viewType;

    public Message(String text, String createdAt, String name, String friend_socket_id, String socket_id, String country, boolean notification, int gender, int viewType) {
        this.text = text;
        this.createdAt = createdAt;
        this.name = name;
        this.friend_socket_id = friend_socket_id;
        this.socket_id = socket_id;
        this.country = country;
        this.notification = notification;
        this.gender = gender;
        this.viewType = viewType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriend_socket_id() {
        return friend_socket_id;
    }

    public void setFriend_socket_id(String friend_socket_id) {
        this.friend_socket_id = friend_socket_id;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
