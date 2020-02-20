package com.example.thespell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class All_choises extends AppCompatActivity {

    private final static int choises_number = 4; //CHANGE WITH FEATURES!!!
    private static final String LOG_CAT = "All_choises";
    private String[] chosen_features;
    private TextView header;
    private Button button1;
    private Button button2;
    private Button button3;
    private int featureNumber;
    private ArrayList<Feature_Class> classes;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_choises);
        header = findViewById(R.id.Feature_class);
        button1 = findViewById(R.id.feature1);
        button2 = findViewById(R.id.feature2);
        button3 = findViewById(R.id.feature3);

        chosen_features = new String[choises_number];
        featureNumber = 0;
        XmlPullParser xpn = getResources().getXml(R.xml.features);
        FeatureFileParser ffp = new FeatureFileParser();
        ffp.parse(xpn);
        classes = ffp.getClasses();

        nextFeatureChoise(classes.get(featureNumber));
    }


    private void nextFeatureChoise(Feature_Class curClass){
        header.setText(curClass.getClass_name());
        final Feature feature1 = curClass.getNext();
        final Feature feature2 = curClass.getNext();
        final Feature feature3 = curClass.getNext();

        button1.setText(feature1.getName());
        button2.setText(feature2.getName());
        button3.setText(feature3.getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.feature1:
                        chosen_features[featureNumber] = feature1.getName();
                        featureNumber++;
                        if (featureNumber < choises_number) nextFeatureChoise(classes.get(featureNumber));
                        else showSpell();
                        break;
                    case R.id.feature2:
                        chosen_features[featureNumber] = feature2.getName();
                        featureNumber++;
                        if (featureNumber < choises_number) nextFeatureChoise(classes.get(featureNumber));
                        else showSpell();
                        break;
                    case R.id.feature3:
                        chosen_features[featureNumber] = feature3.getName();
                        featureNumber++;
                        if (featureNumber < choises_number) nextFeatureChoise(classes.get(featureNumber));
                        else showSpell();
                        break;
                        default: return;
                }
                return;
            }
        };
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
    }

    private void showSpell(){
        Intent i = new Intent();
        i.putExtra("et", chosen_features);
        setResult(RESULT_OK, i);
        finish();
    }
}
