package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by hansp on 07.03.2017.
 */

public class CreateEventActivity extends MvpActivity<CreateEventView, CreateEventPresenter>
        implements CreateEventView {

    @Inject
    public CreateEventPresenter presenter;

    // The Activity's title.
    private TextView txtTitle;

    // The Events title.
    private EditText etTitle;
    private EditText etDescription;
    private EditText etImageUrl;
    private Spinner spinnerCategories;
    private Button btnSubmit;
    private Button btnAddLocation;
    private List<Location> locations;

    private AddLocationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        txtTitle = (TextView) findViewById(R.id.text_create_event_title);
        etTitle = (EditText) findViewById(R.id.field_title);
        etDescription = (EditText) findViewById(R.id.field_description);
        etImageUrl = (EditText) findViewById(R.id.field_image_url);
        spinnerCategories = (Spinner) findViewById(R.id.spinner_event_categories);

        btnSubmit = (Button) findViewById(R.id.button_create_event);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        btnAddLocation = (Button) findViewById(R.id.button_add_location);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment == null)
                {
                    fragment = new AddLocationFragment();
                }

                // view AddLocationFragment

                // handle callback -> save new location to list
                // -> show on list in this view
                // give option to add more locations
            }
        });

        presenter.attachView(this);
        presenter.getEventCategories();

    }

    private void create()
    {
        if(!validate())
        {
            onCreateFailure();
            return;
        }

        btnSubmit.setEnabled(false);

        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String imageUrl = etImageUrl.getText().toString();

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setImageUrl(imageUrl);

        presenter.create(event);
    }

    private boolean validate()
    {
        boolean valid = true;

        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if(title.isEmpty())
        {
            etTitle.setError("Title is a required field.");
            valid = false;
        } else {
            etTitle.setError(null);
        }

        if(description.isEmpty())
        {
            etDescription.setError("Description is a required field.");
            valid = false;
        } else {
            etDescription.setError(null);
        }

        return valid;
    }

    @NonNull
    @Override
    public CreateEventPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public void onCreateSuccess() {
        Toast.makeText(this, "Event is created successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onCreateFailure() {
        Toast.makeText(this, "Event is not created successfully.", Toast.LENGTH_SHORT).show();
        btnSubmit.setEnabled(true);
    }

    @Override
    public void onCategoriesLoaded(List<EventCategory> categories) {

        // Needs some refactoring
        HashSet<String> list = new HashSet<>();

        for(int i = 0; i < categories.size(); i++)
        {
            list.add(categories.get(i).getTitle());
        }

        ArrayAdapter<EventCategory> adapter = new ArrayAdapter<EventCategory>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategories.setAdapter(adapter);
    }
}
