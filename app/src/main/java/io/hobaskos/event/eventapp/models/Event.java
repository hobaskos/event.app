package io.hobaskos.event.eventapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andre on 1/25/2017.
 */
public class Event {



    @SerializedName("title")
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
