package io.hobaskos.event.eventapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alex on 2/15/17.
 */

public class EventCategory implements Parcelable {

    private Long id;

    private String title;

    private String iconUrl;

    private EventCategoryTheme theme;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public EventCategoryTheme getTheme() {
        return theme;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.iconUrl);
        dest.writeInt(this.theme == null ? -1 : this.theme.ordinal());
    }

    public EventCategory() {
    }

    public EventCategory(String title, long id) {
        this.title = title;
        this.id = id;
    }

    protected EventCategory(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.iconUrl = in.readString();
        int tmpTheme = in.readInt();
        this.theme = tmpTheme == -1 ? null : EventCategoryTheme.values()[tmpTheme];
    }

    public static final Parcelable.Creator<EventCategory> CREATOR = new Parcelable.Creator<EventCategory>() {
        @Override
        public EventCategory createFromParcel(Parcel source) {
            return new EventCategory(source);
        }

        @Override
        public EventCategory[] newArray(int size) {
            return new EventCategory[size];
        }
    };

    public String toString() {
        return title;
    }
}
