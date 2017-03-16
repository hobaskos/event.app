package io.hobaskos.event.eventapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static io.hobaskos.event.eventapp.R.string.create;

/**
 * Created by andre on 3/16/2017.
 */

public class ProfileFragment extends MvpFragment<ProfileView, ProfilePresenter> implements ProfileView {

    public final static String TAG = ProfileFragment.class.getName();


    @BindView(R.id.user_profile_name) TextView userProfileName;
    @BindView(R.id.user_profile_photo) ImageView userProfilePhoto;
    @BindView(R.id.edit) TextView edit;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabHost) TabHost tab;
    private DrawerLayout drawerLayout;

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


        // Configure toolbar:
        setHasOptionsMenu(true);
        toolbar.setTitle(getString(R.string.my_profile));

        // Setup Navigation Drawer
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

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

        edit.setOnClickListener((View v) -> {
            Intent i = new Intent(getContext(), ProfileEditActivity.class);
            startActivity(i);
        });

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

}