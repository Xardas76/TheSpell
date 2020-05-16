package com.example.thespell;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.animation.animpresseffect.PressEffectButton;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MyLogs";
    PressEffectButton spellBook;

    SharedPreferences sPref;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startCreation = findViewById(R.id.start);
        spellBook = findViewById(R.id.spellbook);
        checkLibraryButtonStatus();
        startCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreationActivity.class);
                startActivityForResult(i, 1);
            }
        });
        spellBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SpellBookActivity.class);
                startActivity(i);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case RESULT_OK:
                        String[] chosen_features = data.getStringArrayExtra("et");
                        Intent a = new Intent(MainActivity.this, ShowSpell.class);
                        a.putExtra("chsn", chosen_features);
                        startActivityForResult(a, 2);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (resultCode) {
                    case RESULT_OK:
                        sPref = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt("LibraryButton", 1);
                        ed.apply();
                        checkLibraryButtonStatus();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void checkLibraryButtonStatus() {
        sPref = getPreferences(MODE_PRIVATE);
        int status = sPref.getInt("LibraryButton", 0);
        if (status > 0){
            spellBook.setVisibility(View.VISIBLE);
        }
    }
}