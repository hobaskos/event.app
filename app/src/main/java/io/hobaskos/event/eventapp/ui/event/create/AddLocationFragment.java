package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by hansp on 07.03.2017.
 */

public class AddLocationFragment extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private Context context;
    private LocationListener activity;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etFromDate;
    private EditText etToDate;
    private EditText etFromTime;
    private EditText etToTime;
    private Button btnAdd;

    private DatePickerState datePickerState;
    private TimePickerState timePickerState;

    private enum DatePickerState {
        TO, FROM
    }

    private enum TimePickerState {
        TO, FROM
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_location, container, false);

        etTitle = (EditText) view.findViewById(R.id.fragment_add_location_field_title);
        etDescription = (EditText) view.findViewById(R.id.fragment_add_location_field_description);

        etFromTime = (EditText) view.findViewById(R.id.fragment_add_location_field_from_time);
        etFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerState = TimePickerState.FROM;
                showTimePickerDialog();
            }
        });

        etFromDate = (EditText) view.findViewById(R.id.fragment_add_location_field_from_date);
        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerState = DatePickerState.FROM;
                showDatePickerDialog();
            }
        });

        etToTime = (EditText) view.findViewById(R.id.fragment_add_location_field_to_time);
        etToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerState = TimePickerState.TO;
                showTimePickerDialog();
            }
        });

        etToDate = (EditText) view.findViewById(R.id.fragment_add_location_field_to_date);
        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerState = DatePickerState.TO;
                showDatePickerDialog();
            }
        });


        btnAdd = (Button) view.findViewById(R.id.fragment_add_location_button_add);
        btnAdd.setOnClickListener(v -> callback());

        return view;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            activity = (LocationListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private void callback() {
        if(validate())
        {

        }

        Location location = new Location();
        location.setName(etTitle.getText().toString());
        location.setDescription(etDescription.getText().toString());
        location.setGeoPoint(new GeoPoint(59.91, 10.45));

        activity.addLocation(location);
    }

    private boolean validate() {

        boolean valid = true;

        String name = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if(name.isEmpty())
        {
            etTitle.setError(getText(R.string.locations_name_is_required));
            valid = false;
        } else {
            etTitle.setError(null);
        }

        if(description.isEmpty())
        {
            etDescription.setError(getText(R.string.locations_description_is_required));
            valid = false;
        } else {
            etDescription.setError(null);
        }

        return valid;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        switch (timePickerState) {
            case FROM:
                etFromTime.setText(hourOfDay + ":" + minute);
                break;
            case TO:
                etToTime.setText(hourOfDay + " : " + minute);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (datePickerState) {
            case FROM:
                etFromDate.setText(dayOfMonth + " / " + monthOfYear + " - " + year);
                break;
            case TO:
                etToDate.setText(dayOfMonth + " / " + monthOfYear + " - " + year);
                break;
        }
    }

    private void showTimePickerDialog()
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(AddLocationFragment.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE), true);
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    private void showDatePickerDialog()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(AddLocationFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.show(getFragmentManager(), "DatePickerDialog");

    }
}
