package com.example.eventby3;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Frag2 extends Fragment {

    View rootView;
    Button button;
    CardView cardView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        // Declaring the respective layout
        rootView = inflater.inflate(R.layout.frag2_layout, container, false);

        cardView = rootView.findViewById(R.id.cardView);
        button = rootView.findViewById(R.id.buttonExpand);
        textView = rootView.findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (textView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    //imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    button.setText("Collapse");
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    //imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    button.setText("Expand");
                }
            }
        });


        return rootView;
    }
}
