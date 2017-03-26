package io.hobaskos.event.eventapp.data.model;

import io.hobaskos.event.eventapp.data.model.enumeration.DeviceType;

/**
 * Created by alex on 3/26/17.
 */

public class Device {

    private Long id;

    private String token;

    private DeviceType type;

    private String userLogin;

    public Device(String token) {
        this.token = token;
        type = DeviceType.ANDROID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
