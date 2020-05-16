package com.example.thespell;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

public class SpellBookActivity extends AppCompatActivity {
    private static final String LOG_CAT = "mylogsSpellBook";
    TextView spellName;

    int[] spells_id;
    String[] spellNames;
    DBSpells db;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_spellbook);
        db = new DBSpells(getApplicationContext());

        spellName = findViewById(R.id.spellName);
        ViewPager pager = findViewById(R.id.viewPager);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(LOG_CAT, String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                spellName.setText(spellNames[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        pager.setPageTransformer(true, bookFlipPageTransformer);

        spellName.setText(spellNames[0]);
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

            SharedPreferences sPref = getSharedPreferences("info", MODE_PRIVATE);
            String[] has = sPref.getString("openedSpells","").split(" ");
            spells_id = new int[has.length];
            spellNames = new String[has.length];
            for (int i = 0; i < spells_id.length; i++) {
                spells_id[i] = Integer.valueOf(has[i]);
                spellNames[i] = db.getNameByID(spells_id[i]);
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            arguments.putInt("id", spells_id[position]);
            Fragment fragment = new PageFragment();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return spells_id.length;
        }
    }
}
