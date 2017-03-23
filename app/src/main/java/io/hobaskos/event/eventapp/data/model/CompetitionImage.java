package io.hobaskos.event.eventapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionImage implements CompetitionItem, Parcelable {

    private Long id;
    private String ownerLogin;
    private int hearts;
    private String imageUrl;
    private boolean hasMyVote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public void setHasMyVote(boolean b) {
        hasMyVote = b;
        if(hasMyVote) {
            hearts++;
        } else {
            hearts--;
        }
    }

    public boolean getHasMyVote() {
        return hasMyVote;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbsoluteImageUrl() {
        //String s = "https://" + Constants.API_HOST + "/api" + imageUrl;

        // for early development only:
        return imageUrl;
    }

    @Override
    public String toString() {
        return "CompetitionImage{" +
                "ownerLogin='" + ownerLogin + '\'' +
                ", hearts=" + hearts +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.ownerLogin);
        dest.writeInt(this.hearts);
        dest.writeString(this.imageUrl);
        dest.writeByte(this.hasMyVote ? (byte) 1 : (byte) 0);
    }

    public CompetitionImage() {
    }

    protected CompetitionImage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.ownerLogin = in.readString();
        this.hearts = in.readInt();
        this.imageUrl = in.readString();
        this.hasMyVote = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CompetitionImage> CREATOR = new Parcelable.Creator<CompetitionImage>() {
        @Override
        public CompetitionImage createFromParcel(Parcel source) {
            return new CompetitionImage(source);
        }

        @Override
        public CompetitionImage[] newArray(int size) {
            return new CompetitionImage[size];
        }
    };
}
