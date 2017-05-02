package io.hobaskos.event.eventapp.ui.event.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.R;

/**
 * Created by alex on 4/25/17.
 */

public class FilterEventsDialog extends DialogFragment {

    @Nullable @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @Nullable @BindView(R.id.appbar_layout)
    protected AppBarLayout appBarLayout;

    public static FilterEventsDialog newInstance() {
        Bundle args = new Bundle();
        FilterEventsDialog fragment = new FilterEventsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.EventFilterDialog);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_pane, new FilterEventsFragment())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter_events, container, false);

        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getString(R.string.filter_events));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event_filter_dialog, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
