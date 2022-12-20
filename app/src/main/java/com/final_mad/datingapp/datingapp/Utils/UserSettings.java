package com.final_mad.datingapp.datingapp.Utils;



public class UserSettings {

    private User user;

    public UserSettings(User user) {
        this.user = user;
    }

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                '}';
    }
}
