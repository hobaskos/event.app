package io.hobaskos.event.eventapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.PersistentStorage;
import io.hobaskos.event.eventapp.data.service.JwtTokenProxy;
import io.hobaskos.event.eventapp.ui.events.EventsFragment;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Views:
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    //private ViewPager viewPager;

    @Inject
    public JwtTokenProxy jwtTokenProxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInst().getComponent().inject(this);

        setContentView(R.layout.activity_main);

        // Find views:
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //viewPager = (ViewPager) findViewById(R.id.view_pager);

        if(isLoggedIn())
        {
           navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        }
        else {
            navigationView.getMenu().setGroupVisible(R.id.navigational_menu_logged_in, false);
        }

        // Set toolbar:
        setSupportActionBar(toolbar);

        // Setup Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        
        // Temp solution, Initial fragment:
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame, new EventsFragment()).commit();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handles navigation view item clicks
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
                break;
            case R.id.nav_friends:
                break;
            case R.id.nav_profile:
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

    private boolean isLoggedIn()
    {
        return jwtTokenProxy.isSet();

    }

    private void logout()
    {
        jwtTokenProxy.remove();
        startActivity(new Intent(this, MainActivity.class));
    }
}
