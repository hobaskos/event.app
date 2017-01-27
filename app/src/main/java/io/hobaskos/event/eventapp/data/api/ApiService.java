package io.hobaskos.event.eventapp.data.api;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andre on 1/25/2017.
 */
public class ApiService
{
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = new Retrofit.Builder();

    private ApiService(HttpUrl httpUrl) {
        builder.baseUrl(httpUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    public static ApiService build(HttpUrl httpUrl) {
        return new ApiService(httpUrl);
    }

    public <S> S createService(Class<S> serviceClass)
    {
        return builder.client(httpClient.build())
                .build()
                .create(serviceClass);
    }
}
