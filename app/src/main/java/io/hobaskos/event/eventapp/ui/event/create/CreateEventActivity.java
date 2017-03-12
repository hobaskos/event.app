package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;


import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.util.ImageUtil;

import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;

/**
 * Created by hansp on 11.03.2017.
 */

public class CreateEventActivity extends MvpActivity<CreateEventView, CreateEventPresenter>
                                    implements CreateEventView {

    @Inject
    public CreateEventPresenter presenter;

    private EditText title;
    private EditText description;
    private String image;
    private Spinner categories;
    private SwitchCompat privateEvent;
    private Button chooseImage;
    private Button create;
    private RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("CreateEventActivity", "Inside Create Event on Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        title = (EditText) findViewById(R.id.create_event_field_title);

        description = (EditText) findViewById(R.id.create_event_field_description);

        chooseImage = (Button) findViewById(R.id.create_event_button_choose_image);
        chooseImage.setOnClickListener(v -> openGallery());

        categories = (Spinner) findViewById(R.id.create_event_spinner_event_categories);
        categories.setVisibility(View.INVISIBLE);

        privateEvent = (SwitchCompat) findViewById(R.id.create_event_switch_private_event);

        create = (Button) findViewById(R.id.create_event_button_create);
        create.setOnClickListener(v -> onCreateButtonClicked());

        loadingPanel = (RelativeLayout) findViewById(R.id.activity_create_event_loading_panel);

        presenter.attachView(this);
        presenter.loadCategories();
    }

    private void onCreateButtonClicked() {
        create.setEnabled(false);

        if(!validate()) {
            create.setEnabled(true);
            return;
        }

        Event event = new Event();
        event.setTitle(title.getText().toString());
        event.setDescription(description.getText().toString());
        event.setPrivateEvent(privateEvent.isChecked());
        event.setCategory( (EventCategory) categories.getSelectedItem() );
        if(image != null) {
            event.setImage(image);
        }
        presenter.post(event);
        showLoader();
    }

    private boolean validate() {
        boolean valid = true;

        if(title.getText().toString().isEmpty()) {
            valid = false;
            title.setError(getText(R.string.error_message_required_field_title));
        } else {
            title.setError(null);
        }

        // Should Description be a required field?
        // Any other required fields?

        return valid;
    }

    private void openGallery(){
        Intent intent = new Intent();
        // Only show images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    @Override
    public CreateEventPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    public void showLoader() {
        loadingPanel.setVisibility(View.VISIBLE);
    }


    public void hideLoader() {
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(long id) {
        // redirect to newly created event with id= id
        hideLoader();
        Log.i("Activity", "event with id=" + id + ", created.");

        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("eventId", id);
        startActivity(intent);
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.could_not_create_event, Toast.LENGTH_SHORT).show();
        hideLoader();
    }

    @Override
    public void onFailureLoadingCategories() {
        Toast.makeText(this, R.string.could_not_load_categories, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoriesLoaded(List<EventCategory> categoryList) {
        ArrayAdapter<EventCategory> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        categoryList);

        categories.setAdapter(adapter);
        categories.setVisibility(View.VISIBLE);
    }

}
