package io.hobaskos.event.eventapp.ui.location.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.DateTimeVM;
import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2;


/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public class LocationActivity extends MvpActivity<LocationView, LocationPresenter>
        implements GoogleApiClient.OnConnectionFailedListener,
                    TimePickerDialog.OnTimeSetListener, OnDateSetListener, LocationView {

    public final static String EVENT_ID = "eventId";
    public final static String EVENT_STATE = "eventState";
    public final static String LOCATION = "location";

    private EditText name;
    private EditText description;
    private EditText fromDate;
    private EditText fromTime;
    private EditText toDate;
    private EditText toTime;
    private Button create;
    private Button edit;
    private DateTimeVM fromDateTimeVM;
    private DateTimeVM toDateTimeVM;
    private Location location;

    private SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    String googlePlace;
    double lat;
    double lon;
    Long eventId;

    private GoogleApiClient mGoogleApiClient;

    private enum PickerState { FROM, TO }
    public enum State { CREATE, EDIT }

    private PickerState pickerState;
    private State state;

    @Inject
    public LocationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        name = (EditText) findViewById(R.id.activity_add_location_name);

        description = (EditText) findViewById(R.id.activity_add_location_description);

        fromDateTimeVM = new DateTimeVM();
        toDateTimeVM = new DateTimeVM();

        fromDate = (EditText) findViewById(R.id.activity_add_location_from_date);
        fromDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                showDatePickerDialog();
                pickerState = PickerState.FROM;
            }
        });

        fromTime = (EditText) findViewById(R.id.activity_add_location_from_time);
        fromTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                showTimePickerDialog();
                pickerState = PickerState.FROM;
            }
        });

        toDate = (EditText) findViewById(R.id.activity_add_location_to_date);
        toDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                showDatePickerDialog();
                pickerState = PickerState.TO;
            }
        });

        toTime = (EditText) findViewById(R.id.activity_add_location_to_time);
        toTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                showTimePickerDialog();
                pickerState = PickerState.TO;
            }
        });

        create = (Button) findViewById(R.id.activity_add_location_submit);
        create.setOnClickListener(v -> {
            create.setEnabled(false);
            onCreateButtonClicked();
        });

        edit = (Button) findViewById(R.id.activity_add_location_edit);
        edit.setVisibility(View.GONE);
        edit.setOnClickListener(v -> {
            edit.setEnabled(false);
            onEditButtonClicked();
        });

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_location_activity_place_autocomplete_fragment);

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                googlePlace = place.getName().toString();
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                Log.i("LocationActivity", "An error occurred: " + status);
            }
        });

        eventId = getIntent().getExtras().getLong(EVENT_ID);
        setState(getIntent().getIntExtra(EVENT_STATE, 0));

        if(state.equals(State.EDIT)) {
            location = getIntent().getParcelableExtra(LOCATION);
            name.setText(location.getName());
            description.setText(location.getDescription());
            // Todo: refactor to location.getAddress/google-search-query/something
            placeAutocompleteFragment.setText(location.getName());
            fromDateTimeVM = new DateTimeVM(location.getFromDate());
            toDateTimeVM = new DateTimeVM(location.getToDate());
            fromDate.setText(fromDateTimeVM.getDate());
            fromTime.setText(fromDateTimeVM.getTime());
            toDate.setText(toDateTimeVM.getDate());
            toTime.setText(toDateTimeVM.getTime());
            create.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        }

        presenter.attachView(this);
    }



    private void setState(int i) {
        if(i == 0) {
            state = State.CREATE;
            return;
        }

        state = State.EDIT;
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Location not added!", Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public LocationPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog
                .newInstance(
                        LocationActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
        dpd.setVersion(VERSION_2);
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog
                .newInstance(LocationActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        true);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.show(getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (pickerState) {
            case FROM:
                fromDateTimeVM.setDate(year, (monthOfYear + 1), dayOfMonth);
                fromDate.setText(fromDateTimeVM.getDate());
                break;
            case TO:
                toDateTimeVM.setDate(year, (monthOfYear + 1), dayOfMonth);
                toDate.setText(toDateTimeVM.getDate());
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        switch (pickerState) {
            case FROM:
                fromDateTimeVM.setTime(hourOfDay, minute);
                fromTime.setText(fromDateTimeVM.getTime());
                break;
            case TO:
                toDateTimeVM.setTime(hourOfDay, minute);
                toTime.setText(toDateTimeVM.getTime());
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    private void onCreateButtonClicked() {

        if(!validate()) {
            create.setEnabled(true);
            return;
        }

        Location location = new Location();
        location.setEventId(eventId);
        location.setName(name.getText().toString());
        if(!description.getText().toString().isEmpty()) {
            location.setDescription(description.getText().toString());
        }
        location.setFromDate(fromDateTimeVM.getDateTime());
        location.setToDate(toDateTimeVM.getDateTime());
        GeoPoint geoPoint = new GeoPoint(lat, lon);
        location.setGeoPoint(geoPoint);

        presenter.addLocation(location);
    }

    private void onEditButtonClicked() {
        if(!validate()) {
            edit.setEnabled(true);
            return;
        }

        Location location = new Location();
        location.setId(this.location.getId());
        location.setEventId(this.location.getEventId());
        location.setName(name.getText().toString());
        if(!description.getText().toString().isEmpty()) {
            location.setDescription(description.getText().toString());
        }
        location.setFromDate(fromDateTimeVM.getDateTime());
        location.setToDate(toDateTimeVM.getDateTime());
        GeoPoint geoPoint = new GeoPoint(lat, lon);
        location.setGeoPoint(geoPoint);

        presenter.updateLocation(location);


    }

    private boolean validate() {
        boolean valid = true;

        if(name.getText().toString().isEmpty()) {
            valid = false;
            name.setError(getText(R.string.error_message_required_field_title));
        } else {
            name.setError(null);
        }

        // Todo: handle situation where Google Places field is not set.

        if(fromDate.getText().toString().isEmpty()) {
            valid = false;
            fromDate.setError(getText(R.string.error_message_required_field_from_date));
        } else {
            fromDate.setError(null);
        }

        if(fromTime.getText().toString().isEmpty()) {
            valid = false;
            fromTime.setError(getText(R.string.error_message_required_field_from_time));
        } else {
            fromTime.setError(null);
        }

        if(toDate.getText().toString().isEmpty()) {
            valid = false;
            toDate.setError(getText(R.string.error_message_required_field_to_date));
        } else {
            toDate.setError(null);
        }

        if(toTime.getText().toString().isEmpty()) {
            valid = false;
            toTime.setError(getText(R.string.error_message_required_field_to_time));
        } else {
            toTime.setError(null);
        }

        return valid;
    }
}
