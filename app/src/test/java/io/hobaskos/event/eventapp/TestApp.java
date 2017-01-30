package io.hobaskos.event.eventapp;

import java.util.Arrays;

import io.hobaskos.event.eventapp.config.TestConstants;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.module.NetModule;
import io.hobaskos.event.eventapp.module.TestNetModule;
import okhttp3.HttpUrl;

/**
 * Created by alex on 1/29/17.
 */

public class TestApp extends App {

    @Override
    protected HttpUrl getApiUrl() {
        return new HttpUrl.Builder()
            .scheme(TestConstants.TEST_SCHEME)
            .host(TestConstants.TEST_HOST)
            .port(TestConstants.TEST_PORT)
            .build();
    }

    @Override
    protected NetModule getNetModule() {

        //TODO this event (and list) should not be created here.
        Event event = new Event();
        event.setId(1L);
        event.setTitle("event1");

        TestNetModule netModule = new TestNetModule(getApiUrl());

        netModule.setEvent(event);
        netModule.setEventList(Arrays.asList(event));

        return netModule;
    }
}
