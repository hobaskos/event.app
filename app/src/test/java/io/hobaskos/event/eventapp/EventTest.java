package io.hobaskos.event.eventapp;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;

import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventTest extends BaseApiTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(TEST_PORT);

    private EventRepository eventRepository;

    public EventTest()
    {
        eventRepository = new EventRepository(ApiService.build(url)
                .createService(EventService.class));
    }

    @Test
    public void getEventsTest() {

        String jsonListOfEvents = "[{ \"id\": 1, \"title\": \"event1\"}," +
                "{\"id\": 2, \"title\": \"event2\"}]";

        stubFor(get(urlEqualTo("/api/events"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonListOfEvents)));

        eventRepository.getAll().doOnNext((events) -> {
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

        eventRepository.get(1).doOnNext((events) -> {
            assertTrue(events.getId() == 1);
            assertTrue(events.getTitle().equals("event1"));
        }).subscribe();
    }

    @Test
    public void saveEvent() {
        String singleEvent = "{\"id\": 1, \"title\": \"newEvent\"}";
        Event event = new Event();
        event.setTitle("newEvent");

        stubFor(post(urlEqualTo("/api/events"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(singleEvent)));

        eventRepository.save(event).doOnNext((events) -> {
            assertTrue(events.getId() == 1);
            assertTrue(events.getTitle().equals("newEvent"));
        }).subscribe();

    }

    @Test
    public void updateEvent() {

        String singleEvent = "{\"id\": 1, \"title\": \"newEventTitle\"}";
        Event event = new Event();
        event.setId(1L);
        event.setTitle("newEventTitle");

        stubFor(put(urlEqualTo("/api/events"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(singleEvent)));

        eventRepository.update(event).doOnNext((events) -> {
            assertTrue(events.getId() == 1);
            assertTrue(events.getTitle().equals("newEventTitle"));
        }).subscribe();
    }

    @Test
    public void deleteEvent() {

        stubFor(delete(urlEqualTo("/api/events/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")));

        eventRepository.delete(1).doOnNext((Void) -> {
            // mocking this seems a bit unnecessary...
        });
    }
}
