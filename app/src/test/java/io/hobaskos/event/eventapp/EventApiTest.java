package io.hobaskos.event.eventapp;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import io.hobaskos.event.eventapp.api.EventService;
import io.hobaskos.event.eventapp.models.Event;
import retrofit2.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class EventApiTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Test
    public void getEventsTest() {
        String jsonListOfEvents = "[{ \"id\": 1, \"title\": \"event1\"}," +
                "{\"id\": 2, \"title\": \"event2\"}]";

        stubFor(get(urlEqualTo("/api/events"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonListOfEvents)));

        try {
            Response<List<Event>> listResponse = EventService.createService().getEvents().execute();
            assertTrue(listResponse.isSuccessful());
            List<Event> events = listResponse.body();
            assertTrue(events.size() == 2);
        } catch (IOException ioe) {
            // do nothing for now
        }
    }

    @Test
    public void getEventTest() {

        String singleEvent = "{\"id\": 1, \"title\": \"event1\"}";

        stubFor(get(urlEqualTo("/api/events/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(singleEvent)));

        try {
            Response<Event> listResponse = EventService.createService().getEvent(1).execute();
            assertTrue(listResponse.isSuccessful());
            Event events = listResponse.body();
            assertTrue(events.getId() == 1);
            assertTrue(events.getTitle().equals("event1"));
        } catch (IOException ioe) {
            // do nothing for now
        }

    }
}
