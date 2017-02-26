package io.hobaskos.event.eventapp.ui.events.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import javax.inject.Inject;

import butterknife.BindView;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseFragment;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsFragment extends BaseFragment implements FilterEventsView, GoogleApiClient.OnConnectionFailedListener {
    public final static String TAG = FilterEventsFragment.class.getName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.seekBarText) TextView seekBarText;
    @BindView(R.id.categorySpinner) Spinner spinner;
    @BindView(R.id.applyFiltersButton)
    Button button;

    private int seekBarProgress;
    private SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    private GoogleApiClient mGoogleApiClient;

    String location;
    double lat;
    double lon;

    @Inject
    public FilterEventsPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInst().getComponent().inject(this);
        presenter.attachView(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_filter_events;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeAutocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location = place.getName().toString();
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


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
        seekBarText.setText("");
    }

    @Override
    public void setLocation(String name, double lat, double lon) {
        this.location = name;
        placeAutocompleteFragment.setText(name);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void setCategory() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
