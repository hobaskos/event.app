package io.hobaskos.event.eventapp.ui.profile.events.mine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsAdapter;
import rx.functions.Action1;

/**
 * Created by test on 3/16/2017.
 */

public class MyEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = MyEventsAdapter.class.getName();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Event> items;
    private Context context;
    private final Action1<Event> onItemClick;
    //private final Action1<Integer> onListBottom;

    private boolean showLoadMore = false;

    public MyEventsAdapter(List<Event> items, Context context, Action1<Event> onItemClick) {
        this.items = items;
        this.context = context;
        this.onItemClick = onItemClick;
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
                notifyDataSetChanged();
            } else {
                showLoadMore = true;
                notifyDataSetChanged();
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


    public Event getItemAtPosition(int position) {
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
                return new MyEventsAdapter.EventViewHolder(view);
            case 1: // viewType = 1 (show load more item)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loadmore, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new MyEventsAdapter.LoadMoreViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                EventsAdapter.EventViewHolder eventHolder = (EventsAdapter.EventViewHolder) holder;
                Event event = items.get(position);

                if (event.getMyAttendance() == null) eventHolder.myAttendance.setVisibility(View.GONE);

                eventHolder.click(event, onItemClick);
                eventHolder.eventTitle.setText(event.getTitle());
                eventHolder.eventLocation.setText(event.getLocation());
                eventHolder.eventDate.setText(event.getDate(context));
                eventHolder.attendanceCount.setText(String.valueOf(event.getAttendanceCount()));
                setCategoryColorView(eventHolder.categoryColor, eventHolder.categorySubColor, event.getCategory().getTheme());
                break;
            case 1:
                //LoadMoreViewHolder holder1 = (LoadMoreViewHolder) holder;
                break;
        }
    }

    private void setCategoryColorView(View mainView, View subView, EventCategoryTheme theme) {
        if (mainView == null || subView == null) return;
        switch (theme) {
            case RED:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedDark));
                break;
            case ORANGE:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeDark));
                break;
            case YELLOW:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellowDark));
                break;
            case GREEN:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenDark));
                break;
            case BLUE:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlueDark));
                break;
            case INDIGO:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigo));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigoDark));
                break;
            case VIOLET:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorViolet));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVioletDark));
                break;
        }
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle, eventLocation, eventDate, attendanceCount, myAttendance;
        public View categoryColor;
        public View categorySubColor;

        public EventViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "EventViewHolder()");
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.event_location);
            attendanceCount = (TextView) itemView.findViewById(R.id.attendance_count);
            myAttendance = (TextView) itemView.findViewById(R.id.my_attendance);
            eventDate = (TextView) itemView.findViewById(R.id.event_time);
            categoryColor = itemView.findViewById(R.id.category_theme_color);
            categorySubColor = itemView.findViewById(R.id.category_theme_color_sub);

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
