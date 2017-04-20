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
        if (isOwner) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.INVISIBLE);
        }
    }

    public void setItems(List<Location> items) {
        locations.clear();
        locations.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout contentWrapper;
        public final LinearLayout actionWrapper;
        public final TextView title;
        public final TextView address;
        public final TextView date;
        public final ImageView delete;
        public final ImageView edit;
        public Location location;

        public LocationViewHolder(View view) {
            super(view);
            contentWrapper = (LinearLayout) view.findViewById(R.id.content_wrapper);
            actionWrapper = (LinearLayout) view.findViewById(R.id.action_wrapper);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            date = (TextView) view.findViewById(R.id.date);
            delete = (ImageView) view.findViewById(R.id.delete);
            edit = (ImageView) view.findViewById(R.id.edit);

            if (listener == null) return;

            contentWrapper.setOnClickListener(v -> listener.onLocationMapInteraction(locations, location));

            if (isOwner) {
                edit.setOnClickListener(v -> listener.onLocationEditInteraction(location));
                delete.setOnClickListener(v -> listener.onLocationDeleteInteraction(location));
            } else {
                actionWrapper.setVisibility(View.GONE);
            }
        }

    }
}
