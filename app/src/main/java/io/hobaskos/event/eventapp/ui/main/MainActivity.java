package io.hobaskos.event.eventapp.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.eventbus.UserHasLoggedInEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.about.AboutActivity;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseViewStateActivity;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventActivity;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsFragment;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;
import io.hobaskos.event.eventapp.ui.profile.ProfileActivity;
import io.hobaskos.event.eventapp.util.UrlUtil;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import rx.Observer;

/**
 * Created by test on 3/16/2017.
 */

public class MainActivity extends BaseViewStateActivity<MainView, MainPresenter>
        implements
        NavigationView.OnNavigationItemSelectedListener,
        MainView,
        JoinPrivateEventFragment.OnInviteCodeSubmitInteractionListener {

    public final static String TAG = MainActivity.class.getName();

    // Views:
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private final static String JOIN_EVENT_KEY = "joinEventFragment";

    @Inject
    public MainPresenter presenter;

    @Inject
    public AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        App.getInst().getComponent().inject(this);
        setContentView(R.layout.activity_main_navigation);

        if (!googleServicesAvailable()) {
            Log.i("MainActivity", "Google services is not working");
        }

        // Find views:
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        hideNavigationHeader();
        presenter.attachView(this);
        presenter.onLoginState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // Set initial item in navigation view to checked
            navigationView.setCheckedItem(R.id.nav_events);
            // Initial fragment:
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_pane, new EventsFragment())
                    .commit();
        }

        if (accountManager.isLoggedIn()) {
            insertUserToNavigationDrawer(accountManager.getLocalAccount());
        }
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Kan ikke koble til Google Play Services!", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Handles navigation view item clicks
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_events:
                fragment = new EventsFragment();
                break;
            case R.id.nav_create_event:
                startActivity(new Intent(this, CreateEventActivity.class));
                break;
            case R.id.nav_join_private_event:
                joinPrivateEvent();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_logout:
                logoutDialog();
                break;

            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
        }
        // Set navdrawer item to checked
        item.setChecked(true);

        // Open new fragment
        if (fragment != null) { // Temporary fix while having empty items(links to no fragment)
            // TODO: Add fragments to a container / viewpager?
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_pane, fragment).commit();
        }

        // Close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.logout, (dialog, which) -> presenter.logout())
                .setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void joinPrivateEvent() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(JOIN_EVENT_KEY);
        if (prev != null) { ft.remove(prev); }
        JoinPrivateEventFragment.newInstance().show(ft, JOIN_EVENT_KEY);
    }

    @Override
    public ViewState<MainView> createViewState() {
        return new MainViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUserHasLoggedInEvent(UserHasLoggedInEvent userHasLoggedInEvent) {
        insertUserToNavigationDrawer(userHasLoggedInEvent.getUser());
        viewAuthenticatedNavigation();
    }

    @Override
    public void onUserHasLoggedOutEvent() {
        Log.d(TAG, "onUserHasLoggedOutEvent: user has logged out");
        setUserName("");
        setDefaultPicture();
        hideNavigationHeader();
        viewAnonymousNavigation();
        Toast.makeText(this, R.string.you_have_now_logged_out, Toast.LENGTH_SHORT).show();
    }

    private void insertUserToNavigationDrawer(User user) {
        setUserName(user.getName());
        setUserPicture(user.getProfileImageUrl());
    }

    public void setUserName(String text) {
        View header = navigationView.getHeaderView(0);
        TextView tvNavHeaderUsername = (TextView) header.findViewById(R.id.nav_header_username);
        tvNavHeaderUsername.setText(text);
    }

    public void setUserPicture(String imageUrl) {
        View header = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) header.findViewById(R.id.imageView);

        if(imageUrl == null || imageUrl.equals("")) {
            imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
            return;
        }

        Picasso.with(getApplicationContext())
                .load(UrlUtil.getImageUrl(imageUrl))
                .transform(new CropCircleTransformation())
                .into(imageView);
    }

    public void setDefaultPicture() {
        View header = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) header.findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.eventure_logo);
    }

    @Override
    public void viewAuthenticatedNavigation() {
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        navigationView.getMenu().setGroupVisible(R.id.navigational_menu_logged_in, true);
    }

    @Override
    public void viewAnonymousNavigation() {
        navigationView.getMenu().setGroupVisible(R.id.navigational_menu_logged_in, false);
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
    }

    @Override
    public void hideNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        TextView tvNavHeaderUsername = (TextView) header.findViewById(R.id.nav_header_username);
//        ImageView imHeaderImage = (ImageView) header.findViewById(R.id.nav_header_profile_picture);
//        imHeaderImage.setVisibility(View.GONE);
        tvNavHeaderUsername.setText("");
    }

    @Override
    public void onInviteCodeSubmitInteractionListener(String inviteCode) {
        presenter.getEventFromInviteCode(inviteCode, new Observer<Event>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Toast.makeText(io.hobaskos.event.eventapp.ui.main.MainActivity.this, getString(R.string.invalid_invite_code), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Event event) {
                Intent intent = new Intent(io.hobaskos.event.eventapp.ui.main.MainActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EVENT_ID, event.getId());
                intent.putExtra(EventActivity.EVENT_THEME, event.getCategory().getTheme());
                startActivity(intent);
            }
        });
    }
}
