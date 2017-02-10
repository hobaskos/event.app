package io.hobaskos.event.eventapp.data.model;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class UserLogin {

    private String login;
    private String password;
    private boolean rememberMe;

    public UserLogin(String login, String password, boolean rememberMe) {
        this.login = login;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
