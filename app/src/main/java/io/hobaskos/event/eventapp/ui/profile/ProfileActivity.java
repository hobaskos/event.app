package io.hobaskos.event.eventapp.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * Created by Magnus on 22.02.2017.
 */

public class ProfileActivity extends MvpActivity<ProfileView, ProfilePresenter> implements ProfileView {

    private static final String TAG = "ProfileActivity";

    /**
     *
     */
    private TextView userProfileName;

    /**
     *
     */
    private ImageView userProfilePhoto;

    /**
     *
     */
    @Inject
    public ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("TAB1");
        spec1.setIndicator(getText(R.string.attending));
        spec1.setContent(R.id.TAB1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("TAB2");
        spec2.setIndicator(getText(R.string.archived));
        spec2.setContent(R.id.TAB2);
        tab.addTab(spec2);

        TabHost.TabSpec spec3 = tab.newTabSpec("TAB3");
        spec3.setIndicator(getText(R.string.mine));
        spec3.setContent(R.id.TAB3);
        tab.addTab(spec3);

        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);

        presenter.refreshProfileData();
    }

    /**
     *
     * @return
     */
    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    /**
     *
     * @param user
     */
    @Override
    public void setProfileData(User user) {
        userProfileName.setText(user.getFirstName() + " " + user.getLastName());

        if(user.hasProfilePicture())
        {
            Picasso.with(getApplicationContext())
                    .load(user.getProfileImageUrl())
                    .transform(new CropCircleTransformation())
                    .fit()
                    .into(userProfilePhoto);
        }
    }
}