package io.hobaskos.event.eventapp.data.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by hansp on 14.03.2017.
 */

public class DateTimeVM {
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private int hour;
    private int minute;

    public DateTimeVM() {
    }

    public DateTimeVM(DateTime dateTime) {
        this.year = dateTime.getYear();
        this.monthOfYear = dateTime.monthOfYear().get();
        this.dayOfMonth = dateTime.getDayOfMonth();
        this.hour = dateTime.getHourOfDay();
        this.minute = dateTime.getMinuteOfHour();
    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getDate() {
        return String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear) + "-" + year;
    }

    public String getTime() {
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    public DateTime getDateTime() {
        return new DateTime(year,
                            monthOfYear,
                            dayOfMonth,
                            hour,
                            minute,
                            DateTimeZone.getDefault());
    }


}
