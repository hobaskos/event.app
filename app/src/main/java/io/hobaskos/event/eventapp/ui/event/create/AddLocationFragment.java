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
import android.widget.Toast;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by hansp on 07.03.2017.
 */

public class AddLocationFragment extends Fragment {

    private LocationListener activity;
    private EditText etTitle;
    private EditText etDescription;
    private Button btnAdd;

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
        btnAdd = (Button) view.findViewById(R.id.fragment_add_location_button_add);
        btnAdd.setOnClickListener(v -> callback());

        return view;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            Location location = new Location();
            location.setName(etTitle.getText().toString());
            location.setDescription(etDescription.getText().toString());
            location.setGeoPoint(new GeoPoint(59.91, 10.45));

            activity.addLocation(location);
        }
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

}
