package io.hobaskos.event.eventapp.ui.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;

import io.hobaskos.event.eventapp.R;


/**
 * Created by Magnus on 22.02.2017.
 */



public class ProfileActivity extends AppCompatActivity implements ProfileView {

    @BindView(R.id.user_profile_name)
    TextView userProfileName;

    @BindView(R.id.user_profile_photo)
    ImageView userProfilePhoto;

    ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("TAB1");
        spec1.setIndicator("Påmeldt");
        spec1.setContent(R.id.TAB1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("TAB2");
        spec2.setIndicator("Arkivert");
        spec2.setContent(R.id.TAB2);
        tab.addTab(spec2);

        TabHost.TabSpec spec3 = tab.newTabSpec("TAB3");
        spec3.setIndicator("Mine");
        spec3.setContent(R.id.TAB3);
        tab.addTab(spec3);



        ButterKnife.bind(this);
        presenter = new ProfilePresenter(this);
    }
}