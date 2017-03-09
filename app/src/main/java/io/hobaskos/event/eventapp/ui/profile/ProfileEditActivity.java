package io.hobaskos.event.eventapp.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * Created by Magnus on 08.03.2017.
 */

public class ProfileEditActivity extends MvpActivity<ProfileEditView, ProfileEditPresenter> implements ProfileEditView {

    private static final String TAG = "ProfileEditActivity";


    private TextView firstname;
    private TextView lastname;
    private TextView changeIMG;
    private TextView deleteIMG;
    private ImageView userProfilePhoto;
    private Button btnDone;

    @Inject
    public ProfileEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firstname = (TextView) findViewById(R.id.firstnameInput);
        lastname = (TextView) findViewById(R.id.lastnameInput);
        changeIMG = (TextView) findViewById(R.id.change_img);
        deleteIMG = (TextView) findViewById(R.id.delete_img);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        btnDone = (Button) findViewById(R.id.btn_done);


        presenter.refreshProfileData();

    }

    @NonNull
    @Override
    public ProfileEditPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public void setProfileData(User user) {
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
    public void updateProfileData(User user) {
        btnDone.setOnClickListener((View v) -> {
            Toast.makeText(this, "Knappen virker", Toast.LENGTH_LONG).show();
            user.setFirstName("Endret");
            user.setLastName("HURRA!");
        });
    }

}// End of class ProfileEditActivity


