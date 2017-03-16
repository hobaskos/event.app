package io.hobaskos.event.eventapp.ui.event.create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.util.ImageUtil;

import static android.app.Activity.RESULT_OK;
import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;

/**
 * Created by test on 3/16/2017.
 */

public class CreateEventFragment extends MvpFragment<CreateEventView, CreateEventPresenter>
        implements CreateEventView {

    public final static String TAG = CreateEventFragment.class.getName();

    @Inject
    public CreateEventPresenter presenter;

    @BindView(R.id.create_event_field_title) EditText title;
    @BindView(R.id.create_event_field_description) EditText description;
    @BindView(R.id.create_event_spinner_event_categories) Spinner categories;
    @BindView(R.id.create_event_switch_private_event) SwitchCompat privateEvent;
    @BindView(R.id.create_event_button_choose_image) Button chooseImage;
    @BindView(R.id.create_event_button_create) Button create;
    @BindView(R.id.activity_create_event_loading_panel) RelativeLayout loadingPanel;

    @BindView(R.id.toolbar) Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private String image;

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        create.setOnClickListener(v -> onCreateButtonClicked());
        chooseImage.setOnClickListener(v -> openGallery());
        categories.setVisibility(View.INVISIBLE);

        // Configure toolbar:
        setHasOptionsMenu(true);
        toolbar.setTitle(getString(R.string.create_event));

        // Setup Navigation Drawer
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        presenter.attachView(this);
        presenter.loadCategories();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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
        Log.i(TAG, "event with id=" + id + ", created.");

        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra("eventId", id);
        startActivity(intent);
    }

    @Override
    public void onFailure() {
        Toast.makeText(getContext(), R.string.could_not_create_event, Toast.LENGTH_SHORT).show();
        hideLoader();
    }

    @Override
    public void onFailureLoadingCategories() {
        Toast.makeText(getContext(), R.string.could_not_load_categories, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoriesLoaded(List<EventCategory> categoryList) {
        ArrayAdapter<EventCategory> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        categoryList);

        categories.setAdapter(adapter);
        categories.setVisibility(View.VISIBLE);
    }

}