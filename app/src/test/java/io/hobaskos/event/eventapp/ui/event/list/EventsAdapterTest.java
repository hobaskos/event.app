package io.hobaskos.event.eventapp.ui.event.list;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.model.Event;

import static org.assertj.android.recyclerview.v7.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by andre on 2/15/2017.
 */

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestApp.class)
public class EventsAdapterTest {

    private Context context;

    private EventsAdapter adapter;
    private EventsAdapter.EventViewHolder eventViewHolder;
    private EventsAdapter.LoadMoreViewHolder loadMoreViewHolder;

    @Mock
    private View mockView;

    @Mock
    private Fragment mockFragment;

    @Mock
    private List<Event> eventList;

    @Before
    public void setup() throws Exception {
        adapter = new EventsAdapter(null, null, null);
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application;
        //stub(mockFragment.getString(anyInt())).toReturn("Candy");
    }

    @Test
    public void itemCount() {
        Event event = new Event();
        adapter.setItems(Arrays.asList(event, event, event));
        assertThat(adapter.getItemCount()).isEqualTo(3);
    }


    @Test
    public void getItemAtPosition() {
        Event firstEvent = new Event();
        Event secondEvent = new Event();
        adapter.setItems(Arrays.asList(firstEvent, secondEvent));
        assertThat(adapter.getItemAtPosition(0)).isEqualTo(firstEvent);
        assertThat(adapter.getItemAtPosition(1)).isEqualTo(secondEvent);
    }

    /* TODO:
        eventViewHolder.itemView.performClick();
        Intent intent = new Intent(mockFragment.getActivity(), EventActivity.class);
        intent.putExtra("event", 1L);
        verify(mockFragment).startActivity(intent);
    */

    @Test
    public void onBindViewHolder() {
        // Set up input
        Event event1 = new Event();
        event1.setTitle("Tittel 1");
        event1.setId(1L);

        Event event2 = new Event();
        event2.setTitle("Tittel 2");
        event2.setId(2L);

        List<Event> events = Arrays.asList(event1, event2);
        EventsAdapter adapter = new EventsAdapter(events, null, null);
        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));

        // Run test
        EventsAdapter.EventViewHolder viewHolder = (EventsAdapter.EventViewHolder)
                adapter.createViewHolder(rvParent, 0);


        adapter.onBindViewHolder(viewHolder, 0);
        assertThat(adapter.getItemViewType(0)).isEqualTo(0);
        assertEquals("Tittel 1", viewHolder.eventTitle.getText().toString());
        assertThat(adapter).hasItemCount(2);

        adapter.onBindViewHolder(viewHolder, 1);
        assertThat(adapter.getItemViewType(0)).isEqualTo(0);
        assertEquals("Tittel 2", viewHolder.eventTitle.getText().toString());
        assertThat(adapter).hasItemCount(2);

        // SetLoadMore:
        adapter.setLoadMore(true);
        assertThat(adapter.getItemViewType(2)).isEqualTo(1);
        assertThat(adapter).hasItemCount(3);
        adapter.setLoadMore(false);
        assertThat(adapter).hasItemCount(2);
    }
}