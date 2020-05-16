package com.example.thespell;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.Math.abs;

public class ShowSpell extends AppCompatActivity {
    private static final String LOG_CAT = "mylogsShowSpell";
    TextView spellName;
    TextView spellDescription;
    Button addToLibrary;
    ImageView spellImage;
    private String[] chosen_features;

    SharedPreferences sPref;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_show_spell);
        spellName = findViewById(R.id.spell_name);
        spellDescription = findViewById(R.id.spellDescription);
        addToLibrary = findViewById(R.id.addToSpellbook);
        spellImage = findViewById(R.id.spell_image);
        chosen_features = getIntent().getStringArrayExtra("chsn");
        int mana_entered = Integer.parseInt(chosen_features[3]);

        DBSpells db = new DBSpells(getApplicationContext());
        final long spellID = db.findSpellID(chosen_features);
        if (spellID <= 0) {
            if (spellID == 0) {
                spellName.setText(getResources().getString(R.string.disaster));
                spellDescription.setText(getResources().getString(R.string.disasterDescription));
                setImageFromAssets("disaster");
            } else {
                spellName.setText(getResources().getString(R.string.accident));
                spellDescription.setText(getResources().getString(R.string.accidentDescription) + " "
                        + db.getSpellByID(spellID*(-1)).getName().toLowerCase());
                setImageFromAssets("accident");
            }
            return;
        }

        Log.d(LOG_CAT, "id: "+spellID);
        Spell result = db.getSpellByID(spellID);
        int might = result.getPoints() * 50 / 120 + 50-abs(50 - mana_entered * 50 / result.getManacost());
        if (might > 97) might = 100;
        setImageFromAssets(String.valueOf(spellID));
        spellName.setText(result.getName());
        spellDescription.setText(result.getDiscription()+"\n"+getString(R.string.might)+": "+might+"%");

        if (might == 100) { //Perfect cast
            findViewById(R.id.constraintLayoutShow).setBackgroundColor(getColor(R.color.gold));
            Toast.makeText(this, "Perfect cast!", Toast.LENGTH_LONG).show();
            addToLibrary.setVisibility(View.VISIBLE);
            addToLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSpellToPreferences(String.valueOf(spellID));
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
    }

    private void setImageFromAssets (String name) {
        try {
            InputStream ims = getAssets().open("images/" + name + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            spellImage.setImageDrawable(d);
        } catch (IOException ex) {
            Log.e(LOG_CAT, "Не удалось загрузить картинку заклинания");
        }
    }

    private void addSpellToPreferences(String spell_id) {
        sPref = getSharedPreferences("info", MODE_PRIVATE);
        if (sPref.getString("openedSpells","").contains(spell_id)) return;
        SharedPreferences.Editor ed = sPref.edit();
        String has = sPref.getString("openedSpells","");
        ed.putString("openedSpells", has+(has.equals("")?"":" ")+spell_id);
        ed.apply();
    }

}
