package com.example.thespell;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SchoolFragment extends Fragment {

    private onFragmentListener eventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            eventListener = (onFragmentListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement onEventListener");
        }
    }

    private static final String LOG_CAT = "mylogs";
    private ImageView back_circle;
    private ImageView center;
    private ConstraintLayout layout;

    private ArrayList<Feature> schools;
    private ImageView school1;
    private ImageView school2;
    private ImageView school3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school_fragment, container, false);
        layout = view.findViewById(R.id.const_layout);
        back_circle = view.findViewById(R.id.backgroung_circle);
        center = view.findViewById(R.id.circleCenter);
        View dropAim = view.findViewById(R.id.dropAim);

        center.setX(getDP(154));
        center.setY(getDP(224));
        dropAim.setX(getDP(178));
        dropAim.setY(getDP(250));
        dropAim.setOnDragListener(new MyDragListener());

        schools = eventListener.getFeatures(0);
        school1 = createSchoolFrame(schools.get(0).getName(), schools.get(0).getImageFrame());
        school2 = createSchoolFrame(schools.get(1).getName(), schools.get(1).getImageFrame());
        school3 = createSchoolFrame(schools.get(2).getName(), schools.get(2).getImageFrame());
        layout.addView(school1);
        layout.addView(school2);
        layout.addView(school3);


        back_circle.startAnimation(AnimationUtils.loadAnimation(getContext(), (R.anim.circle_start)));
        school1.animate().translationX(getDP(159)).translationY(getDP(116)).alpha(0.9f)
                .setInterpolator(new DecelerateInterpolator()).setDuration(500).setStartDelay(500);
        school2.animate().translationX(getDP(60)).translationY(getDP(286)).alpha(0.9f)
                .setInterpolator(new DecelerateInterpolator()).setDuration(500).setStartDelay(1000);
        school3.animate().translationX(getDP(258)).translationY(getDP(286)).alpha(0.9f)
                .setInterpolator(new DecelerateInterpolator()).setDuration(500).setStartDelay(1500);
        return view;
    }

    private ImageView createSchoolFrame(String schoolName, Drawable image){
        ImageView new_school = new ImageView(getContext());
        new_school.setLayoutParams(new ViewGroup.LayoutParams(getDP(83), getDP(83)));
        new_school.setX(getDP(159));
        new_school.setY(getDP(230));
        new_school.setAlpha(0f);
        new_school.setContentDescription(schoolName);
        new_school.setImageDrawable(image);
        new_school.setOnTouchListener(new MyTouchListener());
        return new_school;
    }

    private int getDP(int pixels){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    private static class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
            return false;
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView dragged = (ImageView) event.getLocalState();
            //v - Aim for drop
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                case DragEvent.ACTION_DRAG_EXITED:
                    back_circle.animate().alpha(0.8f).setDuration(500);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    back_circle.animate().alpha(1f).setDuration(500);
                    break;
                case DragEvent.ACTION_DROP:
                    dropAction(dragged.getDrawable());
                    activateCircleAnimation(dragged.getContentDescription().toString());

                    for (Feature f: schools){
                        if (f.getName().equals(dragged.getContentDescription())){
                            eventListener.centralizedSchool(f);
                            break;
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()){
                        dragged.setVisibility(View.VISIBLE);
                        back_circle.animate().alpha(0.6f).setDuration(500);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void dropAction(Drawable drawable) {
        school1.setVisibility(View.INVISIBLE);
        school2.setVisibility(View.INVISIBLE);
        school3.setVisibility(View.INVISIBLE);
        center.setImageDrawable(drawable);
        center.setVisibility(View.VISIBLE);


        center.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                school1.setVisibility(View.VISIBLE);
                school2.setVisibility(View.VISIBLE);
                school3.setVisibility(View.VISIBLE);
                center.setVisibility(View.INVISIBLE);
                back_circle.clearAnimation();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    getActivity().requireViewById(R.id.continueButton).setVisibility(View.INVISIBLE);
                }
                back_circle.setImageResource(R.drawable.back_default);
                back_circle.setAlpha((float)0.6);
                layout.startAnimation(AnimationUtils.loadAnimation(getContext(), (R.anim.fadein)));
                return true;
            }
        });
    }

    private void activateCircleAnimation(String school) {
        back_circle.setImageResource(getResources().getIdentifier(school+"_back", "drawable", getContext().getPackageName()));
        back_circle.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotation));
    }
}
