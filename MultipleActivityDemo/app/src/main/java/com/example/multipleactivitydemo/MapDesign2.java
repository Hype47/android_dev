package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/*
public class MapDesign2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_design2);
    }
}

 */

public class MapDesign2 extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap gmap;
    double lat;
    double lon;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_design2);
        final TextView textViewSnips = findViewById(R.id.textSnipsDesign2);
        textViewSnips.setVisibility(View.GONE);

        // Receive data from MainActivity
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0);
        lon = intent.getDoubleExtra("lon",0);
        String title = intent.getStringExtra("title");
        String snips = intent.getStringExtra("snips");

        // Card init
        TextView textTitle = findViewById(R.id.textViewTitle3);
        textTitle.setText(title);
        textViewSnips.setText(snips);

        /*
        // Card expanding
        final CardView cardView = findViewById(R.id.cardViewText);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                cardView.requestLayout();
                textViewSnips.setVisibility(View.VISIBLE);
            }
        });

         */

        //Toolbar tb = findViewById(R.id.toolbar);
        //setSupportActionBar(tb);
        //tb.setSubtitle("MapView");

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapViewCard);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(15);
        //LatLng place = new LatLng(40.7143528, -74.0059731);
        LatLng place = new LatLng(lat,lon);
        gmap.addMarker(new MarkerOptions().position(place).title("News Location"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }
}