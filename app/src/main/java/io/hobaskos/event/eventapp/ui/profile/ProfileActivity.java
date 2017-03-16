package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
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
    private TextView edit;
    private Event event;

    List<Event> eventsAttending;

    @Inject
    public ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProfileFragmentPagerAdapter(event, this, getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMenu);
        tabLayout.setupWithViewPager(viewPager);


        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        edit = (TextView) findViewById(R.id.edit);

        edit.setOnClickListener((View v) -> {
            Intent i = new Intent(this, ProfileEditActivity.class);
            startActivity(i);
        });

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


    @Override
    public void onResume() {
        super.onResume();
        presenter.refreshProfileData();
    }

}


/*
List<String> list2 = list1.stream().collect(Collectors.toList());

List<SomeBean> newList = new ArrayList<SomeBean>(otherList);
 */