package com.zaf.bakingapp.models;

import com.google.gson.annotations.SerializedName;

public class Cake {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public Cake(String name, int id) {
        this.name = name;
        this.id = id;
    }

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
}
