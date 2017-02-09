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
 * Created by andre on 1/26/2017.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    public final static String TAG = EventsAdapter.class.getName();

    private List<Event> data;
    private final Action1<Event> onItemClick;
    private final Action1<Integer> onListBottom;

    public EventsAdapter(List<Event> data, Action1<Event> onItemClick, Action1<Integer> onListBottom) {
        this.data = data;
        this.onItemClick = onItemClick;
        this.onListBottom = onListBottom;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
        view.setLayoutParams(new RecyclerView.
                LayoutParams(RecyclerView.LayoutParams.
                MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {

        Event event = data.get(position);

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

    @Override
    public int getItemCount() {
        return data.size();
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
