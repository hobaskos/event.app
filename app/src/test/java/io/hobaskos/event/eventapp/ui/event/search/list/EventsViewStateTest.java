package io.hobaskos.event.eventapp.ui.event.search.list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by andre on 2/15/2017.
 */

public class EventsViewStateTest {

    @Mock
    private EventsView view;

    private List<EventsPresentationModel> eventList;
    private EventsViewState viewState;

    private boolean pullToRefresh = false;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewState = new EventsViewState();
        eventList = new ArrayList<>();
    }


    @Test
    public void testShowLoadTrueMoreWhenShowingContent() {
        viewState.setStateShowContent(eventList);
        viewState.setLoadingMore(true);
        viewState.apply(view, false);

        verify(view, times(1)).showLoadMore(true);
        verify(view, times(1)).setData(anyListOf(EventsPresentationModel.class));
        verify(view, times(1)).showContent();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testShowLoadMoreFalseWhenShowingContent() {
        viewState.setStateShowContent(eventList);
        viewState.setLoadingMore(false);
        viewState.apply(view, false);

        verify(view, times(1)).showLoadMore(false);
        verify(view, times(1)).setData(anyListOf(EventsPresentationModel.class));
        verify(view, times(1)).showContent();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testShowLoadMoreTrueWhenNotShowingContent() {
        viewState.setLoadingMore(true);
        viewState.apply(view, false);

        verify(view, times(1)).loadData(false);
        verifyNoMoreInteractions(view);
    }
}
