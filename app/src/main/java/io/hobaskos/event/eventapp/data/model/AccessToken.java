package io.hobaskos.event.eventapp.data.model;

/**
 * Created by hansp on 25.02.2017.
 */

public class AccessToken {

    private String userId;
    private String accessToken;

    public AccessToken(String userId, String accessToken)
    {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
