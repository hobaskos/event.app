package io.hobaskos.event.eventapp.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseViewStateActivity;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventActivity;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;
import io.hobaskos.event.eventapp.ui.profile.ProfileActivity;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsFragment;


public class MainActivity extends BaseViewStateActivity<MainView, MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    // Views:
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    //private ViewPager viewPager;

    @Inject
    public MainPresenter presenter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        App.getInst().getComponent().inject(this);
        setContentView(R.layout.activity_main);

        if(!googleServicesAvailable()) {
            Log.i("MainActivity", "Google services is not working");
        }

        // Find views:
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        hideNavigationHeader();
        presenter.attachView(this);
        presenter.onCreateOptionsMenu();

        // Set toolbar:
        setSupportActionBar(toolbar);

        // Setup Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // Temp solution, Initial fragment:
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_pane, new EventsFragment())
                .commit();

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
     * When Navigation Drawer button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MainActivity", "onCreateOptionsMenu()");
        presenter.onCreateOptionsMenu();
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

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

    private void logout() {
        presenter.logout();
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public ViewState<MainView> createViewState() {
        return new MainViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void setNavigationHeaderText(String text) {
        View header = navigationView.getHeaderView(0);
        TextView tvNavHeaderUsername = (TextView) header.findViewById(R.id.nav_header_username);
        tvNavHeaderUsername.setText(text);
    }

    @Override
    public void viewAuthenticatedNavigation() {
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
    }

    @Override
    public void viewAnonymousNavigation() {
        navigationView.getMenu().setGroupVisible(R.id.navigational_menu_logged_in, false);
    }

    @Override
    public void hideNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        TextView tvNavHeaderUsername = (TextView) header.findViewById(R.id.nav_header_username);
//        ImageView imHeaderImage = (ImageView) header.findViewById(R.id.nav_header_profile_picture);
//        imHeaderImage.setVisibility(View.GONE);
        tvNavHeaderUsername.setText("");
    }
}