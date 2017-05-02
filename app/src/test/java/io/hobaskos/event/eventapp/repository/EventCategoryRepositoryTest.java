package io.hobaskos.event.eventapp.repository;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;

import io.hobaskos.event.eventapp.config.TestConstants;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventCategoryService;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertTrue;

public class EventCategoryRepositoryTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(TestConstants.TEST_PORT);

    private EventCategoryRepository eventCategoryRepository;

    public EventCategoryRepositoryTest()
    {
        eventCategoryRepository = new EventCategoryRepository(ApiService.build(TestConstants.HTTP_URL)
                .createService(EventCategoryService.class));
    }

    @Test
    public void getEventCategoriesTest() {

        String jsonListOfEvents = "[ { \"icon\": [ \"null\"], \"iconContentType\": \"null\", \"iconUrl\": \"string\", \"id\": 1, \"theme\": \"RED\", \"title\": \"string\"}]";

        stubFor(get(urlEqualTo("/api/event-categories?page=1&size=10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonListOfEvents)));

        eventCategoryRepository.getAll(1).doOnNext((events) -> {
            assertTrue(events.size() == 1);
            EventCategory eventCategory = events.get(0);
            assertTrue(eventCategory.getTheme().equals(EventCategoryTheme.RED));
            assertTrue(eventCategory.getTitle().equals("string"));
        }).subscribe();
    }
}
