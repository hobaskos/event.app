package io.hobaskos.event.eventapp.data.model.response;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class Response  {

    private String message;
    private boolean status;

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
