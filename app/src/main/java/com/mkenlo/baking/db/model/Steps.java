package com.mkenlo.baking.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId"), indices = {@Index(value = "recipeId")})
public class Steps implements Parcelable {

    @Expose
    public int id;
    @Expose
    public String shortDescription;
    @Expose
    public String description;
    @Expose
    public String videoURL;
    @Expose
    public String thumbnailURL;

    @PrimaryKey(autoGenerate = true)
    public int stepId;
    public int recipeId;

    public Steps() {
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

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int key) {
        this.stepId = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int num) {
        this.id = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator<Steps> CREATOR
            = new Parcelable.Creator<Steps>() {
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    private Steps(Parcel in) {
        id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    @Override
    public String toString() {
        return id +".   "+ shortDescription;
    }
}
