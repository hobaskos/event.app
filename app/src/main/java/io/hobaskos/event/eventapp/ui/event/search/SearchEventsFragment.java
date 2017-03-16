package io.hobaskos.event.eventapp.ui.event.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseFragment;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsFragment;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsFragment;

/**
 * Created by test on 3/10/2017.
 */

public class SearchEventsFragment extends BaseFragment {
    public final static String TAG = SearchEventsFragment.class.getName();

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search_events;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        showToast("One");
                        break;
                    case 1:
                        showToast("Two");

                        break;
                    case 2:
                        showToast("Three");

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new EventsFragment(), "LIST");
        adapter.addFragment(new EventsFragment(), "MAP");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "onLoginState()");
        inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                // TODO: Create searchNearby fragment/activity
                return true;
            case R.id.action_filter:
                FilterEventsFragment fragment = new FilterEventsFragment();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                //android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);

                ft.replace(R.id.main_pane, fragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}