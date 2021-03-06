package com.example.eventby3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/*
public class NewsLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_location);
    }
}

 */

public class NewsLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private MapView mapView;
    private GoogleMap gmap;
    double lat;
    double lon;
    double userLat;
    double userLon;
    String snips;
    String title;
    String date;
    Marker newsMarker;
    String url;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_location);

        // Receiving data from MainActivity
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0);
        lon = intent.getDoubleExtra("lon",0);
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        snips = intent.getStringExtra("snips");
        url = intent.getStringExtra("url");
        userLat = intent.getDoubleExtra("userLat",0);
        userLon = intent.getDoubleExtra("userLon",0);

        // Card init
        TextView textTitle = findViewById(R.id.textEventTitle);
        textTitle.setText(title);
        TextView textDate = findViewById(R.id.textEventDate);
        textDate.setText(date);
        TextView textSnips = findViewById(R.id.textEventSnips);
        textSnips.setText(snips);

        // Read More button --> go to url
        Button buttonReadMore = findViewById(R.id.buttonReadMore);
        buttonReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uriUrl);
                startActivity(intent);
            }
        });


        // Back to more Nearby News
        Button buttonNearbyNews = findViewById(R.id.buttonNewsNearby);
        buttonNearbyNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapViewEvent);
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
        LatLng place = new LatLng(lat,lon);
        LatLng userPlace = new LatLng(userLat,userLon);

        // Get location information from a set of coordinates
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String newsLocation = " ";
        try {
            List<Address> listAddresses = geocoder.getFromLocation(place.latitude,place.longitude,2);

            // Show the location in Text
            if (listAddresses != null && listAddresses.size() > 0){
                newsLocation = listAddresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // News Marker
        newsMarker = gmap.addMarker(new MarkerOptions()
                .position(place).title(title)
                .snippet(newsLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
        newsMarker.showInfoWindow();
        gmap.setOnInfoWindowClickListener(this);

        // User Marker
        gmap.addMarker(new MarkerOptions().position(userPlace).title("You're here"));

        /*
        // If want to zoom on all markers
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(place)
                .include(userPlace)
                .build();
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

         */
    }

    // Go to news url when infoWindows is clicked
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(newsMarker)) {
            //Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
            Uri uriUrl = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW,uriUrl);
            startActivity(intent);
        }
    }
}