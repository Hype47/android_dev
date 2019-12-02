package ru.embersoft.expandablecardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout expandableView, constraintLayout;
    Button arrowBtn, button;
    CardView cardView;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //expandableView = findViewById(R.id.expandableView);
        arrowBtn = findViewById(R.id.arrowBtn);
        cardView = findViewById(R.id.cardView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.buttonExpand);
        textView = findViewById(R.id.textView);


        /*
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

         */

        arrowBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //cardView.setLayoutParams(new ConstraintLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,1000));
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                imageView.setVisibility(View.VISIBLE);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //if (imageView.getVisibility()==View.GONE){
                textView.setVisibility(View.VISIBLE);
                /*
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

                 */

            }
        });
    }
}
