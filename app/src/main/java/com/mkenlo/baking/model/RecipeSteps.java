package com.mkenlo.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeSteps implements Parcelable {

    long id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public RecipeSteps() {
    }

    public RecipeSteps(long ID, String shortDescription, String description, String videoURL, String thumbsnailURL) {
        this.id = ID;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbsnailURL;
    }

    public long getID() {
        return id;
    }

    public void setID(long ID) {
        this.id = ID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }



    public static final Parcelable.Creator<RecipeSteps> CREATOR
            = new Parcelable.Creator<RecipeSteps>() {
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    private RecipeSteps(Parcel in) {
        id = in.readLong();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();

    }
}
