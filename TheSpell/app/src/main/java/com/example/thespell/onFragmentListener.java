package com.example.thespell;

import java.util.ArrayList;

public interface onFragmentListener {
    void centralizedSchool(Feature feature);
    ArrayList<Feature> getFeatures(int classNumber);
    void manaFinished (int mana);
    void manaContinued ();
}
