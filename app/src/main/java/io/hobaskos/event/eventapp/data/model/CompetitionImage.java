package io.hobaskos.event.eventapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionImage implements CompetitionItem, Parcelable {

    private Long id;
    private String ownerLogin;
    private int numberOfVotes;
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

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void setHasMyVote(boolean b) {
        hasMyVote = b;
        if(hasMyVote) {
            numberOfVotes++;
        } else {
            numberOfVotes--;
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
                ", numberOfVotes=" + numberOfVotes +
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
        dest.writeInt(this.numberOfVotes);
        dest.writeString(this.imageUrl);
        dest.writeByte(this.hasMyVote ? (byte) 1 : (byte) 0);
    }

    public CompetitionImage() {
    }

    protected CompetitionImage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.ownerLogin = in.readString();
        this.numberOfVotes = in.readInt();
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
