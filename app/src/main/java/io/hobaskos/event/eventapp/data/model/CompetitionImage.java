package io.hobaskos.event.eventapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.hobaskos.event.eventapp.config.Constants;
import io.hobaskos.event.eventapp.util.UrlUtil;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionImage implements CompetitionItem, Parcelable {

    @SerializedName("id")
    private Long id;
    @SerializedName("ownerLogin")
    private String ownerLogin;
    @SerializedName("voteCount")
    private int numberOfVotes;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("hasMyVote")
    private boolean hasMyVote;
    @SerializedName("file")
    private String imageBase64;
    @SerializedName("pollId")
    private Long competitionId;
    @SerializedName("fileContentType")
    private String fileContentType;

    protected CompetitionImage(Parcel in) {
        ownerLogin = in.readString();
        numberOfVotes = in.readInt();
        imageUrl = in.readString();
        hasMyVote = in.readByte() != 0;
        imageBase64 = in.readString();
        fileContentType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ownerLogin);
        dest.writeInt(numberOfVotes);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (hasMyVote ? 1 : 0));
        dest.writeString(imageBase64);
        dest.writeString(fileContentType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompetitionImage> CREATOR = new Creator<CompetitionImage>() {
        @Override
        public CompetitionImage createFromParcel(Parcel in) {
            return new CompetitionImage(in);
        }

        @Override
        public CompetitionImage[] newArray(int size) {
            return new CompetitionImage[size];
        }
    };

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

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getAbsoluteImageUrl() {
        return UrlUtil.getImageUrl(imageUrl);
    }

    public boolean isHasMyVote() {
        return hasMyVote;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    public String toString() {
        return "CompetitionImage{" +
                "ownerLogin='" + ownerLogin + '\'' +
                ", numberOfVotes=" + numberOfVotes +
                '}';
    }


    public CompetitionImage() {
    }

}
