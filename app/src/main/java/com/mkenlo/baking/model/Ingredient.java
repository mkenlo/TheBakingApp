package com.mkenlo.baking.model;


import android.os.Parcel;
import android.os.Parcelable;


public class Ingredient implements Parcelable {

    double quantity;
    String measure;
    String ingredient;

    public Ingredient() {
    }

    public Ingredient(double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
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
