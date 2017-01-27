package io.hobaskos.event.eventapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andre on 1/25/2017.
 */
public class Event {

    @SerializedName("id")
    public Long id;

    @SerializedName("title")
    public String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
