package io.hobaskos.event.eventapp.data.api;

import android.net.Proxy;

import java.io.IOException;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JWTTokenInterceptor implements Interceptor {

    private String authToken;
    JwtStorageProxy storageProxy;

    public JWTTokenInterceptor(JwtStorageProxy storageProxy)
    {
        this.storageProxy = storageProxy;
        authToken = storageProxy.get();
    }

    public Request authenticate(Proxy proxy, Response response) throws IOException
    {
        String newToken = storageProxy.get();

        return response.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + newToken)
                .build();
    }

    public boolean authorizationTokenIsEmpty()
    {
        return !storageProxy.isSet();
    }

    public boolean alreadyHasAuthorizationHeader()
    {
        return false;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = storageProxy.get();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}
