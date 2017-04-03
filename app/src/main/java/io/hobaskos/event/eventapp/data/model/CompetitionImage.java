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
    @SerializedName("voteScore")
    private Long voteScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean hasMyVote() {
        return hasMyVote;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getAbsoluteImageUrl() {
        return UrlUtil.getImageUrl(imageUrl);
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public Long getVoteScore() {
        return voteScore;
    }

    public void setVoteScore(Long voteScore) {
        this.voteScore = voteScore;
    }

    public String getVoteScoreAsReadable() {
        if(voteScore > 1000) {
            return voteScore / 1000 + "k";
        }

        return voteScore.toString();
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
        dest.writeString(this.imageBase64);
        dest.writeValue(this.competitionId);
        dest.writeString(this.fileContentType);
        dest.writeValue(this.voteScore);
    }

    protected CompetitionImage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.ownerLogin = in.readString();
        this.numberOfVotes = in.readInt();
        this.imageUrl = in.readString();
        this.hasMyVote = in.readByte() != 0;
        this.imageBase64 = in.readString();
        this.competitionId = (Long) in.readValue(Long.class.getClassLoader());
        this.fileContentType = in.readString();
        this.voteScore = (Long) in.readValue(Long.class.getClassLoader());
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
