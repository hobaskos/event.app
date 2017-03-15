package io.hobaskos.event.eventapp.ui.profile;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;


import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.event.details.EventPagerAdapter;
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

    @Inject
    public ProfilePresenter presenter;

    private ViewPager mPager;
    private TabLayout mTabs;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //setTitle(R.string.loading);
        //getSupportActionBar().setElevation(0);


        //--------NY faner START--------------------

        mPager = (ViewPager) findViewById(R.id.pagerPro);
        mTabs = (TabLayout) findViewById(R.id.tabsPro);

        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setupWithViewPager(mPager);


        //--------NY faner SLUTT--------------------

        //--------------GAMMEL Start Faner------------------

        /*
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
        */

        //--------------GAMMEL End of Faner---------------

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
    public void setData(Event event) {
        this.event = event;
        
        setTitle(event.getTitle());

        viewPager.setAdapter(new EventPagerAdapter(event, this, getSupportFragmentManager()));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.refreshProfileData();
    }

}