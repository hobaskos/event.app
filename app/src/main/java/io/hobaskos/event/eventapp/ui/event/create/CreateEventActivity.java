package io.hobaskos.event.eventapp.ui.event.create;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.util.ImageUtil;
import io.hobaskos.event.eventapp.util.SavingProgress;
import io.hobaskos.event.eventapp.util.UrlUtil;

import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;
import static io.hobaskos.event.eventapp.util.ImageUtil.CAPTURE_IMAGE_REQUEST;

/**
 * Created by hansp on 11.03.2017.
 */

public class CreateEventActivity extends MvpActivity<CreateEventView, CreateEventPresenter>
                                    implements CreateEventView {

    @Inject
    public CreateEventPresenter presenter;

    public static final String ACTIVITY_STATE = "activity_state";

    private static final String[] CAMERA_PERMS = { Manifest.permission.CAMERA };
    private static final int CAMERA_REQUEST = 2337;

    public static final String EVENT = "event";

    private EditText title;
    private EditText description;
    private String image;
    private Spinner categories;
    private SwitchCompat privateEvent;
    private ImageView chooseImage;
    private Event event;

    private ActivityState activityState;
    private SavingProgress savingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Don't show keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setTitle(R.string.create_event);

        title = (EditText) findViewById(R.id.create_event_field_title);

        description = (EditText) findViewById(R.id.create_event_field_description);

        chooseImage = (ImageView) findViewById(R.id.create_event_button_choose_image);
        chooseImage.setOnClickListener(v -> pickNewEventImage());

        categories = (Spinner) findViewById(R.id.create_event_spinner_event_categories);
        categories.setVisibility(View.INVISIBLE);

        privateEvent = (SwitchCompat) findViewById(R.id.create_event_switch_private_event);

        presenter.attachView(this);
        presenter.loadCategories();

        setState(getIntent().getIntExtra(ACTIVITY_STATE, 0));

        if (activityState.equals(ActivityState.EDIT)) {
            setTitle(R.string.edit_event);
            event = getIntent().getParcelableExtra(EVENT);
            title.setText(event.getTitle());
            description.setText(event.getDescription());
            if (event.getImageUrl() != null && !event.getImageUrl().equals("")) {
                Picasso.with(this)
                        .load(UrlUtil.getImageUrl(event.getImageUrl()))
                        .into(chooseImage);
            }
        }
    }

    private int getIndex(EventCategory eventCategory) {
        int index = 0;
        for (int i = 0; i < categories.getCount(); i++ ) {
            EventCategory ec = (EventCategory)categories.getItemAtPosition(i);
            if (ec.getId().equals(eventCategory.getId())) return i;
        }
        return index;
    }

    private void setState(int i) {
        if(i == 0) {
            activityState = ActivityState.CREATE;
            return;
        }
        activityState = ActivityState.EDIT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_event_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                if (activityState == ActivityState.EDIT) onEditButtonClicked();
                else onCreateButtonClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onCreateButtonClicked() {
        if (!validate()) { return; }

        Event event = new Event();
        event.setTitle(title.getText().toString());
        event.setDescription(description.getText().toString());
        event.setPrivateEvent(privateEvent.isChecked());
        event.setCategory( (EventCategory) categories.getSelectedItem() );
        if (image != null) {
            event.setImage( image );
            event.setContentType("image/jpg");
        }
        presenter.post(event);
        showLoader();
    }

    private void onEditButtonClicked() {
        if (!validate()) { return; }

        event.setTitle(title.getText().toString());
        event.setDescription(description.getText().toString());
        event.setCategory( (EventCategory) categories.getSelectedItem());

        if (image != null) {
            event.setImage( image );
            event.setContentType("image/jpg");
        }

        presenter.update(event);
        showLoader();
    }

    private boolean validate() {
        boolean valid = true;

        if (title.getText().toString().isEmpty()) {
            valid = false;
            title.setError(getText(R.string.error_message_required_field_title));
        } else {
            title.setError(null);
        }

        // TODO Any other required fields?

        return valid;
    }

    public void pickNewEventImage() {
        CharSequence options[] = new CharSequence[]{
                getString(R.string.capture_image),
                getString(R.string.select_image_from_lib)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_image_option))
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: launchCamera(); break;
                        case 1: launchLibrary(); break;
                    }
                })
                .setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void launchCamera() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, R.string.could_not_find_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, CAMERA_PERMS, CAMERA_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        }
    }

    private void launchLibrary(){
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
                    chooseImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE_REQUEST) {
                //Get the photo
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
                chooseImage.setImageBitmap(bitmap);
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
        savingProgress = SavingProgress.createAndShow(this);
    }


    public void hideLoader() {
        savingProgress.dismiss();
    }

    @Override
    public void onSuccess(Event event) {
        hideLoader();

        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(EventActivity.EVENT_ID, event.getId());
        intent.putExtra(EventActivity.EVENT_THEME, event.getCategory().getTheme());

        if (activityState.equals(ActivityState.CREATE)) {
            startActivity(intent);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFailure() {
        hideLoader();

        if(activityState.equals(ActivityState.CREATE)) {
            Toast.makeText(this, R.string.could_not_create_event, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, R.string.could_not_edit_event, Toast.LENGTH_SHORT).show();
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

        if (activityState.equals(ActivityState.EDIT)) {
            categories.setSelection(getIndex(event.getEventCategory()));
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
