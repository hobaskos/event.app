package io.hobaskos.event.eventapp;

import android.app.Application;

import io.hobaskos.event.eventapp.config.Constants;
import okhttp3.HttpUrl;

/**
 * Created by alex on 1/25/17.
 */
public class App extends Application
{
    private static App inst;

    private HttpUrl apiUrl;

    @Override
    public void onCreate()
    {
        super.onCreate();

        inst = this;

        apiUrl = new HttpUrl.Builder()
                .scheme(Constants.API_SCHEME)
                .host(Constants.API_HOST)
                .port(Constants.API_PORT)
                .build();

    }

    public static App getInst()
    {
        return inst;
    }

    public HttpUrl getApiUrl()
    {
        return apiUrl;
    }
}
