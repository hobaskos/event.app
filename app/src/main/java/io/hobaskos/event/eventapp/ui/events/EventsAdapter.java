package io.hobaskos.event.eventapp.ui.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private List<Event> data;
    private Context context;

    public EventsAdapter(Context context, List<Event> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.context = context;
    }


    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, null);
        view.setLayoutParams(new RecyclerView.
                LayoutParams(RecyclerView.LayoutParams.
                MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {
        holder.click(data.get(position), listener);
        holder.tvCity.setText(data.get(position).getId() + "");
        holder.tvDesc.setText(data.get(position).getTitle());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnItemClickListener {
        void onClick(Event Item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, tvDesc;
        //ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.event_id);
            tvDesc = (TextView) itemView.findViewById(R.id.event_title);
            //background = (ImageView) itemView.findViewById(R.id.image);

        }


        public void click(final Event event, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(event);
                }
            });
        }
    }


}
