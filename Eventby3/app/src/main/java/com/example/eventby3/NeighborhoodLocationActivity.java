package com.example.eventby3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*public class NeighborhoodLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_location);
    }
}*/


public class NeighborhoodLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private MapView mapView;
    double lat;
    double lon;
    String content,author;
    private String title;
    private Marker newsMarker;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_location);

        // Receiving data from MainActivity
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0);
        lon = intent.getDoubleExtra("lon",0);
        title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");

        // Card init
        TextView textTitle = findViewById(R.id.textNeighborhoodMapTitle);
        textTitle.setText(title);
        TextView textDate = findViewById(R.id.textNeighborhoodMapDate);
        textDate.setText(date);
        TextView textAuthor = findViewById(R.id.textNeighborhoodMapAuthor);
        textAuthor.setText(author);
        TextView textContent = findViewById(R.id.textNeighborhoodMapContent);
        textContent.setText(content);

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


        /*// Google search this event when button is clicked
        Button buttonEventSearch = findViewById(R.id.buttonGoogleSearch);
        buttonEventSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, title);
                startActivity(intent);
            }
        });*/

        /*// Back to more Nearby Events
        Button buttonNearbyNews = findViewById(R.id.buttonNewsNearby);
        buttonNearbyNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapViewNeighborhood);
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
        GoogleMap gmap = googleMap;
        gmap.setMinZoomPreference(15);
        //LatLng place = new LatLng(40.7143528, -74.0059731);
        LatLng place = new LatLng(lat,lon);
//        LatLng userPlace = new LatLng(userLat,userLon);

        // News Marker
        newsMarker = gmap.addMarker(new MarkerOptions().position(place)
//                .title(title)
//                .snippet("@" + place.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
        newsMarker.showInfoWindow();
        gmap.setOnInfoWindowClickListener(this);

        // User Marker
//        gmap.addMarker(new MarkerOptions().position(userPlace).title("You're here"));

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
        /*if (marker.equals(newsMarker)) {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, title);
            startActivity(intent);
        }*/
    }


}
