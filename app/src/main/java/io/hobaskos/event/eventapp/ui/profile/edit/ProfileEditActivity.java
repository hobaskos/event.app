package io.hobaskos.event.eventapp.ui.profile.edit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.util.ImageUtil;
import io.hobaskos.event.eventapp.util.SavingProgress;
import io.hobaskos.event.eventapp.util.UrlUtil;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Magnus on 08.03.2017.
 */
public class ProfileEditActivity extends MvpActivity<ProfileEditView, ProfileEditPresenter> implements ProfileEditView {

    private static final String TAG = ProfileEditActivity.class.getName();

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_LIBRARY = 1;

    private static final String[] CAMERA_PERMS = { Manifest.permission.CAMERA };
    private static final int CAMERA_REQUEST = 3337;

    private TextView firstName;
    private TextView lastName;
    private ImageView userProfilePhoto;

    private String image;
    private User user;

    private SavingProgress savingProgress;

    @Inject
    public ProfileEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle(R.string.edit_profile);

        // Don't show keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firstName = (TextView) findViewById(R.id.firstnameInput);
        lastName = (TextView) findViewById(R.id.lastnameInput);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);

        presenter.refreshProfileData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void pickNewProfileImage(View view) {
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
               .setNegativeButton(R.string.close, (dialog, which) -> {
                   dialog.dismiss();
               })
               .show();
    }

    public void launchCamera() {
        if (!hasCamera()) {
            Toast.makeText(this, R.string.could_not_find_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, CAMERA_PERMS, CAMERA_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void launchLibrary() {
        Intent intent = new Intent();
        // Only show images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_IMAGE_LIBRARY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                updateProfileData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RoundedBitmapDrawable roundDrawable;

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_LIBRARY && data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    roundDrawable.setCircular(true);
                    userProfilePhoto.setImageDrawable(roundDrawable);

                    image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
                    user.setProfileImage(image);
                    user.setProfileImageContentType("image/png");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //Get the photo
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                userProfilePhoto.setImageBitmap(photo);

                roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                roundDrawable.setCircular(true);

                user.setProfileImageUrl(roundDrawable.toString());
                userProfilePhoto.setImageDrawable(roundDrawable);

                image = ImageUtil.getEncoded64ImageStringFromBitmap(photo);
                user.setProfileImage(image);
                user.setProfileImageContentType("image/png");
            }
        }
    }

    @NonNull
    @Override
    public ProfileEditPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public void setProfileData(User user) {
        this.user = user;
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());

        if (user.hasProfilePicture()) {
            Picasso.with(this)
                    .load(UrlUtil.getImageUrl(user.getProfileImageUrl()))
                    .transform(new CropCircleTransformation())
                    .into(userProfilePhoto);
        }
    }

    @Override
    public void updateProfileData() {
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());

        savingProgress = SavingProgress.createAndShow(this);
        presenter.updateProfile(user);
    }

    public void savedProfileData() {
        savingProgress.dismiss();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}

