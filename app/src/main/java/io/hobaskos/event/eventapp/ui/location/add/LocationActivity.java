package io.hobaskos.event.eventapp.ui.location.add;

import android.content.Context;
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

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2;


/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public class LocationActivity extends MvpActivity<LocationView, LocationPresenter>
        implements GoogleApiClient.OnConnectionFailedListener,
                    TimePickerDialog.OnTimeSetListener, OnDateSetListener, LocationView {

    public final static String EVENT_ID = "eventId";

    private EditText name;
    //@BindView(R.id.activity_add_location_location)private EditText location;
    private EditText description;
    private EditText fromDate;
    private String fromDateString;
    private EditText fromTime;
    private String fromTimeString;
    private EditText toDate;
    private String toDateString;
    private EditText toTime;
    private String toTimeString;
    private Button create;
    private DateTimeVM fromDateTimeVM;
    private DateTimeVM toDateTimeVM;

    private SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    String location;
    double lat;
    double lon;
    Long eventId;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Location added!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Location not added!", Toast.LENGTH_LONG).show();
    }

    private enum PickerState { FROM, TO }
    private PickerState pickerState;

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
        fromDate.setOnClickListener(v -> {
            showDatePickerDialog();
            pickerState = PickerState.FROM;
        });

        fromTime = (EditText) findViewById(R.id.activity_add_location_from_time);
        fromTime.setOnClickListener(v -> {
            showTimePickerDialog();
            pickerState = PickerState.FROM;
        });

        toDate = (EditText) findViewById(R.id.activity_add_location_to_date);
        toDate.setOnClickListener(v -> {
            showDatePickerDialog();
            pickerState = PickerState.TO;
        });

        toTime = (EditText) findViewById(R.id.activity_add_location_to_time);
        toTime.setOnClickListener(v -> {
            showTimePickerDialog();
            pickerState = PickerState.TO;
        });

        create = (Button) findViewById(R.id.activity_add_location_submit);
        create.setOnClickListener(v -> {
            create.setEnabled(false);
            onCreateButtonClicked();
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
                location = place.getName().toString();
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("LocationActivity", "An error occurred: " + status);
            }
        });

        eventId = getIntent().getExtras().getLong(EVENT_ID);

        // Todo: handle add location calls without event id?

        presenter.attachView(this);
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
                fromDateTimeVM.setDate(year, monthOfYear, dayOfMonth);
                fromDate.setText(year + "-" + formatNumber(monthOfYear + 1) + "-" + formatNumber(dayOfMonth));
                break;
            case TO:
                toDateTimeVM.setDate(year, monthOfYear, dayOfMonth);
                toDate.setText(year + "-" + formatNumber(monthOfYear + 1) + "-" + formatNumber(dayOfMonth));
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
                fromTime.setText(formatNumber(hourOfDay) +  ":" + formatNumber(minute));
                break;
            case TO:
                toDateTimeVM.setTime(hourOfDay, minute);
                toTime.setText(formatNumber(hourOfDay) +  ":" + formatNumber(minute));
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
            location.setDescription(name.getText().toString());
        }

        String fromDateTime = fromDate.getText().toString() + " " + fromTime.getText().toString();
        location.setFromDate(parseToLocalDateTime(fromDateTimeVM));

        Log.i("LocationActivity", "fromDate=" + location.getFromDate());

        String toDateTime = toDate.getText().toString() + " " + toTime.getText().toString();
        location.setToDate(parseToLocalDateTime(toDateTimeVM));

        GeoPoint geoPoint = new GeoPoint(lat, lon);
        location.setGeoPoint(geoPoint);

        presenter.addLocation(location);
    }

    private String formatNumber(int number) {

        String newNumber = "";

        switch (number) {
            case 0:
                newNumber = "00";
                break;
            case 1:
                newNumber = "01";
                break;
            case 2:
                newNumber = "02";
                break;
            case 3:
                newNumber = "03";
                break;
            case 4:
                newNumber = "04";
                break;
            case 5:
                newNumber = "05";
                break;
            case 6:
                newNumber = "06";
                break;
            case 7:
                newNumber = "07";
                break;
            case 8:
                newNumber = "08";
                break;
            case 9:
                newNumber = "09";
                break;
            default:
                newNumber = Integer.toString(number);
                break;
        }

        return newNumber;
    }

    private DateTime parseToLocalDateTime(DateTimeVM dateTimeVM) {
        return new DateTime(dateTimeVM.getYear(),
                                    dateTimeVM.getMonthOfYear(),
                                    dateTimeVM.getDayOfMonth(),
                                    dateTimeVM.getHour(),
                                    dateTimeVM.getMinute(), DateTimeZone.getDefault());
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
