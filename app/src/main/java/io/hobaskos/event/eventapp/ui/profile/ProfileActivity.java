package io.hobaskos.event.eventapp.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by Magnus on 22.02.2017.
 */



public class ProfileActivity extends MvpActivity<ProfileView, ProfilePresenter> implements ProfileView {

    private static final String TAG = "ProfileActivity";

    @BindView(R.id.user_profile_name)
    TextView userProfileName;

    @BindView(R.id.user_profile_photo)
    ImageView userProfilePhoto;

    @Inject
    ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("TAB1");
        spec1.setIndicator("Påmeldt");
        spec1.setContent(R.id.TAB1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("TAB2");
        spec2.setIndicator("Arkivert");
        spec2.setContent(R.id.TAB2);
        tab.addTab(spec2);

        TabHost.TabSpec spec3 = tab.newTabSpec("TAB3");
        spec3.setIndicator("Mine");
        spec3.setContent(R.id.TAB3);
        tab.addTab(spec3);

        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        ButterKnife.bind(this);

        presenter.refreshProfileData();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public void setProfileData(User user) {
        Log.i(ProfileActivity.TAG, "Name: " + user.getFirstName() + " " + user.getLastName());
        userProfileName.setText(user.getFirstName() + " " + user.getLastName());

        if(user.hasProfilePicture())
        {
            Log.i(TAG, "Fetching user profile picture.");
            Picasso.with(getApplicationContext())
                    .load(user.getProfileImageUrl())
                    .transform(new CropCircleTransformation())
                    .fit()
                    .into(userProfilePhoto);
        } else {
            Log.i(TAG, "User does not have profile picture");
            // add some generic photo
        }

    }
}