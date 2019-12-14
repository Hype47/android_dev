package com.example.eventby3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
//import com.google.android.gms.common.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//public class CreatePostActivity extends AppCompatActivity
public class CreatePostActivity extends AppCompatActivity implements OnMapReadyCallback {


    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location postCoord;
    private String visibility = "Neighborhood";

    // Main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Getting access to the Edit Text Fields
        final EditText editPostTitle = findViewById(R.id.editPostTitle);
        final EditText editPostContent = findViewById(R.id.editPostContent);

        // Chip Group Control --> Visibility
        final ChipGroup chipGroupVisibility = findViewById(R.id.chipGroupVisibility);
        chipGroupVisibility.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = findViewById(i);
                if (chip != null){
                    visibility = chip.getText().toString();
                } else {
                    visibility = "Neighborhood";
                    Chip chipClicked = findViewById(R.id.chipVisibility);
                    chipClicked.setChecked(true);
                }
                Log.i("Visibility Selected", visibility );
            }
        });


        // Clear Button
        Button buttonClear = findViewById(R.id.buttonPostClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPostContent.setText("");
                editPostTitle.setText("");
            }
        });

        // Cancel Button
        Button buttonCancel = findViewById(R.id.buttonPostCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Cancel Button","is Clicked");
                finish();
            }
        });

        //region Post Button
        Button buttonPostPost = findViewById(R.id.buttonPostPost);
        buttonPostPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(CreatePostActivity.this);

                    String URL = "https://news-geocode.herokuapp.com/create";

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("createdBy", "AndroidApp");
                    jsonBody.put("timestamp", currentDateTime);
                    jsonBody.put("title", editPostTitle.getText().toString() + " " + currentDateTime);
                    jsonBody.put("content", editPostContent.getText().toString());
                    jsonBody.put("lat", postCoord.getLatitude());
                    jsonBody.put("lon", postCoord.getLongitude());
                    jsonBody.put("visibility", visibility);

                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,URL, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

//                        @Override
//                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                            String responseString = "";
//                            if (response != null) {
//                                responseString = String.valueOf(response.statusCode);
//                                // can get more details such as response.headers
//                            }
//                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                        }
                    };

                    requestQueue.add(stringRequest);
                    Log.i("New Post","is Saved");
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion

        // Part of code to display the map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapViewPost);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    //region Display Map Codes
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
        final GoogleMap mMap = googleMap;

        // Codes to get user's location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Pass location to other method
                postCoord = location;

                // Clear map before adding user Marker
                mMap.clear();

                // Add user location marker
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                Marker newsmarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("You're here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                newsmarker.showInfoWindow();

                // Get location information from a set of coordinates
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {

                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);

                    // Show the location in Text
                    if (listAddresses != null && listAddresses.size() > 0){
                        TextView textPostLocation = findViewById(R.id.textPostLocation);
                        textPostLocation.setText(listAddresses.get(0).getAddressLine(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // Checking if User has granted permission to access their location --> If user denies access to location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }


        // Checking User Permission for their location
        // The short code below is for SDK 23 and above
        if (Build.VERSION.SDK_INT > 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                // Use last known location at app start
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // Map last know location
                // Clear map before adding user Marker
                mMap.clear();

                // Add user location marker
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("You're here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,12));
            }
        }
    }

}
