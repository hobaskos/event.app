package io.hobaskos.event.eventapp.repository;

import android.util.Log;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.Set;

import io.hobaskos.event.eventapp.config.TestConstants;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.repository.EventRepository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import static org.junit.Assert.*;

public class EventRepositoryTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(TestConstants.TEST_PORT);

    private EventRepository eventRepository;

    public EventRepositoryTest()
    {
        eventRepository = new EventRepository(
                ApiService.build(TestConstants.HTTP_URL).createService(EventService.Anonymously.class),
                ApiService.build(TestConstants.HTTP_URL).createService(EventService.Authenticated.class));
    }

    @Test
    public void getEventsTest() {

        String jsonListOfEvents = "[{ \"id\": 1, \"title\": \"event1\"}," +
                "{\"id\": 2, \"title\": \"event2\"}]";

        stubFor(get(urlEqualTo("/api/events?page=1&size=20"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonListOfEvents)));

        eventRepository.getAll(1).doOnNext((events) -> {
            assertTrue(events.size() == 2);
        }).subscribe();
    }

    @Test
    public void getEventTest() {

        //String singleEvent = "{\"id\": 1, \"title\": \"event1\"}";

        String singleEvent = "{" +
                "\"id\": 1, " +
                "\"title\": \"event1\", " +
                "\"description\": \"desc1\", " +
                "\"imageUrl\": \"image-url1\", " +
                "\"fromDate\": \"2017-02-21T14:06:48.783+01:00\", " +
                "\"toDate\": \"2017-02-21T14:06:51.416+01:00\", " +
                "\"ownerId\": 1," +
                "\"locations\": [" +
                "      {" +
                "        \"id\": 1, " +
                "        \"name\": \"Blob\", " +
                "        \"description\": \"dop\", " +
                "        \"geoPoint\": {" +
                "          \"lat\": 10," +
                "          \"lon\": 10" +
                "        }, " +
                "        \"fromDate\": \"2017-02-21T14:06:48.783+01:00\", " +
                "        \"toDate\": \"2017-02-21T14:06:51.416+01:00\", " +
                "        \"vector\": 1," +
                "        \"eventId\": 1" +
                "      } " +
                "    ]}";


        stubFor(get(urlEqualTo("/api/events/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(singleEvent)));


        Event event = eventRepository.get(1L).toBlocking().first();

        assertTrue(event.getId() == 1);
        assertTrue(event.getTitle().equals("event1"));
        assertTrue(event.getDescription().equals("desc1"));
        assertTrue(event.getImageUrl().equals("image-url1"));
        assertTrue(event.getOwnerId() == 1);
        assertTrue(event.getFromDate().getYear() == 2017);
        assertTrue(event.getFromDate().getMonthOfYear() == 2);
        assertTrue(event.getFromDate().getDayOfMonth() == 21);
        assertTrue(event.getFromDate().getHourOfDay() == 14);
        assertTrue(event.getFromDate().getMinuteOfHour() == 6);
        assertTrue(event.getFromDate().getSecondOfMinute() == 48);
        assertTrue(event.getFromDate().getMillisOfSecond() == 783);
        assertTrue(event.getToDate().getYear() == 2017);
        assertTrue(event.getToDate().getMonthOfYear() == 2);
        assertTrue(event.getToDate().getDayOfMonth() == 21);
        assertTrue(event.getToDate().getHourOfDay() == 14);
        assertTrue(event.getToDate().getMinuteOfHour() == 6);
        assertTrue(event.getToDate().getSecondOfMinute() == 51);
        assertTrue(event.getToDate().getMillisOfSecond() == 416);


        Set<Location> locations = event.getLocations();

        assertFalse(locations.size() == 0);

        for(Location location : locations)
        {
            assertTrue(location.getDescription().equals("dop"));
            assertTrue(location.getName().equals("Blob"));
            assertTrue(location.getEventId() == event.id);
            assertTrue(location.getGeoPoint().getLat() == 10);
            assertTrue(location.getGeoPoint().getLon() == 10);
            assertTrue(location.getId() == 1);
            assertTrue(location.getVector() == 1);
            assertTrue(location.getFromDate().getYear() == 2017);
            assertTrue(location.getFromDate().getMonthOfYear() == 2);
            assertTrue(location.getFromDate().getDayOfMonth() == 21);
            assertTrue(location.getFromDate().getHourOfDay() == 14);
            assertTrue(location.getFromDate().getMinuteOfHour() == 6);
            assertTrue(location.getFromDate().getSecondOfMinute() == 48);
            assertTrue(location.getFromDate().getMillisOfSecond() == 783);
            assertTrue(location.getToDate().getYear() == 2017);
            assertTrue(location.getToDate().getMonthOfYear() == 2);
            assertTrue(location.getToDate().getDayOfMonth() == 21);
            assertTrue(location.getToDate().getHourOfDay() == 14);
            assertTrue(location.getToDate().getMinuteOfHour() == 6);
            assertTrue(location.getToDate().getSecondOfMinute() == 51);
            assertTrue(location.getToDate().getMillisOfSecond() == 416);
        }
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

        eventRepository.delete(1L).doOnNext((Void) -> {
            // mocking this seems a bit unnecessary...
        });
    }
}
