package io.hobaskos.event.eventapp.data.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by hansp on 14.03.2017.
 */

public class DateTimeVM {
    private String year;
    private String monthOfYear;
    private String dayOfMonth;
    private String hour;
    private String minute;

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        this.year = formatNumber(year);
        this.monthOfYear = formatNumber(monthOfYear);
        this.dayOfMonth = formatNumber(dayOfMonth);
    }

    public void setTime(int hour, int minute) {
        this.hour = formatNumber(hour);
        this.minute = formatNumber(minute);
    }

    public String getDate() {
        return dayOfMonth + "/" + monthOfYear + "-" + year;
    }

    public String getTime() {
        return hour + ":" + minute;
    }

    public DateTime getDateTime() {
        return new DateTime(Integer.parseInt(year),
                Integer.parseInt(monthOfYear),
                Integer.parseInt(dayOfMonth),
                Integer.parseInt(hour),
                Integer.parseInt(minute), DateTimeZone.getDefault());
    }

    private String formatNumber(int number) {

        String newNumber;

        switch (number) {
            case 0:
                newNumber = "00";
                break;
            case 1:
                newNumber = "01";
                break;
            case 2:
                newNumber = "02";
                break;
            case 3:
                newNumber = "03";
                break;
            case 4:
                newNumber = "04";
                break;
            case 5:
                newNumber = "05";
                break;
            case 6:
                newNumber = "06";
                break;
            case 7:
                newNumber = "07";
                break;
            case 8:
                newNumber = "08";
                break;
            case 9:
                newNumber = "09";
                break;
            default:
                newNumber = Integer.toString(number);
                break;
        }

        return newNumber;
    }
}
