package io.hobaskos.event.eventapp.data.model;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class UserLogin {

    private String username;
    private String password;
    private boolean rememberMe = false;

    public UserLogin(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
