package com.example.thespell;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class FeatureFileParser {
    private static final String LOG_TAG = "Parser";
    private ArrayList<Feature_Class> classes;

    FeatureFileParser(){
        classes = new ArrayList<>();
    }

    public ArrayList<Feature_Class> getClasses(){
        return classes;
    }

    public void parse(XmlPullParser xpp) {
        int i = 0;
        Feature_Class current = null;


        try {
            boolean inFeatureClass = false;
            boolean inFeature = false;
            String textValue = null;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("feature_class".equalsIgnoreCase(tagName)) {
                            current = new Feature_Class();
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
                                current.addFeature(textValue);
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
                eventType = xpp.next();
            }
        } catch (Exception e) {

            Log.wtf(LOG_TAG, e.getMessage());
        }
    }
}
