package io.hobaskos.event.eventapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JwtToken {

    @SerializedName("id_token")
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
