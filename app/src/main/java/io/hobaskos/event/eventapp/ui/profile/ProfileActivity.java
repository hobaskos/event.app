package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import io.hobaskos.event.eventapp.util.UrlUtil;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProfileActivity extends BaseViewStateActivity<ProfileView, ProfilePresenter>
        implements ProfileView {

    private final static String PROFILE_FRAGMENT_TAG = "profileFragment";

    @BindView(R.id.user_profile_photo)
    protected ImageView userProfilePhoto;
    @BindView(R.id.tabsLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.container)
    protected ViewPager viewPager;

    @BindView(R.id.appbar_layout)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private ProfilePagerAdapter profilePagerAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject
    public ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        App.getInst().getComponent().inject(this);

        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        edit.setOnClickListener((View v) -> {
            Intent i = new Intent(this, ProfileEditActivity.class);
            startActivity(i);
        });
        */

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        presenter.attachView(this);
        presenter.refreshProfileData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                Intent i = new Intent(this, ProfileEditActivity.class);
                startActivity(i);
                break;
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
    public void onResume() {
        super.onResume();
        presenter.refreshProfileData();
    }

    @Override
    public void onNewViewStateInstance() {}

    @Override
    public void setProfileData(User user) {
        collapsingToolbarLayout.setTitle(user.getName());

        if (user.hasProfilePicture()) {
            Picasso.with(this)
                    .load(UrlUtil.getImageUrl(user.getProfileImageUrl()))
                    .transform(new CropCircleTransformation())
                    .into(userProfilePhoto);
        }
    }
}
