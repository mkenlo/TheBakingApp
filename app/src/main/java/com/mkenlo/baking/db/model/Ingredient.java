package com.mkenlo.baking.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId"), indices = {@Index(value = "recipeId")})
public class Ingredient implements Parcelable {

    @Expose
    public double quantity;
    @Expose
    public String measure;
    @Expose
    public String ingredient;

    @PrimaryKey(autoGenerate = true)
    public int id;

    int recipeId;

    public Ingredient() {
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public String toString() {
        return quantity + " " + measure + " " + ingredient;
    }

    public Ingredient(Parcel in){
        ingredient = in.readString();
        measure =  in.readString();
        quantity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeString(measure);
        dest.writeDouble(quantity);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR =  new Parcelable.Creator<Ingredient>(){

        public Ingredient createFromParcel(Parcel in){ return new Ingredient(in);}

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
