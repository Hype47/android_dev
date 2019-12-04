package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button buttonPrevious = findViewById(R.id.buttonBack);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);

                finish(); //to go back to previous activity
            }
        });

        // To receive intent values
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("lon", 0);
        Toast.makeText(this,lat + "," + lon ,Toast.LENGTH_SHORT).show();

        // Second button to show the values
        Button buttonShowValue = findViewById(R.id.buttonShowValue);
        buttonShowValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                double lat = intent.getDoubleExtra("lat", 0);
                double lon = intent.getDoubleExtra("lon", 0);
                Toast.makeText(getApplicationContext(),lat + "," + lon ,Toast.LENGTH_SHORT).show();


            }
        });


    }




}
