package com.example.catswitcherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void btnSwitchFunction (View view){

        Log.i("btnSwitch pressed", "btnSwitchFunction activated");

        ImageView img = (ImageView) findViewById(R.id.imageViewCat);
        img.setImageResource(R.drawable.cat2);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
