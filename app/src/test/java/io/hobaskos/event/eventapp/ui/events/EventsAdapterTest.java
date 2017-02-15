package io.hobaskos.event.eventapp.ui.events;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
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
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.EventActivity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by andre on 2/15/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
public class EventsAdapterTest {

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
        //stub(mockFragment.getString(anyInt())).toReturn("Candy");
    }

    @Test
    public void itemCount() {
        Event event = new Event();
        adapter.setItems(Arrays.asList(event, event, event));
        adapter.notifyDataSetChanged();
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

    @Test
    public void onBindViewHolder() {
        Event event = new Event();
        event.setTitle("Tittel");
        event.setId(1L);

        adapter.setItems(Arrays.asList(event));
        adapter.notifyDataSetChanged();
        adapter.setContext(mockFragment);
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        View listItemView = inflater.inflate(R.layout.list_item_event, null, false);
        eventViewHolder = adapter.new EventViewHolder(listItemView);
        adapter.onBindViewHolder(eventViewHolder, 0);



        assertThat(eventViewHolder.eventTitle.getText().toString()).isEqualTo(event.getTitle());
        eventViewHolder.itemView.performClick();
        Intent intent = new Intent(mockFragment.getActivity(), EventActivity.class);
        intent.putExtra("event", 1L);
        verify(mockFragment).startActivity(intent);
    }


}
