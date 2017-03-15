package io.hobaskos.event.eventapp.data.api;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.lang.reflect.Type;
import java.util.Date;

import io.hobaskos.event.eventapp.data.model.Event;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andre on 1/25/2017.
 */


public class ApiService
{
    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final Retrofit.Builder builder = new Retrofit.Builder();
    
    private final Gson serializer = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
            .create();

    private ApiService(HttpUrl httpUrl) {

        builder.baseUrl(httpUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(serializer));

    }

    public static ApiService build(HttpUrl httpUrl) {
        return new ApiService(httpUrl);
    }

    public <S> S createService(Class<S> serviceClass, Cache cache) {
        return builder.client(httpClient.cache(cache).build())
                .build()
                .create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass) {
        return builder.client(httpClient.build())
                .build()
                .create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass, Cache cache, Interceptor interceptor) {
        return builder.client(httpClient
                .cache(cache)
                .addInterceptor(interceptor)
                .build()
        ).build().create(serviceClass);
    }
}
