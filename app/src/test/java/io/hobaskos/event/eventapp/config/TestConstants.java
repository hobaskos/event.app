package io.hobaskos.event.eventapp.config;

import okhttp3.HttpUrl;

/**
 * Created by alex on 1/29/17.
 */

public class TestConstants {

    public static final int TEST_PORT = 9090;
    public static final String TEST_HOST = "localhost";
    public static final String TEST_SCHEME = "http";

    public static final HttpUrl HTTP_URL = new HttpUrl.Builder()
            .scheme(TestConstants.TEST_SCHEME)
            .host(TestConstants.TEST_HOST)
            .port(TestConstants.TEST_PORT)
            .build();
}
