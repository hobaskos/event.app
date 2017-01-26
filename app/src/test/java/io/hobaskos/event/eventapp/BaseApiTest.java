package io.hobaskos.event.eventapp;

import okhttp3.HttpUrl;

/**
 * Created by alex on 1/25/17.
 */
public class BaseApiTest
{
    protected static final int TEST_PORT = 9090;
    protected static final String TEST_HOST = "localhost";
    protected static final String TEST_SCHEME = "http";

    protected HttpUrl url = new HttpUrl.Builder()
            .scheme(TEST_SCHEME)
            .host(TEST_HOST)
            .port(TEST_PORT)
            .build();
}
