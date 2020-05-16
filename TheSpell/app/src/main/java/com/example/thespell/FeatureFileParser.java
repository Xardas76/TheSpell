package com.example.thespell;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class FeatureFileParser {
    private static final String LOG_TAG = "mylogs";
    private ArrayList<FeatureClass> classes;

    FeatureFileParser(){
        classes = new ArrayList<>();
    }

    public ArrayList<FeatureClass> getClasses(){
        return classes;
    }

    public void parse(XmlPullParser xpp, Context context) {
        FeatureClass current = null;

        boolean inFeatureClass = false;
        boolean inFeature = false;
        String textValue = "";

        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        } catch (XmlPullParserException ex) {
            Log.e (LOG_TAG, ex.getMessage());
        }
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("feature_class".equalsIgnoreCase(tagName)) {
                        current = new FeatureClass();
                        inFeatureClass = true;
                    } else if ("feature".equalsIgnoreCase(tagName)) {
                        inFeature = true;
                    }
                    break;
                case XmlPullParser.TEXT:
                    textValue = xpp.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (inFeatureClass) {
                        if ("feature".equalsIgnoreCase(tagName) && inFeature) {
                            current.addFeature(textValue, context);
                        } else if ("name".equalsIgnoreCase(tagName)) {
                            current.setClass_name(textValue);
                        } else if ("feature_class".equalsIgnoreCase(tagName)) {
                            inFeatureClass = false;
                            classes.add(current);
                            current = null;
                        }
                    }
                    break;
                default:
                    break;
            }
            try {
                eventType = xpp.next();
            } catch (IOException e) {
                Log.e (LOG_TAG, e.getMessage());
            } catch (XmlPullParserException e) {
                Log.e (LOG_TAG, e.getMessage());
            }
        }
    }
}
