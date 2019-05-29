package com.zaf.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cake implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("ingredients")
    private List<Ingredients> ingredients;
    @SerializedName("steps")
    private List<Steps> steps;

    public Cake(String name, int id, List<Ingredients> ingredients, List<Steps> steps) {
        this.name = name;
        this.id = id;
        this.ingredients = ingredients;
        this.steps = steps;
    }


    protected Cake(Parcel in) {
        name = in.readString();
        id = in.readInt();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<Cake> CREATOR = new Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }
}
