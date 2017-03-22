package io.hobaskos.event.eventapp.ui.event.details.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Location;

import java.util.List;

public class LocationRecyclerViewAdapter extends
        RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder> {

    private final List<Location> locations;
    private final LocationsFragment.OnListFragmentInteractionListener listener;
    private final Context context;
    private final boolean isOwner;

    public LocationRecyclerViewAdapter(List<Location> locations, LocationsFragment.OnListFragmentInteractionListener listener, Context context, boolean isOwner) {
        this.locations = locations;
        this.listener = listener;
        this.context = context;
        this.isOwner = isOwner;
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
        holder.address.setText(location.getAddress());
        holder.date.setText(location.getDateLine(context));
        if(isOwner) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout locationInfoView;
        public final TextView title;
        public final TextView address;
        public final TextView date;
        public final ImageView delete;
        public Location location;

        public LocationViewHolder(View view) {
            super(view);
            locationInfoView = (LinearLayout) view.findViewById(R.id.location_info);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            date = (TextView) view.findViewById(R.id.date);
            delete = (ImageView) view.findViewById(R.id.delete);

            if(isOwner) {

                locationInfoView.setOnClickListener(v -> {
                    if (null != listener) { listener.onListFragmentEditInteraction(location); }
                });

                delete.setOnClickListener(v -> {
                    if(null != listener) { listener.onListFragmentDeleteInteraction(location); }
                });

            }
        }

    }
}
