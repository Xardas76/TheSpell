package com.example.thespell;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ManaFragment extends Fragment {
    private static final String LOG_CAT = "mylogs";
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

    private ImageView manaSphere;
    private float mana;
    private float maxMana = 31;
    private float factor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mana_fragment, container, false);
        manaSphere = view.findViewById(R.id.manaSphere);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        eventListener.manaContinued();
                        factor = 0;
                        mana = 3;
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        factor = (float) Math.random();
                        if (mana + factor*(maxMana/100) >= maxMana) break;
                        mana += factor*(maxMana/100);
                        manaSphere.setAlpha(mana/maxMana);
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        eventListener.manaFinished((int) mana);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                }
                return true;
            }
        });

        return view;
    }

}
