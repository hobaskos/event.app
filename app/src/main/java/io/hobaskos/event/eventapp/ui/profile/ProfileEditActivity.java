package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.http.PUT;

import static android.R.attr.bitmap;
import static android.R.attr.drawable;
import static android.R.attr.theme;

/**
 * Created by Magnus on 08.03.2017.
 */

public class ProfileEditActivity extends MvpActivity<ProfileEditView, ProfileEditPresenter> implements ProfileEditView {

    private static final String TAG = "ProfileEditActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_LIBRARY = 1;


    private TextView firstname;
    private TextView lastname;
    private TextView changeIMG;
    private TextView deleteIMG;
    private ImageView userProfilePhoto;
    private Button btnDone;
    private User user;

    @Inject
    public ProfileEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle(R.string.edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firstname = (TextView) findViewById(R.id.firstnameInput);
        lastname = (TextView) findViewById(R.id.lastnameInput);
        changeIMG = (TextView) findViewById(R.id.change_img);
        deleteIMG = (TextView) findViewById(R.id.delete_img);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        btnDone = (Button) findViewById(R.id.btn_done);

        if (!hasCamera())
            changeIMG.setEnabled(false);

        btnDone.setOnClickListener((View v) -> {
            updateProfileData();
        });

        presenter.refreshProfileData();

    }
    //Check if the device has a camera at all, front or backfaceing
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass result along to onActiviyResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void launchLibrary(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , REQUEST_IMAGE_LIBRARY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    } // end of onOptionsItemSelected()


    //If you want to return the taken image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imgDecodableString;
        RoundedBitmapDrawable roundDrawable;
        User user = new User();

        try {

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //Get the photo
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                userProfilePhoto.setImageBitmap(photo);

                roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                roundDrawable.setCircular(true);

                user.setProfileImageUrl(roundDrawable.toString());
                userProfilePhoto.setImageDrawable(roundDrawable);
            }
            // When an Image is picked
            if (requestCode == REQUEST_IMAGE_LIBRARY && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), imgDecodableString);
                roundDrawable.setCircular(true);

                user.setProfileImageUrl(roundDrawable.toString());
                userProfilePhoto.setImageDrawable(roundDrawable);


            }
        } catch (Exception e) {
            Toast.makeText(this, "Ups, dette gikk ikke bra! :(", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteIMG(View view) {
        userProfilePhoto.setImageResource(android.R.color.transparent);
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
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());

        if(user.hasProfilePicture()) {
            Picasso.with(getApplicationContext())
                    .load(user.getProfileImageUrl())
                    .transform(new CropCircleTransformation())
                    .fit()
                    .into(userProfilePhoto);
        }
    }
    @Override
    public void updateProfileData() {
        user.setFirstName(firstname.getText().toString());
        user.setLastName(lastname.getText().toString());

        presenter.updateProfile(user);
    }

    public void savedProfileData() {
        finish();
    }

}// End of class ProfileEditActivity


