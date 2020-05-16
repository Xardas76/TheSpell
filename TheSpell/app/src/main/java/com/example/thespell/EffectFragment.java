package com.example.thespell;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;

public class EffectFragment extends Fragment {

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.effect_fragment, container, false);
        ArrayList<Feature> effects = eventListener.getFeatures(2);
        CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPager);
        cardSliderViewPager.setAdapter(new FeaturesAdapter(effects));
        cardSliderViewPager.setCurrentItem(1);

        return view;
    }

    private class FeaturesAdapter extends CardSliderAdapter<FeaturesAdapter.FeatureViewHolder> {
        private ArrayList<Feature> features;

        FeaturesAdapter(ArrayList<Feature> features){
            this.features = features;
        }

        @NonNull
        @Override
        public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_page, parent, false);
            return new FeatureViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return features.size();
        }

        @Override
        public void bindVH(FeatureViewHolder featureViewHolder, int i) {
            ((TextView)featureViewHolder.itemView.findViewById(R.id.textView)).setText(features.get(i).getPrint());
            ((ImageView)featureViewHolder.itemView.findViewById(R.id.imageView)).setImageDrawable(features.get(i).getImageFrame());
        }

        class FeatureViewHolder extends RecyclerView.ViewHolder {

            public FeatureViewHolder(View view){
                super(view);
            }
        }
    }
}
