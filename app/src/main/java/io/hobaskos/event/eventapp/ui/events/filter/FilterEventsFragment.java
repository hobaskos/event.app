package io.hobaskos.event.eventapp.ui.events.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseFragment;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsFragment extends BaseFragment implements FilterEventsView {
    public final static String TAG = FilterEventsFragment.class.getName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.locationAutoCompleteTextView)
    MultiAutoCompleteTextView locationAutoCompleteTextView;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.seekBarText) TextView seekBarText;
    @BindView(R.id.categorySpinner) Spinner spinner;
    @BindView(R.id.applyFiltersButton)
    Button button;

    private int seekBarProgress;

    @Inject
    public FilterEventsPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInst().getComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_filter_events;
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

        seekBar = (SeekBar) getView().findViewById(R.id.seekBar);
        seekBarText = (TextView) getView().findViewById(R.id.seekBarText);
        button = (Button) getView().findViewById(R.id.applyFiltersButton);


        button.setOnClickListener(v -> presenter.storeDistance(seekBarProgress));



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarText.setVisibility(View.GONE);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarText.setVisibility(View.VISIBLE);
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                seekBarProgress = progress;
                String progressText = String.valueOf(progress) + " km";
                seekBarText.setText(progressText);
                int seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
                seekBarText.setX(seek_label_pos - seekBarText.getWidth() / 2);
            }
        });

        presenter.loadDistance();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "onCreateOptionsMenu()");
        //inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setDistance(int defaultValue) {
        seekBarProgress = defaultValue;
        seekBar.setProgress(seekBarProgress);
    }

    @Override
    public void setCategory() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }


}
