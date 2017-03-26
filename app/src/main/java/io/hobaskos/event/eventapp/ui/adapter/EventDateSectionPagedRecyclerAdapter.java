package io.hobaskos.event.eventapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.util.ColorUtil;
import rx.functions.Action1;

/**
 * Created by test on 3/26/2017.
 */

public class EventDateSectionPagedRecyclerAdapter extends SectionedPagedRecyclerAdapter<Event> {

    public EventDateSectionPagedRecyclerAdapter(Context context, Action1<Event> onItemClick) {
        super(context, onItemClick);
    }

    private ArrayList<Section<Event>> getSortedInDateSections(List<Event> items) {

        DateTime currDate = DateTime.now();

        Section<Event> sectionToday = new Section<Event>("Today");
        Section<Event> sectionTomorrow = new Section<Event>("Tomorrow");
        Section<Event> sectionThisWeek = new Section<Event>("This week");
        Section<Event> sectionNextWeek = new Section<Event>("Next week");

        //Months
        Section<Event> sectionJanuary = new Section<Event>("January");
        Section<Event> sectionFebruary = new Section<Event>("February");
        Section<Event> sectionMarch = new Section<Event>("March");
        Section<Event> sectionApril = new Section<Event>("April");
        Section<Event> sectionMay = new Section<Event>("May");
        Section<Event> sectionJune = new Section<Event>("June");
        Section<Event> sectionJuly = new Section<Event>("July");
        Section<Event> sectionAugust = new Section<Event>("August");
        Section<Event> sectionSeptember = new Section<Event>("September");
        Section<Event> sectionOctober = new Section<Event>("October");
        Section<Event> sectionNovember = new Section<Event>("November");
        Section<Event> sectionDecember = new Section<Event>("December");

        for (Event item : items) {
            DateTime date = item.getFromDate();
            // Today:
            if (date.dayOfYear().equals(currDate.dayOfYear())) {
                sectionToday.addItem(item);
                // Tomorrow:
            } else if ((Days.daysBetween(currDate, date).getDays() == 1)) {
                sectionTomorrow.addItem(item);
                //This week:
            } else if (date.weekOfWeekyear().equals(currDate.weekOfWeekyear())) {
                sectionThisWeek.addItem(item);
                //Next week:
            } else if ((Weeks.weeksBetween(currDate, date).getWeeks() == 1)) {
                sectionNextWeek.addItem(item);
                // January:
            } else if (date.getMonthOfYear() == 1) {
                sectionJanuary.addItem(item);
                //February:
            } else if (date.getMonthOfYear() == 2) {
                sectionFebruary.addItem(item);
                //March:
            } else if (date.getMonthOfYear() == 3) {
                sectionMarch.addItem(item);
                //April:
            } else if (date.getMonthOfYear() == 4) {
                sectionApril.addItem(item);
                //May:
            } else if (date.getMonthOfYear() == 5) {
                sectionMay.addItem(item);
                //June:
            } else if (date.getMonthOfYear() == 6) {
                sectionJune.addItem(item);
                //July:
            } else if (date.getMonthOfYear() == 7) {
                sectionJuly.addItem(item);
                //August:
            } else if (date.getMonthOfYear() == 8) {
                sectionAugust.addItem(item);
                //September:
            } else if (date.getMonthOfYear() == 9) {
                sectionSeptember.addItem(item);
                //October:
            } else if (date.getMonthOfYear() == 10) {
                sectionOctober.addItem(item);
                //November:
            } else if (date.getMonthOfYear() == 11) {
                sectionNovember.addItem(item);
                //December:
            } else if (date.getMonthOfYear() == 12) {
                sectionDecember.addItem(item);
            }
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                EventPagedRecyclerAdapter.EventViewHolder eventHolder = (EventPagedRecyclerAdapter.EventViewHolder) holder;
                Event event = getItemAtPosition(position);

                if (event.getMyAttendance() == null) eventHolder.myAttendance.setVisibility(View.GONE);

                eventHolder.click(event, onItemClick);
                eventHolder.eventTitle.setText(event.getTitle());
                eventHolder.eventLocation.setText(event.getLocation());
                eventHolder.eventDate.setText(event.getDate(context));
                eventHolder.attendanceCount.setText(String.valueOf(event.getAttendanceCount()));
                ColorUtil.setCategoryColorView(context,eventHolder.categoryColor, eventHolder.categorySubColor, event.getCategory().getTheme());
                break;
            case VIEW_TYPE_HEADER:

            case VIEW_TYPE_LOADING:
                //LoadMoreViewHolder holder1 = (LoadMoreViewHolder) holder;
                break;
        }
    }

    @Override
    protected EventDateSectionPagedRecyclerAdapter.ItemViewHolder newItemViewHolder(View view) {
        return new EventDateSectionPagedRecyclerAdapter.EventViewHolder(view);
    }

    public class EventViewHolder extends SectionedPagedRecyclerAdapter.ItemViewHolder {
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
            categoryColor = itemView.findViewById(R.id.category_theme_color);
            categorySubColor = itemView.findViewById(R.id.category_theme_color_sub);

            //eventId = (TextView) itemView.findViewById(R.id.event_id);
            //background = (ImageView) itemView.findViewById(R.id.image);
        }

        public void click(final Event event, Action1<Event> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }
    }
}
