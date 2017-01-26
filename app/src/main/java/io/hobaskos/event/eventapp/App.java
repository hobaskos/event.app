package io.hobaskos.event.eventapp;

import android.app.Application;

import io.hobaskos.event.eventapp.config.Constants;
import io.hobaskos.event.eventapp.module.AppModule;
import io.hobaskos.event.eventapp.module.DaggerDiComponent;
import io.hobaskos.event.eventapp.module.DiComponent;
import io.hobaskos.event.eventapp.module.NetModule;
import okhttp3.HttpUrl;

/**
 * Created by alex on 1/25/17.
 */
public class App extends Application
{
    private static App inst;

    private DiComponent diComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        inst = this;

        HttpUrl apiUrl = new HttpUrl.Builder()
                .scheme(Constants.API_SCHEME)
                .host(Constants.API_HOST)
                .port(Constants.API_PORT)
                .build();

        diComponent = DaggerDiComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(apiUrl))
                .build();

    }

    public static App getInst()
    {
        return inst;
    }

    public DiComponent getDiComponent() {
        return diComponent;
    }
}
