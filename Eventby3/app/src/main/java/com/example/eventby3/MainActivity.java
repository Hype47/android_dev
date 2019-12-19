package com.example.eventby3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Accessing toolbar
        toolbar = findViewById(R.id.toolbarMainActivity);

        // Changing Toolbar title
        String toolTitle;
        if (Frag3.screenName == null){
            toolTitle = "Welcome to " + getString(R.string.app_name);
            toolbar.setTitle(toolTitle);
        }

        // Changing toolbar Navigation icon
        if (Frag3.screenName == null){
            toolbar.setNavigationIcon(R.drawable.ic_action_user_account);
        }

        // Move to User Registration Menu
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent;
                if (Frag3.screenName == null){
                    intent = new Intent(context, UserSignInActivity.class);
                } else {
                    intent = new Intent(context, UserProfileActivity.class);
                }
                context.startActivity(intent);
            }
        });



    }




}
