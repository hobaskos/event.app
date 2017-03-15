package io.hobaskos.event.eventapp.ui.event.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseFragment;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsFragment extends BaseFragment
        implements FilterEventsView, GoogleApiClient.OnConnectionFailedListener {
    public final static String TAG = FilterEventsFragment.class.getName();

    Toolbar toolbar;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.seekBarText) TextView seekBarText;
    @BindView(R.id.categorySpinner) Spinner spinner;
    @BindView(R.id.applyFiltersButton) Button button;

    private int seekBarProgress;
    private SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    private GoogleApiClient mGoogleApiClient;

    String location;
    double lat;
    double lon;
    long selectedCategoryId;

    ArrayList<EventCategory> categories;
    ArrayAdapter<EventCategory> spinnerAdapter;

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
        spinner = (Spinner) getView().findViewById(R.id.categorySpinner);
        button = (Button) getView().findViewById(R.id.applyFiltersButton);

        button.setOnClickListener(v -> {
            presenter.storeDistance(seekBarProgress);
            Log.i("jJJJJ", "lat, lon: " + lat + ", " + lon);

            presenter.storeLocation(location, lat, lon);

            if(spinner.getSelectedItem() != null) {
                EventCategory category = (EventCategory) spinner.getSelectedItem();
                presenter.storeCategoryId(category.getId());
            }
            Toast.makeText(getContext(), getString(R.string.filters_applied), Toast.LENGTH_SHORT).show();
        });


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
        presenter.loadLocation();
        presenter.loadCategoryId();
        presenter.loadCategories();
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
    public void setLocation(String name, double lat, double lon) {
        this.location = name;
        placeAutocompleteFragment.setText(name);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void setCategories(List<EventCategory> categories) {
        this.categories = new ArrayList<>();

        // Create category object representing all categories:
        EventCategory allCategoriesOption = new EventCategory("All", 0);

        this.categories.add(allCategoriesOption);
        this.categories.addAll(categories);

        spinnerAdapter = new ArrayAdapter<EventCategory>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, this.categories);
        spinner.setAdapter(spinnerAdapter);

        // Initalize stored category as the selected one in spinner:
        for (EventCategory c : categories) {
            if (c.getId() == selectedCategoryId) {
                spinner.setSelection(spinnerAdapter.getPosition(c));
                break;
            }
        }
    }


    @Override
    public void setCategory(long id) {
        selectedCategoryId = id;
    }

    @Override
    public void showError(Throwable e) {
        Log.i(TAG, e.getMessage());
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
