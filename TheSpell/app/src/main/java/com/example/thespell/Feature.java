package com.example.thespell;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

class Feature {
    private static final String LOG_CAT = "mylogs";

    public String getName() {
        return name;
    }

    private String name;
    private Drawable imageFrame;
    private String print;

    Feature(Context context, String name){
        this.name = name;
        try {
            imageFrame = context.getDrawable(
                    context.getResources().getIdentifier(name + "_feature", "drawable", context.getPackageName()));
        } catch (Exception e) {
            Log.e(LOG_CAT, "No Drawable Picture for feature: " + name);
        }
        try {
            print = context.getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
        } catch (Exception e) {
            Log.e(LOG_CAT, "No String for feature: " + name);
        }
    }

    public Drawable getImageFrame() {
        return imageFrame;
    }

    public String getPrint() {
        return print;
    }
}
