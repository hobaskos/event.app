package io.hobaskos.event.eventapp.ui.events.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import butterknife.BindView;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseFragment;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsFragment extends BaseFragment {
    public final static String TAG = FilterEventsFragment.class.getName();

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_events;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        setHasOptionsMenu(true);
        toolbar.setTitle("Filter events");
        /*
        toolbar.setNavigationIcon(android.R.drawable.arrow_down_float);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "onCreateOptionsMenu()");
        //inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
