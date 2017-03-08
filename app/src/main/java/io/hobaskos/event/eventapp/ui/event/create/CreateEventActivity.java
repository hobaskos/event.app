package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
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
        implements CreateEventView, LocationListener {

    @Inject
    public CreateEventPresenter presenter;

    // The Events title.
    private EditText etTitle;
    private EditText etDescription;
    private EditText etImageUrl;
    private Spinner spinnerCategories;
    private Button btnSubmit;
    private ArrayAdapter<Location> locationsListAdapter;
    private ListView lwLocations;
    private List<Location> locations;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etTitle = (EditText) findViewById(R.id.field_title);
        etDescription = (EditText) findViewById(R.id.field_description);
        etImageUrl = (EditText) findViewById(R.id.field_image_url);
        spinnerCategories = (Spinner) findViewById(R.id.spinner_event_categories);
        loader = (RelativeLayout) findViewById(R.id.loadingPanel);
        loader.setVisibility(View.GONE);

        lwLocations = (ListView) findViewById(R.id.create_event_location_list);
        locations = new ArrayList<>();

        btnSubmit = (Button) findViewById(R.id.button_create_event);
        btnSubmit.setOnClickListener(v -> create());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.create_event_add_location, new AddLocationFragment())
                .commit();

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
        event.setLocations(locations);

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
        hideLoader();
        Toast.makeText(this, "Event is created successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onCreateFailure() {
        hideLoader();
        Toast.makeText(this, "Event is not created successfully.", Toast.LENGTH_SHORT).show();
        btnSubmit.setEnabled(true);
    }

    @Override
    public void showLoader() {
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCategoriesLoaded(List<EventCategory> categories) {

        // Needs some refactoring
        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < categories.size(); i++)
        {
            list.add(categories.get(i).getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinnerCategories.setAdapter(adapter);
    }


    @Override
    public void onLocationListUpdated(Location location)
    {
        if(locationsListAdapter == null)
        {
            ArrayList<Location> list = new ArrayList<>();
            list.add(location);

            locationsListAdapter = new ArrayAdapter<>(this,
                    R.layout.list_item_location_list_item, list);
            lwLocations.setAdapter(locationsListAdapter);

        } else {
            locationsListAdapter.add(location);
        }
    }

    @Override
    public void addLocation(Location location) {
        //this.locations.add(location);
        onLocationListUpdated(location);

        Toast.makeText(this, "Location is added", Toast.LENGTH_SHORT).show();
    }
}
