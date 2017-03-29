package io.hobaskos.event.eventapp.ui.base.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.util.ColorUtil;
import io.hobaskos.event.eventapp.util.UrlUtil;
import rx.functions.Action1;

/**
 * Created by test on 3/26/2017.
 */

public class EventDateSectionPagedRecyclerAdapter extends SectionedPagedRecyclerAdapter<Event> {

    private Section<Event> sectionToday;
    private Section<Event> sectionTomorrow;
    private Section<Event> sectionThisWeek;
    private Section<Event> sectionNextWeek;

    //Months
    private Section<Event> sectionJanuary;
    private Section<Event> sectionFebruary;
    private Section<Event> sectionMarch;
    private Section<Event> sectionApril;
    private Section<Event> sectionMay;
    private Section<Event> sectionJune;
    private Section<Event> sectionJuly;
    private Section<Event> sectionAugust;
    private Section<Event> sectionSeptember;
    private Section<Event> sectionOctober;
    private Section<Event> sectionNovember;
    private Section<Event> sectionDecember;
    private Section<Event> sectionNextYear;


    public EventDateSectionPagedRecyclerAdapter(Context context, Action1<Event> onItemClick) {
        super(context, onItemClick);
    }

    private void initializeSections() {
        sectionToday = new Section<>(context.getResources().getString(R.string.today));
        sectionTomorrow = new Section<>(context.getResources().getString(R.string.tomorrow));
        sectionThisWeek = new Section<>(context.getResources().getString(R.string.this_week));
        sectionNextWeek = new Section<>(context.getResources().getString(R.string.next_week));

        //Months
        sectionJanuary = new Section<>(context.getResources().getString(R.string.january));
        sectionFebruary = new Section<>(context.getResources().getString(R.string.february));
        sectionMarch = new Section<>(context.getResources().getString(R.string.march));
        sectionApril = new Section<>(context.getResources().getString(R.string.april));
        sectionMay = new Section<>(context.getResources().getString(R.string.may));
        sectionJune = new Section<>(context.getResources().getString(R.string.june));
        sectionJuly = new Section<>(context.getResources().getString(R.string.july));
        sectionAugust = new Section<>(context.getResources().getString(R.string.august));
        sectionSeptember = new Section<>(context.getResources().getString(R.string.september));
        sectionOctober = new Section<>(context.getResources().getString(R.string.october));
        sectionNovember = new Section<>(context.getResources().getString(R.string.november));
        sectionDecember = new Section<>(context.getResources().getString(R.string.december));
        sectionNextYear = new Section<>(context.getResources().getString(R.string.next_year));
    }

    public List<Event> getItems() {
        return items;
    }

    public void setItems(List<Event> items) {
        initializeSections();
        activeSectionsList = new ArrayList<>();
        totalPositions = 0;
        addItemsToDatedSections(items);
        notifyDataSetChanged();
    }

    public void addItems(List<Event> items) {
        addItemsToDatedSections(items);
        notifyDataSetChanged();
    }


    private void insertItem(Event event, Section section, String title) {
        if (section.isEmpty()) {
            Log.i(TAG, "New section for " + title);
            // Create new section if it does not yet exist
            //section = new Section(title);
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
            // Make sure that year is current year
            if (date.getYear() == currDate.getYear()) {
                // Today:
                if (date.dayOfYear().equals(currDate.dayOfYear())) {
                    insertItem(item, sectionToday, "Today");
                    // Tomorrow:
                } else if ((date.dayOfYear().get() - currDate.dayOfYear().get()) == 1) {
                    insertItem(item, sectionTomorrow, "Tomorrow");
                    //This week:
                } else if (date.weekOfWeekyear().equals(currDate.weekOfWeekyear())) {
                    insertItem(item, sectionThisWeek, "This week");
                    //Next week:
                } else if ((date.weekOfWeekyear().get() - currDate.weekOfWeekyear().get()) == 1) {
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
            } else {
                // Next year
                insertItem(item, sectionNextYear, "Next year");
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
                ColorUtil.setCategoryColorCardView(context,eventHolder.cardView, event.getCategory().getTheme());

                //TODO: Load placeholder image if loading image fails
                Picasso.with(context)
                        .load(UrlUtil.getImageUrl(event.getImageUrl()))
                        .into(eventHolder.eventImage);


                break;
            case VIEW_TYPE_HEADER:
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                String sectionTitle = getHeaderTitle(position);
                sectionTitle = sectionTitle.toUpperCase();
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
        public CardView cardView;
        public ImageView eventImage;

        public EventViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ItemViewHolder()");
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.event_location);
            attendanceCount = (TextView) itemView.findViewById(R.id.attendance_count);
            myAttendance = (TextView) itemView.findViewById(R.id.my_attendance);
            eventDate = (TextView) itemView.findViewById(R.id.event_time);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            eventImage = (ImageView) itemView.findViewById(R.id.event_list_item_image);

        }

        public void click(final Event event, Action1<Event> listener) {
            itemView.setOnClickListener((i) -> listener.call(event));
        }
    }
}
