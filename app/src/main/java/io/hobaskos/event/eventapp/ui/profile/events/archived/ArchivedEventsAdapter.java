package io.hobaskos.event.eventapp.ui.profile.events.archived;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.adapter.PagedRecyclerAdapter;
import rx.functions.Action1;

/**
 * Created by test on 3/19/2017.
 */

public class ArchivedEventsAdapter extends PagedRecyclerAdapter<Event> {

    public ArchivedEventsAdapter(List<Event> items, Context context, Action1<Event> onItemClick) {
        super(items, context, onItemClick);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ArchivedEventsAdapter.EventViewHolder eventHolder = (ArchivedEventsAdapter.EventViewHolder) holder;
                Event event = getItemAtPosition(position);

                if (event.getMyAttendance() == null) eventHolder.myAttendance.setVisibility(View.GONE);

                eventHolder.click(event, onItemClick);
                eventHolder.eventTitle.setText(event.getTitle());
                eventHolder.eventLocation.setText(event.getLocation());
                eventHolder.eventDate.setText(event.getDate(context));
                eventHolder.attendanceCount.setText(String.valueOf(event.getAttendanceCount()));
                //ColorUtil.setCategoryColorView(context,eventHolder.categoryColor, eventHolder.categorySubColor, event.getCategory().getTheme());
                break;
            case 1:
                //LoadMoreViewHolder holder1 = (LoadMoreViewHolder) holder;
                break;
        }
    }

    @Override
    protected ItemViewHolder newItemViewHolder(View view) {
        return new ArchivedEventsAdapter.EventViewHolder(view);
    }

    public class EventViewHolder extends PagedRecyclerAdapter.ItemViewHolder {
        public TextView eventTitle, eventLocation, eventDate, attendanceCount, myAttendance;
        public View categoryColor;
        public View categorySubColor;

        public EventViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ItemViewHolder()");
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.event_location);
            attendanceCount = (TextView) itemView.findViewById(R.id.attendance_count);
            myAttendance = (TextView) itemView.findViewById(R.id.my_attendance);
            eventDate = (TextView) itemView.findViewById(R.id.event_time);
            //categoryColor = itemView.findViewById(R.id.category_theme_color);
            //categorySubColor = itemView.findViewById(R.id.category_theme_color_sub);

            //eventId = (TextView) itemView.findViewById(R.id.event_id);
            //background = (ImageView) itemView.findViewById(R.id.image);
        }

        public void click(final Event event, Action1<Event> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }
    }
}