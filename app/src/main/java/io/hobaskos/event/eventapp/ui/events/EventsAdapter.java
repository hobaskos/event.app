package io.hobaskos.event.eventapp.ui.events;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import rx.functions.Action1;

/**
 * Created by andre on 2/14/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = EventsAdapter.class.getName();


    private List<Event> items;
    private final Action1<Event> onItemClick;
    private final Action1<Integer> onListBottom;

    private boolean showLoadMore = false;

    public EventsAdapter(List<Event> items, Action1<Event> onItemClick, Action1<Integer> onListBottom) {
        this.items = items;
        this.onItemClick = onItemClick;
        this.onListBottom = onListBottom;
    }

    public List<Event> getItems() {
        return items;
    }

    public void setItems(List<Event> events) {
        this.items = events;
    }

    public void addItems(List<Event> events) {
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
            return 1;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + (showLoadMore ? 1 : 0);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) { // viewType = 1 (show load more item)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loadmore, null);
            view.setLayoutParams(new RecyclerView.
                    LayoutParams(RecyclerView.LayoutParams.
                    MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new LoadMoreViewHolder(view);
        } else { // viewType = 0 (Event item)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
            view.setLayoutParams(new RecyclerView.
                    LayoutParams(RecyclerView.LayoutParams.
                    MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new EventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                EventViewHolder holder0 = (EventViewHolder) holder;
                Event event = items.get(position);

                holder0.click(event, onItemClick);
                holder0.eventTitle.setText(event.getTitle());
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
        TextView eventTitle, eventId;
        //ImageView background;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            //eventId = (TextView) itemView.findViewById(R.id.event_id);
            //background = (ImageView) itemView.findViewById(R.id.image);
        }

        public void click(final Event event, Action1<Event> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }
}
