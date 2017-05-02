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

    private Long id;
    private String title;

    private String imageUrl;
    private Integer myVote;
    private String userLogin;
    private String userFirstName;
    private String userLastName;
    private Long voteScore;
    private int voteCount;

    @SerializedName("file")
    private String imageBase64;
    private String fileContentType;

    @SerializedName("pollId")
    private Long competitionId;


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return (title == null || title.equals("")) ? "No title" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return userFirstName + " " + userLastName;
    }

    public int getNumberOfVotes() {
        return voteCount;
    }

    public void setNumberOfVotes(int vote) {
        this.voteCount += vote;
    }

    public void setHasMyVote(int vote) {
        myVote = vote;
        voteCount += vote;
    }

    public boolean hasMyVote() {
        return myVote != null;
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
        if (voteScore > 1000) {
            return voteScore / 1000 + "k";
        }
        return voteScore.toString();
    }

    @Override
    public String toString() {
        return "CompetitionImage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", myVote=" + myVote +
                ", userLogin='" + userLogin + '\'' +
                ", voteScore=" + voteScore +
                ", voteCount=" + voteCount +
                ", imageBase64='" + imageBase64 + '\'' +
                ", fileContentType='" + fileContentType + '\'' +
                ", competitionId=" + competitionId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.myVote);
        dest.writeString(this.userLogin);
        dest.writeValue(this.voteScore);
        dest.writeInt(this.voteCount);
        dest.writeString(this.imageBase64);
        dest.writeString(this.fileContentType);
        dest.writeValue(this.competitionId);
    }

    public CompetitionImage() {
    }

    protected CompetitionImage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.myVote = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userLogin = in.readString();
        this.voteScore = (Long) in.readValue(Long.class.getClassLoader());
        this.voteCount = in.readInt();
        this.imageBase64 = in.readString();
        this.fileContentType = in.readString();
        this.competitionId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<CompetitionImage> CREATOR = new Creator<CompetitionImage>() {
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
