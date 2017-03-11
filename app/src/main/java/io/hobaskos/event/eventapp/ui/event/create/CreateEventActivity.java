package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;


import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.util.ImageUtil;

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
    private Spinner themes;
    private Button chooseImage;
    private Button create;
    private RelativeLayout loadingPanel;
    //private List<EventCategory> categoryList;
    //private HashMap<String, EventCategory> categoriesSpinnerMap;

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

        themes = (Spinner) findViewById(R.id.create_event_spinner_event_themes);
        themes.setVisibility(View.INVISIBLE);

        create = (Button) findViewById(R.id.create_event_button_create);
        create.setOnClickListener(v -> onCreateButtonClicked());

        loadingPanel = (RelativeLayout) findViewById(R.id.activity_create_event_loading_panel);

        presenter.attachView(this);
        presenter.loadCategories();
        presenter.loadThemes();
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
    public void onSuccess(int id) {
        // redirect to newly created event with id= id
        hideLoader();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.could_not_create_event, Toast.LENGTH_SHORT).show();
        hideLoader();
    }

    @Override
    public void onCategoriesLoaded(List<String> categoryList) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        categoryList);

        categories.setAdapter(adapter);
        categories.setVisibility(View.VISIBLE);
    }

    @Override
    public void onThemesLoaded(List<String> themesList) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        themesList
                );

        themes.setAdapter(adapter);
        themes.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 10) {
                Uri selectedImageUri = data.getData();
                String imagePath = getRealPathFromURI(selectedImageUri);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public void onCreateButtonClicked() {
        create.setEnabled(false);

        if(!validate()) {
            create.setEnabled(true);
            return;
        }

        Event event = new Event();
        event.setTitle(title.getText().toString());
        event.setDescription(description.getText().toString());
        event.setImageUrl(image);


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
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

}
