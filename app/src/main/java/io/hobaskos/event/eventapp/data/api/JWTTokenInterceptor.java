package io.hobaskos.event.eventapp.data.api;

import java.io.IOException;

import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JWTTokenInterceptor implements Interceptor {

    private JwtStorageProxy storageProxy;

    public JWTTokenInterceptor(JwtStorageProxy storageProxy)
    {
        this.storageProxy = storageProxy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String authToken = storageProxy.get();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}
