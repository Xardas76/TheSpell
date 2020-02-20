package com.example.thespell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Button startCreation;
    private Button spellBook;
    private String[] chosen_features;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startCreation = findViewById(R.id.start);
        spellBook = findViewById(R.id.spellbook);
        startCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, All_choises.class);
                startActivityForResult(i, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:
            switch (resultCode) {
                case RESULT_OK:
                    chosen_features = data.getStringArrayExtra("et");
                    Intent a = new Intent(MainActivity.this, ShowSpell.class);
                    a.putExtra("chsn", chosen_features);
                    startActivityForResult(a, 2);
                    break;
                default:
                    break;
            }
            break;
            case 2:
                switch(resultCode) {
                    case RESULT_OK:
                        spellBook.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}