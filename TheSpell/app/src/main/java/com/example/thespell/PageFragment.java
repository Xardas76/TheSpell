package com.example.thespell;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static androidx.core.content.ContextCompat.getColor;

public class PageFragment extends Fragment {
    private static final String LOG_CAT = "mylogsPageFragment";
    int id;

    private LinearLayout page;
    private ImageView spellImage;
    private TextView spellDesription;
    private ImageView schoolView;
    private ImageView directionView;
    private ImageView effectView;
    private TextView manaView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, container, false);
        spellDesription = view.findViewById(R.id.spellDescription);
        spellImage = view.findViewById(R.id.spell_image);
        schoolView = view.findViewById(R.id.SchoolView);
        directionView = view.findViewById(R.id.DirectionView);
        effectView = view.findViewById(R.id.EffectView);
        manaView = view.findViewById(R.id.manaView);
        page = view.findViewById(R.id.page);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt("id");
        }
        DBSpells db = new DBSpells(getActivity());
        Spell spell = db.getSpellByID(id);

        spellDesription.setText(spell.getDiscription());
        setImageFromAssets(String.valueOf(id));
        setFeatures(spell);
        setPerfectDiscriptions(spell.getPerfectDescription());

        return view;
    }

    private void setImageFromAssets (String name) {
        try {
            InputStream ims = getActivity().getAssets().open("images/" + name + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            spellImage.setImageDrawable(d);
        } catch (IOException ex) {
            Log.e(LOG_CAT, "Не удалось загрузить картинку заклинания");
        }
    }

    private void setFeatures(Spell spell) {
        String[] features = spell.getFeatures().split(" ");
        schoolView.setImageResource(getResources().getIdentifier(features[0] + "_feature", "drawable", getActivity().getPackageName()));
        directionView.setImageResource(getResources().getIdentifier(features[1] + "_feature", "drawable", getActivity().getPackageName()));
        effectView.setImageResource(getResources().getIdentifier(features[2] + "_feature", "drawable", getActivity().getPackageName()));
        manaView.setText(String.valueOf(spell.getManacost()));
    }

    private void setPerfectDiscriptions(HashMap<String, String> perfectDescription) {
        Log.d(LOG_CAT, perfectDescription.toString());
        for (String key: perfectDescription.keySet()){
            TextView textView = new TextView(getActivity());

            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.philosopher);
            textView.setTypeface(typeface);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)page.getLayoutParams();
            params.setMargins(8, 8,
                    8, 8);
            textView.setLayoutParams(params);
            textView.setText(getResources().getIdentifier(key, "string", getActivity().getPackageName()));
            textView.setText(textView.getText()+": " + perfectDescription.get(key));
            textView.setBackgroundResource(getResources().getIdentifier(key+"_key", "drawable", getActivity().getPackageName()));
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            textView.setTextColor(getColor(getActivity(), R.color.white));

            page.addView(textView);
        }
    }
}
