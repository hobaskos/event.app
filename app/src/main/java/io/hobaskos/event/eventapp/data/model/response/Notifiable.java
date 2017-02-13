package io.hobaskos.event.eventapp.data.model.response;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public interface Notifiable {

    void setMessage(String message);
    String getMessage();
    boolean getStatus();

}
