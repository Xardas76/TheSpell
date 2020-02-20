package com.example.thespell;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

public class FindSpell {
    private static final String Disaster = "Disaster";
    private static final String DisasterD = "Oh no! You have failed everything!\nNow, nobody knows what will happen with all mana, that you unleashed...";
    private static final String LOG_CAT = "Find";

    private Spell current_spell;
    private int cur_maxpoints = 0;
    private int sum_points = 0;
    private boolean inSpell = false;
    private boolean inFeature = false;
    private boolean countNextValue = false;
    private boolean writeSpell = false;
    private String textValue = "";

    public FindSpell(){
        current_spell = new Spell();
    }

    Spell startParseandFind(String[] chosen_features, XmlPullParser xpp){
        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("spell".equalsIgnoreCase(tagName)) {
                            inSpell = true;
                        } else if ("feature".equalsIgnoreCase(tagName)) {
                            inFeature = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inSpell && inFeature) {
                            if ("name".equalsIgnoreCase(tagName) && featureInChosen(textValue, chosen_features)) {
                                countNextValue = true;
                            } else if ("value".equalsIgnoreCase(tagName) && countNextValue) {
                                sum_points += Integer.parseInt(textValue);
                                countNextValue = false;
                            } else if ("feature".equalsIgnoreCase(tagName)) {
                                inFeature = false;
                            }
                        } else if (inSpell) {
                            if ("features".equalsIgnoreCase(tagName)) {
                                if (sum_points > cur_maxpoints) {
                                    writeSpell = true;
                                    current_spell = new Spell();
                                }
                            } else if ("name".equalsIgnoreCase(tagName) && writeSpell) {
                                current_spell.setName(textValue);
                            } else if ("discription".equalsIgnoreCase(tagName) && writeSpell) {
                                current_spell.setDiscription(textValue);
                            } else if ("maxpoints".equalsIgnoreCase(tagName) && writeSpell) {
                                int maxpoints = Integer.parseInt(textValue);
                                current_spell.setMaxpoints(maxpoints);
                                cur_maxpoints = sum_points;
                                if (sum_points == maxpoints) current_spell.setPerfectCast();
                            } else if ("might".equalsIgnoreCase(tagName) && writeSpell) {
                                current_spell.setMight(Integer.parseInt(textValue));
                            } else if ("manacost".equalsIgnoreCase(tagName) && writeSpell) {
                                current_spell.setManacost(Integer.parseInt(textValue));
                            } else if ("spell".equalsIgnoreCase(tagName)) {
                                inSpell = false;
                                writeSpell = false;
                                sum_points = 0;
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (cur_maxpoints >= 100) {
            return current_spell;
        } else {
            return brokenCast();
        }
    }

    private boolean featureInChosen (String feature, String[] chosen){
        for (int i = 0; i < chosen.length; i++){
            if (feature.equalsIgnoreCase(chosen[i])) return true;
        }
        return false;
    }

    private Spell brokenCast(){
        Spell broken = new Spell();
        broken.setName(Disaster);
        broken.setDiscription(DisasterD);
        broken.setMight(-1);
        broken.setMaxpoints(-1);
        broken.setManacost(-1);
        return broken;
    }
}
