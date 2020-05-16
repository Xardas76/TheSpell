package com.example.thespell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.github.islamkhsh.CardSliderViewPager;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class CreationActivity extends FragmentActivity implements onFragmentListener {
    private static final String LOG_CAT = "mylogs";
    private Button continueButton;
    private LinearLayout mainLayout;
    private LinearLayout chosenLayout;
    private ImageView schoolView;
    private ImageView directionView;
    private ImageView effectView;
    private TextView manaView;

    private String[] chosen_features = new String[4];
    private Feature curFeature;
    private ArrayList<FeatureClass> all_classes;
    private boolean broken = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_creation);

        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDirectionFragment();
            }
        }); //START DIRECTION!
        chosenLayout = findViewById(R.id.chosen);
        mainLayout = findViewById(R.id.mainLayout);
        schoolView = findViewById(R.id.SchoolView);
        directionView = findViewById(R.id.DirectionView);
        effectView = findViewById(R.id.EffectView);
        manaView = findViewById(R.id.manaView);

        FeatureFileParser ffp = new FeatureFileParser();
        XmlPullParser xpn = getResources().getXml(R.xml.features);
        ffp.parse(xpn, getApplicationContext());
        all_classes = ffp.getClasses();

        Fragment schoolFragment = new SchoolFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentFrame, schoolFragment);
        ft.commit();
    }

    public void centralizedSchool(Feature feature){
        curFeature = feature;
        continueButton.setBackground(getDrawable(getResources().getIdentifier(feature.getName()+"_button", "drawable", getPackageName())));
        continueButton.setVisibility(View.VISIBLE);
        continueButton.setText(feature.getPrint());
    }

    public ArrayList<Feature> getFeatures (int classNumber) {
        FeatureClass featureClass = all_classes.get(classNumber);
        if (classNumber == 0) {
            ArrayList<Feature> schools = featureClass.getList();
            ArrayList<Feature> toreturn = new ArrayList<>();
            int rand2, rand3;
            int rand1 = (int) (Math.random() * 1000) % schools.size();
            do {
                rand2 = (int) (Math.random() * 1000) % schools.size();
            } while (rand2 == rand1);
            do {
                rand3 = (int) (Math.random() * 1000) % schools.size();
            } while (rand3 == rand1 || rand3 == rand2);
            toreturn.add(schools.get(rand1));
            toreturn.add(schools.get(rand2));
            toreturn.add(schools.get(rand3));
            return toreturn;
        }
        else return featureClass.getList();
    }

    private void startDirectionFragment() {
        final Fragment directionFragment = new DirectionFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, directionFragment);
        ft.commit();

        chosen_features[0] = curFeature.getName();
        continueButton.setText(R.string.next);
        continueButton.setContentDescription(curFeature.getName());
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //START EFFECT!
                String chosenDirection = ((TextView) directionFragment.getView().findViewById(R.id.cur_direction)).getText().toString();
                for (Feature f: all_classes.get(1).getList()){
                    if (f.getPrint().equals(chosenDirection)) {
                        curFeature = f;
                        break;
                    }
                }
                directionView.setImageDrawable(curFeature.getImageFrame());
                directionView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_right));
                startEffectFragment();
            }
        });
        mainLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), (R.anim.fadein)));
        mainLayout.setBackgroundResource(getResources().getIdentifier(curFeature.getName()+"_wall", "drawable", getPackageName()));
        schoolView.setImageDrawable(curFeature.getImageFrame());
        schoolView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_right));
    }

    private void startEffectFragment() {
        final Fragment effectFragment = new EffectFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, effectFragment);
        ft.commit();

        chosen_features[1] = curFeature.getName();
        continueButton.setOnClickListener(new View.OnClickListener() { //START MANA!
            @Override
            public void onClick(View v) {
                curFeature = all_classes.get(2).getList().get(((CardSliderViewPager) effectFragment.getView().findViewById(R.id.viewPager)).getCurrentItem());
                effectView.setImageDrawable(curFeature.getImageFrame());
                effectView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_right));
                startManaFragment();
            }
        });
    }

    private void startManaFragment() {
        final Fragment manaFragment = new ManaFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrame, manaFragment);
        ft.commit();

        chosen_features[2] = curFeature.getName();
        continueButton.setVisibility(View.INVISIBLE);
        //showSpell();
    }

    @Override
    public void manaFinished(int mana) {
        broken = false;
        manaView.setText(String.valueOf(mana));
        chosen_features[3] = String.valueOf(mana);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showSpell();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mainLayout.startAnimation(animation);
    }

    public void manaContinued() {
        mainLayout.clearAnimation();
        broken = true;
    }

    private void showSpell(){
        if (!broken) {
            mainLayout.setVisibility(View.INVISIBLE);
            Intent i = new Intent();
            i.putExtra("et", chosen_features);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
