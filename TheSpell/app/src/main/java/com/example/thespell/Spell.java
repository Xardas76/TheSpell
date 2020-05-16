package com.example.thespell;

import android.graphics.drawable.Drawable;
import java.util.HashMap;

class Spell {
    private String name;
    private String description;
    private String features;
    private HashMap<String, String> perfectDescription;
    private int manacost;
    private int points;

    Spell(){
        perfectDescription = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscription() {
        return description;
    }

    public int getManacost() {
        return manacost;
    }

    public void setManacost(int manacost) {
        this.manacost = manacost;
    }

    public HashMap<String, String> getPerfectDescription() {
        return perfectDescription;
    }

    public void addToPerfectDescription(String type, String description) {
        perfectDescription.put(type, description);
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}
