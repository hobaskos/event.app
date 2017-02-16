package io.hobaskos.event.eventapp.ui.events;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import rx.functions.Action1;

/**
 * Created by andre on 2/14/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = EventsAdapter.class.getName();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    private List<EventsPresentationModel> items;
    private final Action1<EventsPresentationModel> onItemClick;
    //private final Action1<Integer> onListBottom;

    private boolean showLoadMore = false;

    public EventsAdapter(List<EventsPresentationModel> items, Action1<EventsPresentationModel> onItemClick) {
        this.items = items;
        this.onItemClick = onItemClick;
        //this.onListBottom = onListBottom;
    }

    public List<EventsPresentationModel> getItems() {
        return items;
    }

    public void setItems(List<EventsPresentationModel> events) {
        this.items = events;
    }

    public void addItems(List<EventsPresentationModel> events) {
        int startPosition = items.size();
        items.addAll(events);
        notifyItemRangeInserted(startPosition, events.size());
    }

    public void setLoadMore(boolean enabled) {

        if (showLoadMore != enabled) {
            if (showLoadMore) {
                showLoadMore = false;
                Handler handler = new Handler();
                final Runnable r = () -> notifyItemRemoved(items.size());
                //notifyItemRemoved(items.size()); // Remove last position
                handler.post(r);
            } else {
                showLoadMore = true;
                Handler handler = new Handler();
                final Runnable r = () -> notifyItemInserted(items.size());
                handler.post(r);
                //notifyItemInserted(items.size());
            }
        }
    }

    @Override public int getItemViewType(int position) {

        if (showLoadMore && position == items.size()) { // At last position add load more item
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + (showLoadMore ? 1 : 0);
    }


    public EventsPresentationModel getItemAtPosition(int position) {
        return items.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0: // viewType = 0 (Event item)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                EventViewHolder holder = new EventViewHolder(view);
                return new EventViewHolder(view);
            case 1: // viewType = 1 (show load more item)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loadmore, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new LoadMoreViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                EventViewHolder eventHolder = (EventViewHolder) holder;
                EventsPresentationModel event = items.get(position);

                eventHolder.click(event, onItemClick);
                eventHolder.eventTitle.setText(event.getTitle());
                eventHolder.eventLocation.setText(event.getLocation());

                eventHolder.eventDate.setText(event.getDate());
                break;
            case 1:
                //LoadMoreViewHolder holder1 = (LoadMoreViewHolder) holder;
                break;
        }
        /*
        if ((position >= getItemCount() - 1)) {
            onListBottom.call(position);
        }
        */
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventLocation, eventDate;
        //ImageView background;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.event_location);
            eventDate = (TextView) itemView.findViewById(R.id.event_time);

            //eventId = (TextView) itemView.findViewById(R.id.event_id);
            //background = (ImageView) itemView.findViewById(R.id.image);
        }

        public void click(final EventsPresentationModel event, Action1<EventsPresentationModel> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }


    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }
}
