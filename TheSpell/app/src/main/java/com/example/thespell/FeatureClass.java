package com.example.thespell;

import android.content.Context;

import java.util.ArrayList;

public class FeatureClass {
    private static final String LOG_CAT = "Feature_Class";

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    private String class_name;
    private ArrayList<Feature> features;

    public FeatureClass(){
        features = new ArrayList<>();
    }

    public void addFeature (String name, Context context){
        features.add(new Feature(context, name));
    }

    public ArrayList<Feature> getList(){
        return features;
    }
}
