package io.hobaskos.event.eventapp.data.model;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class LoginVM {

    private String username;
    private String password;
    private boolean rememberMe = true;

    public LoginVM(String username, String password) {
        this.username = username;
        this.password = password;
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
}
