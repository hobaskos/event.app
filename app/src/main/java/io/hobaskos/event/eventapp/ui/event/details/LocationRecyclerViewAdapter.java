package io.hobaskos.event.eventapp.ui.event.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.event.details.LocationsFragment.OnListFragmentInteractionListener;

import java.util.List;

public class LocationRecyclerViewAdapter extends
        RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder> {

    private final List<Location> locations;
    private final OnListFragmentInteractionListener listener;

    public LocationRecyclerViewAdapter(List<Location> locations, OnListFragmentInteractionListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.location = location;
        holder.title.setText(location.getName());
        holder.date.setText(location.getFromDate().toString());

        holder.view.setOnClickListener((view) -> {
            if (null != listener) { listener.onListFragmentInteraction(holder.location); }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView date;
        public Location location;

        public LocationViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
}
