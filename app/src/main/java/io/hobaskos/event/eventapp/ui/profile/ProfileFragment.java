package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.profile.edit.ProfileEditActivity;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by andre on 3/16/2017.
 */

public class ProfileFragment extends MvpFragment<ProfileView, ProfilePresenter> implements ProfileView, TabLayout.OnTabSelectedListener {

    public final static String TAG = ProfileFragment.class.getName();


    @BindView(R.id.user_profile_name) TextView userProfileName;
    @BindView(R.id.user_profile_photo) ImageView userProfilePhoto;
    @BindView(R.id.edit) TextView edit;
    @BindView(R.id.tabsLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ProfilePagerAdapter profilePagerAdapter;

    private Unbinder unbinder;

    @Inject
    public ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        edit.setOnClickListener((View v) -> {
            Intent i = new Intent(getContext(), ProfileEditActivity.class);
            startActivity(i);
        });

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        profilePagerAdapter = new ProfilePagerAdapter(getFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        presenter.attachView(this);

        presenter.refreshProfileData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            Picasso.with(getContext())
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i(TAG, "changing tab");
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}