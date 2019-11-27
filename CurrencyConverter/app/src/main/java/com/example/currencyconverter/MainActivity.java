package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public void convertFunction (View view){

        Log.i("Info","Convert button pressed");

        EditText amount = (EditText) findViewById(R.id.editTextAmount);
        double pound = Double.valueOf(amount.getText().toString());
        String pound_str = String.valueOf(pound);
        double convert = pound * 1.3;
        String convert_str = String.valueOf(convert);

        Toast.makeText(this, "GBP"+ pound_str +" is USD"+ convert_str, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
