package com.example.thespell;

import android.util.Log;

import java.util.ArrayList;

public class Feature_Class {
    private static final String LOG_CAT = "Feature_Class";

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    private String class_name;
    private ArrayList<Feature> features;
    private int[] got;
    private int pullNumber;

    public Feature_Class(){
        features = new ArrayList<>();
        got = new int[3];
        pullNumber = 0;
        for (int i = 0; i < got.length; i++){
            got[i] = -1;
        }
    }


    public void addFeature (String name){
        features.add(new Feature(name));
    }

    public Feature getNext(){
        int next;
        do{
            boolean alreadyGot = false;
            next = (int) (Math.random() * features.size());
            if (pullNumber > 0) for (int i = 0; i < got.length; i++){
                if (next == got[i]) alreadyGot = true;
            }
            if (alreadyGot) continue;
            got[pullNumber] = next;
            pullNumber++;
            break;
        } while(true);
        return features.get(next);
    }

}
