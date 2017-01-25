package io.hobaskos.event.eventapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andre on 1/25/2017.
 */
public class EventService {

    public static final String URL = "http://localhost:8080";

    public static EventAPI createService() {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EventAPI.class);
    }
}
