package com.example.thespell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

public class ShowSpell extends AppCompatActivity {
    private static final String LOG_CAT = "Show";
    TextView spellName;
    TextView spellDiscription;
    Button addToLibrary;
    private String[] chosen_features;

    private static final String manacostD = "\nManacost: ";
    private static final String mightD = "\nMight: ";
    private static final String efficientD = "\nEfficient: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spell);
        spellName = findViewById(R.id.spell_name);
        spellDiscription = findViewById(R.id.spell_discription);
        addToLibrary = findViewById(R.id.add_to_library);
        chosen_features = getIntent().getStringArrayExtra("chsn");

        XmlPullParser xpp = getResources().getXml(R.xml.spells);
        FindSpell findSpell = new FindSpell();
        final Spell result = findSpell.startParseandFind(chosen_features, xpp);

        if (result.castisPerfect()){
            Toast toast = Toast.makeText(this, "Perfect cast!", Toast.LENGTH_LONG);
            toast.show();
            addToLibrary.setVisibility(View.VISIBLE);
            addToLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //WriteToLibrary
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
        spellName.setText(result.getName());
        spellDiscription.setText(result.getDiscription() + (result.getName().equalsIgnoreCase("Disaster")?"":(manacostD + result.getManacost() +
                mightD + result.getMight() + efficientD + result.getMaxpoints())));
    }


}
