package io.hobaskos.event.eventapp.ui.events;

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

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

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

    @Override public int getItemViewType(int position) {

        if (showLoadMore && position == items.size()) { // At last position add one
            //return R.layout.list_item_loadmore;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + (showLoadMore ? 1 : 0);
    }


    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
        view.setLayoutParams(new RecyclerView.
                LayoutParams(RecyclerView.LayoutParams.
                MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {

        Event event = items.get(position);

        holder.click(event, onItemClick);
        holder.eventTitle.setText(event.getTitle());
        //holder.eventId.setText(String.valueOf(event.getId()));

        /*
        if ((position >= getItemCount() - 1)) {
            Log.i(TAG, " - bottom of list: " + position);
            onListBottom.call(position);
        }
        */
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventId;
        //ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            //eventId = (TextView) itemView.findViewById(R.id.event_id);
            //background = (ImageView) itemView.findViewById(R.id.image);
        }

        public void click(final Event event, Action1<Event> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }
    }
}
