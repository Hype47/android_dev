package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    /* Function for Button Clicking.
    "void" means the function doesnt return anything
     */
    public void clickFunction(View view){

        /* assigning a variable that contains text input
        nameEditText. "R.id" means getting this id in the resources.
        "(EditText)" will convert the id into an EditText object
         */
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText passEditText = (EditText) findViewById(R.id.passEditText);

        // Writing to Log window
        Log.i("Hot Info","Button Pressed in My Awesome App");

        /* Find nameEditText variable, get the value (i.e. the Text)
        and convert it into String so it can be displayed on log window
         */
        Log.i("Username", nameEditText.getText().toString());
        Log.i("Password", passEditText.getText().toString());

        // Some kind of alert when you press the button
        Toast.makeText(this, "Hi there", Toast.LENGTH_SHORT).show();
    }

    // Function for the second button
    public void btnNameFunction (View view){

        EditText mynameEditText = (EditText) findViewById(R.id.mynameEditText);

        Toast.makeText(this, "Hello " + mynameEditText.getText().toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
