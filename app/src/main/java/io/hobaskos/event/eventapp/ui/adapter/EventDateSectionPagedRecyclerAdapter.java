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
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.util.ColorUtil;
import rx.functions.Action1;

/**
 * Created by test on 3/26/2017.
 */

public class EventDateSectionPagedRecyclerAdapter extends SectionedPagedRecyclerAdapter<Event> {

    Section<Event> sectionToday;
    Section<Event> sectionTomorrow;
    Section<Event> sectionThisWeek;
    Section<Event> sectionNextWeek;

    //Months
    Section<Event> sectionJanuary;
    Section<Event> sectionFebruary;
    Section<Event> sectionMarch;
    Section<Event> sectionApril;
    Section<Event> sectionMay;
    Section<Event> sectionJune;
    Section<Event> sectionJuly;
    Section<Event> sectionAugust;
    Section<Event> sectionSeptember;
    Section<Event> sectionOctober;
    Section<Event> sectionNovember;
    Section<Event> sectionDecember;


    public EventDateSectionPagedRecyclerAdapter(Context context, Action1<Event> onItemClick) {
        super(context, onItemClick);
    }

    public List<Event> getItems() {
        return items;
    }

    public void setItems(List<Event> items) {
        this.items = items;
    }

    public void addItems(List<Event> items) {
        addItemsToDatedSections(items);
        notifyDataSetChanged();
    }


    private void insertItem(Event event, Section section, String title) {
        if (section == null) {
            // Create new section if it does not yet exist
            section = new Section(title);
            // Set relative position of section to current totalPositions
            section.setRelativePosition(totalPositions);
            // Add to section list
            activeSectionsList.add(section);
            // Update total position count to include header:
            totalPositions += 1;
        }
        // Add item/event
        section.addItem(event);
        // Update total position count to include item:
        totalPositions += 1;
    }

    private void addItemsToDatedSections(List<Event> items) {
        Log.d(TAG, "addItemsToDatedSections");
        DateTime currDate = DateTime.now();
        for (Event item : items) {
            DateTime date = item.getFromDate();
            //Log.d(TAG, "getMonthOfYear: " + date.getMonthOfYear());
            // Today:
            if (date.dayOfYear().equals(currDate.dayOfYear())) {
                insertItem(item, sectionToday, "Today");
                // Tomorrow:
            } else if ((Days.daysBetween(currDate, date).getDays() == 1)) {
                insertItem(item, sectionTomorrow, "Tomorrow");
                //This week:
            } else if (date.weekOfWeekyear().equals(currDate.weekOfWeekyear())) {
                insertItem(item, sectionThisWeek, "This week");
                //Next week:
            } else if ((Weeks.weeksBetween(currDate, date).getWeeks() == 1)) {
                insertItem(item, sectionNextWeek, "Next week");
                // January:
            } else if (date.getMonthOfYear() == 1) {
                insertItem(item, sectionJanuary, "January");
                //February:
            } else if (date.getMonthOfYear() == 2) {
                insertItem(item, sectionFebruary, "February");
                //March:
            } else if (date.getMonthOfYear() == 3) {
                insertItem(item, sectionMarch, "March");
                //April:
            } else if (date.getMonthOfYear() == 4) {
                insertItem(item, sectionApril, "April");
                //May:
            } else if (date.getMonthOfYear() == 5) {
                insertItem(item, sectionMay, "May");
                //June:
            } else if (date.getMonthOfYear() == 6) {
                insertItem(item, sectionJune, "June");
                //July:
            } else if (date.getMonthOfYear() == 7) {
                insertItem(item, sectionJuly, "July");
                //August:
            } else if (date.getMonthOfYear() == 8) {
                insertItem(item, sectionAugust, "August");
                //September:
            } else if (date.getMonthOfYear() == 9) {
                insertItem(item, sectionSeptember, "September");
                //October:
            } else if (date.getMonthOfYear() == 10) {
                insertItem(item, sectionOctober, "October");
                //November:
            } else if (date.getMonthOfYear() == 11) {
                insertItem(item, sectionNovember, "November");
                //December:
            } else if (date.getMonthOfYear() == 12) {
                insertItem(item, sectionDecember, "December");
            }
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                EventViewHolder eventHolder = (EventViewHolder) holder;
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
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                String sectionTitle = getHeaderTitle(position);
                sectionViewHolder.sectionTitle.setText(sectionTitle);
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


    // NOT CURRENTLY IN USE
    private boolean insertItemIfEligible(Event event, Section section, String title, Predicate<Event> tester) {
        if (tester.test(event)) {
            if (section == null) {
                section = new Section(title);
                activeSectionsList.add(section);
                // Update total position count to include header/section:
                totalPositions += 1;
            }
            // Add item/event
            section.addItem(event);
            // Update total position count to include item:
            totalPositions += 1;
            return true;
        }
        return false;
    }

    // NOT CURRENTLY IN USE
    private void addItemsToDatedSections2(List<Event> items) {
        DateTime currDate = DateTime.now();
        for (Event item : items) {
            // Today:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionTomorrow, "Tomorrow", (Event e) -> Days.daysBetween(currDate, e.getFromDate()).getDays() == 1);
            //This week::
            insertItemIfEligible(item, sectionThisWeek, "This week", (Event e) -> e.getFromDate().weekOfWeekyear().equals(currDate.weekOfWeekyear()));
            // TODO
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));// Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));// Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));
            // Tomorrow:
            insertItemIfEligible(item, sectionToday, "Today", (Event e) -> e.getFromDate().dayOfYear().equals(currDate.dayOfYear()));


//            } else if (date.weekOfWeekyear().equals(currDate.weekOfWeekyear())) {
//                sectionThisWeek.addItem(item);
//                //Next week:
//            } else if ((Weeks.weeksBetween(currDate, date).getWeeks() == 1)) {
//                sectionNextWeek.addItem(item);
//                // January:
//            } else if (date.getMonthOfYear() == 1) {
//                sectionJanuary.addItem(item);
//                //February:
//            } else if (date.getMonthOfYear() == 2) {
//                sectionFebruary.addItem(item);
//                //March:
//            } else if (date.getMonthOfYear() == 3) {
//                sectionMarch.addItem(item);
//                //April:
//            } else if (date.getMonthOfYear() == 4) {
//                sectionApril.addItem(item);
//                //May:
//            } else if (date.getMonthOfYear() == 5) {
//                sectionMay.addItem(item);
//                //June:
//            } else if (date.getMonthOfYear() == 6) {
//                sectionJune.addItem(item);
//                //July:
//            } else if (date.getMonthOfYear() == 7) {
//                sectionJuly.addItem(item);
//                //August:
//            } else if (date.getMonthOfYear() == 8) {
//                sectionAugust.addItem(item);
//                //September:
//            } else if (date.getMonthOfYear() == 9) {
//                sectionSeptember.addItem(item);
//                //October:
//            } else if (date.getMonthOfYear() == 10) {
//                sectionOctober.addItem(item);
//                //November:
//            } else if (date.getMonthOfYear() == 11) {
//                sectionNovember.addItem(item);
//                //December:
//            } else if (date.getMonthOfYear() == 12) {
//                sectionDecember.addItem(item);
//            }

        }
    }
}
