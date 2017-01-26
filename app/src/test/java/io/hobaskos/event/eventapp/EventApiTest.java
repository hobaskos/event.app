package io.hobaskos.event.eventapp;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;

import io.hobaskos.event.eventapp.api.ApiService;
import io.hobaskos.event.eventapp.api.EventApi;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertTrue;

public class EventApiTest extends BaseApiTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(TEST_PORT);

    @Test
    public void getEventsTest() {

        String jsonListOfEvents = "[{ \"id\": 1, \"title\": \"event1\"}," +
                "{\"id\": 2, \"title\": \"event2\"}]";

        stubFor(get(urlEqualTo("/api/events"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonListOfEvents)));

        EventApi api = ApiService.build(url).createService(EventApi.class);

        api.getEvents().doOnNext((events) -> {
            assertTrue(events.size() == 2);
        }).subscribe();
    }

    @Test
    public void getEventTest() {

        String singleEvent = "{\"id\": 1, \"title\": \"event1\"}";

        stubFor(get(urlEqualTo("/api/events/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(singleEvent)));

        EventApi api = ApiService.build(url).createService(EventApi.class);

        api.getEvent(1).doOnNext((events) -> {
            assertTrue(events.getId() == 1);
            assertTrue(events.getTitle().equals("event1"));
        }).subscribe();
    }
}
