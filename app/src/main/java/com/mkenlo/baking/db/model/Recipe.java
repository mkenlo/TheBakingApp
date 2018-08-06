package com.mkenlo.baking.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@Entity
public class Recipe  implements Parcelable {

    @PrimaryKey
    public int    id;
    public int    servings;
    public String name;
    public String image;

    @Ignore
    public List<Ingredient>   ingredients;
    @Ignore
    public List<Steps>   steps;


    public Recipe() {
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public int getServing() {
        return servings;
    }

    public void setServing(int serving) {
        this.servings = serving;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return id+" - "+name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(servings);
        dest.writeList(steps);
        dest.writeList(ingredients);
    }

    private Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.steps = in.createTypedArrayList(Steps.CREATOR);
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) { return new Recipe(in); }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
