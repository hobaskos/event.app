package io.hobaskos.event.eventapp.data.api;

import java.io.IOException;

import io.hobaskos.event.eventapp.data.PersistentStorage;
import io.hobaskos.event.eventapp.data.service.JwtTokenProxy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JWTTokenIntercepter implements Interceptor {

    private String authToken;

    public JWTTokenIntercepter(JwtTokenProxy storageProxy)
    {
        authToken = storageProxy.get();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization: Bearer", authToken)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}
