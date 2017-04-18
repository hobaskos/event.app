package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseViewStateActivity;
import io.hobaskos.event.eventapp.ui.profile.edit.ProfileEditActivity;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProfileActivity extends BaseViewStateActivity<ProfileView, ProfilePresenter>
        implements ProfileView {

    private final static String PROFILE_FRAGMENT_TAG = "profileFragment";

    @BindView(R.id.user_profile_name)
    protected TextView userProfileName;
    @BindView(R.id.user_profile_photo)
    protected ImageView userProfilePhoto;
    @BindView(R.id.edit)
    protected TextView edit;
    @BindView(R.id.tabsLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.viewPager)
    protected ViewPager viewPager;

    private ProfilePagerAdapter profilePagerAdapter;

    @Inject
    public ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        App.getInst().getComponent().inject(this);

        setContentView(R.layout.fragment_profile);

        ButterKnife.bind(this);

        edit.setOnClickListener((View v) -> {
            Intent i = new Intent(this, ProfileEditActivity.class);
            startActivity(i);
        });

        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        presenter.attachView(this);
        presenter.refreshProfileData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public ViewState<ProfileView> createViewState() {
        return new ProfileViewState();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public void onNewViewStateInstance() {}

    @Override
    public void setProfileData(User user) {
        userProfileName.setText(user.getFirstName() + " " + user.getLastName());
        if (user.hasProfilePicture()) {
            Picasso.with(this)
                    .load(user.getProfileImageUrl())
                    .transform(new CropCircleTransformation())
                    .fit()
                    .into(userProfilePhoto);
        }
    }
}
