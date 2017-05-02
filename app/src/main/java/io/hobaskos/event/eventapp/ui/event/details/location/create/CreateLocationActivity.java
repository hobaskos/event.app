package io.hobaskos.event.eventapp.ui.event.details.location.create;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Calendar;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.DateTimeVM;
import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.event.create.ActivityState;

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2;


/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public class CreateLocationActivity extends MvpActivity<CreateLocationView, CreateLocationPresenter>
        implements GoogleApiClient.OnConnectionFailedListener,
                    TimePickerDialog.OnTimeSetListener, OnDateSetListener, CreateLocationView {

    public final static String EVENT_ID = "eventId";
    public final static String EVENT_STATE = "eventState";
    public final static String LOCATION = "location";

    private EditText name;
    private EditText description;
    private TextView fromDate;
    private TextView fromTime;
    private TextView toDate;
    private TextView toTime;
    private LinearLayout fromDateTime;
    private LinearLayout toDateTime;
    private DateTimeVM fromDateTimeVM;
    private DateTimeVM toDateTimeVM;
    private Location location;

    private SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    private String googlePlace;
    private double lat;
    private double lon;
    private Long eventId;
    private String address;

    private GoogleApiClient mGoogleApiClient;

    private enum DateTimePickerState { FROM, TO }

    private DateTimePickerState dateTimePickerState;
    private ActivityState activityState;

    @Inject
    public CreateLocationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        name = (EditText) findViewById(R.id.activity_add_location_name);

        description = (EditText) findViewById(R.id.activity_add_location_description);

        setTitle(R.string.add_location);

        fromDate = (TextView) findViewById(R.id.activity_add_location_from_date);
        fromTime = (TextView) findViewById(R.id.activity_add_location_from_time);
        fromDateTime = (LinearLayout)findViewById(R.id.from_date_time);
        fromDateTime.setOnClickListener(view -> {
            showDatePickerDialog();
            dateTimePickerState = DateTimePickerState.FROM;
        });

        toDate = (TextView) findViewById(R.id.activity_add_location_to_date);
        toTime = (TextView) findViewById(R.id.activity_add_location_to_time);
        toDateTime = (LinearLayout) findViewById(R.id.to_date_time);
        toDateTime.setOnClickListener(view -> {
            showDatePickerDialog();
            dateTimePickerState = DateTimePickerState.TO;
        });

        fromDateTimeVM = new DateTimeVM();
        toDateTimeVM = new DateTimeVM();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_location_activity_place_autocomplete_fragment);

        eventId = getIntent().getExtras().getLong(EVENT_ID);
        setState(getIntent().getIntExtra(EVENT_STATE, 0));

        if (activityState.equals(ActivityState.EDIT)) {
            setTitle(R.string.edit_location);
            location = getIntent().getParcelableExtra(LOCATION);
            name.setText(location.getName());
            lat = location.getGeoPoint().getLat();
            lon = location.getGeoPoint().getLon();
            description.setText(location.getDescription());
            placeAutocompleteFragment.setText(location.getSearchName());
            fromDateTimeVM = new DateTimeVM(location.getFromDate());
            toDateTimeVM = new DateTimeVM(location.getToDate());
            fromDate.setText(fromDateTimeVM.getDate());
            fromTime.setText(fromDateTimeVM.getTime());
            toDate.setText(toDateTimeVM.getDate());
            toTime.setText(toDateTimeVM.getTime());
            googlePlace = location.getSearchName();
        }

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                googlePlace = place.getName().toString();
                address = place.getAddress().toString();
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                Log.i("LocationActivity", "An error occurred: " + status);
            }
        });

        presenter.attachView(this);
    }

    private void setState(int i) {
        if (i == 0) {
            activityState = ActivityState.CREATE;
            return;
        }
        activityState = ActivityState.EDIT;
    }

    @Override
    public void onSuccess() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onFailure() {
        if(activityState.equals(ActivityState.EDIT)) {
            Toast.makeText(this, R.string.could_not_edit_location, Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, R.string.could_not_add_location, Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public CreateLocationPresenter createPresenter() {
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
        int year, month, day;
        if (dateTimePickerState == DateTimePickerState.FROM) {
            year = fromDateTimeVM.getYear();
            month = fromDateTimeVM.getMonthOfYear() - 1;
            day = fromDateTimeVM.getDayOfMonth();
        } else {
            year = toDateTimeVM.getYear();
            month = toDateTimeVM.getMonthOfYear() - 1;
            day = toDateTimeVM.getDayOfMonth();
        }
        DatePickerDialog dpd = DatePickerDialog
                .newInstance(
                        CreateLocationActivity.this,
                        year, month, day);
        dpd.setVersion(VERSION_2);
        dpd.show(getFragmentManager(), "DatePickerDialog");

    }

    private void showTimePickerDialog() {
        int hour, minute;
        if (dateTimePickerState == DateTimePickerState.FROM) {
            hour = fromDateTimeVM.getHour();
            minute = fromDateTimeVM.getMinute();
        } else {
            hour = toDateTimeVM.getHour();
            minute = toDateTimeVM.getMinute();
        }
        TimePickerDialog tpd = TimePickerDialog
                .newInstance(CreateLocationActivity.this,
                        hour, minute, 0, true);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.show(getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (dateTimePickerState) {
            case FROM:
                fromDateTimeVM.setDate(year, (monthOfYear + 1), dayOfMonth);
                fromDate.setText(fromDateTimeVM.getDate());
                fromDate.setError(null);
                break;
            case TO:
                toDateTimeVM.setDate(year, (monthOfYear + 1), dayOfMonth);
                toDate.setText(toDateTimeVM.getDate());
                toDate.setError(null);
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
        showTimePickerDialog();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        switch (dateTimePickerState) {
            case FROM:
                fromDateTimeVM.setTime(hourOfDay, minute);
                fromTime.setText(fromDateTimeVM.getTime());
                fromTime.setError(null);
                break;
            case TO:
                toDateTimeVM.setTime(hourOfDay, minute);
                toTime.setText(toDateTimeVM.getTime());
                toTime.setError(null);
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    private void onCreateButtonClicked() {
        if (!validate()) { return; }

        Location location = new Location();
        location.setEventId(eventId);
        location.setName(name.getText().toString());
        if (!description.getText().toString().isEmpty()) {
            location.setDescription(description.getText().toString());
        }
        location.setFromDate(fromDateTimeVM.getDateTime());
        location.setToDate(toDateTimeVM.getDateTime());
        location.setGeoPoint(new GeoPoint(lat, lon));
        location.setAddress(address);
        location.setSearchName(googlePlace);

        presenter.addLocation(location);
    }

    private void onEditButtonClicked() {
        if (!validate()) { return; }

        location.setName(name.getText().toString());

        if (!description.getText().toString().isEmpty()) {
            location.setDescription(description.getText().toString());
        }

        location.setFromDate(fromDateTimeVM.getDateTime());
        location.setToDate(toDateTimeVM.getDateTime());
        location.setGeoPoint(new GeoPoint(lat, lon));

        if (address != null && !address.equals("")) {
            location.setAddress(address);
        }

        if (googlePlace != null && !googlePlace.equals("")) {
            location.setSearchName(googlePlace);
        }

        presenter.updateLocation(location);
    }

    private boolean validate() {
        boolean valid = true;

        if (name.getText().toString().isEmpty()) {
            valid = false;
            name.setError(getText(R.string.error_message_required_field_title));
        } else {
            name.setError(null);
        }

        if (googlePlace == null) {
            valid = false;
            Toast.makeText(this, R.string.place_is_a_required_field, Toast.LENGTH_SHORT).show();
        } 
        // Todo: handle situation where Google Places field is not set.

        if (fromDate.getText().toString().isEmpty()) {
            valid = false;
            fromDate.setError(getText(R.string.error_message_required_field_from_date));
        } else {
            fromDate.setError(null);
        }

        if (fromTime.getText().toString().isEmpty()) {
            valid = false;
            fromTime.setError(getText(R.string.error_message_required_field_from_time));
        } else {
            fromTime.setError(null);
        }

        if (toDate.getText().toString().isEmpty()) {
            valid = false;
            toDate.setError(getText(R.string.error_message_required_field_to_date));
        } else {
            toDate.setError(null);
        }

        if (toTime.getText().toString().isEmpty()) {
            valid = false;
            toTime.setError(getText(R.string.error_message_required_field_to_time));
        } else {
            toTime.setError(null);
        }

        if (fromDateTimeVM.getDateTime().isAfter(toDateTimeVM.getDateTime())) {
            valid = false;
            fromDate.setError(getText(R.string.error_from_date_is_after_to_date));
            toDate.setError(getText(R.string.error_from_date_is_after_to_date));
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:
                if (activityState.equals(ActivityState.EDIT)) onEditButtonClicked();
                else onCreateButtonClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.locations_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
