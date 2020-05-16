package com.example.thespell;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DirectionFragment extends Fragment {
    private static final String LOG_CAT = "mylogs";
    private ScaleGestureDetector SGD;
    private ViewFlipper viewFlipper;
    private TextView curDirection;
    private ArrayList<Feature> features;
    private ArrayList<ImageView> directions;
    private int curImage = 0;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.direction_fragment, container, false);
        SGD = new ScaleGestureDetector(getContext(), new myScaleListener());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SGD.onTouchEvent(event);
                return true;
            }
        });

        curDirection = view.findViewById(R.id.cur_direction);
        viewFlipper = view.findViewById(R.id.viewFlipper);

        features = eventListener.getFeatures(1);
        addAllImages();
        upInArray();

        return view;
    }

    private void addAllImages() {
        directions = new ArrayList<>();
        for (Feature f: features) {
            ImageView new_direction = new ImageView(getContext());
            new_direction.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            new_direction.setImageDrawable(f.getImageFrame());
            new_direction.setContentDescription(f.getName());
            viewFlipper.addView(new_direction);
            directions.add(new_direction);
        }
    }

    private class myScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float scaleFactor;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = detector.getScaleFactor();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (scaleFactor > 1){
                downInArray();
            }
            else{
                upInArray();
            }
        }

    }

    private void downInArray() {
        if (curImage > 0) {
            curDirection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipout));
            directions.get(curImage).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipout_up));
            viewFlipper.showPrevious();
            curImage--;
            directions.get(curImage).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipin_up));
            curDirection.setText(features.get(curImage).getPrint());
            curDirection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipin));
        }
    }

    private void upInArray() {
        if (curImage < directions.size() - 1) {
            curDirection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipout));
            directions.get(curImage).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipout_down));
            viewFlipper.showNext();
            curImage++;
            directions.get(curImage).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipin_down));
            curDirection.setText(features.get(curImage).getPrint());
            curDirection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flipin));
        }
    }
}
