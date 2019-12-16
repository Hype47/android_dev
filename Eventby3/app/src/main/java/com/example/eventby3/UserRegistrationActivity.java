package com.example.eventby3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserRegistrationActivity extends AppCompatActivity implements OnMapReadyCallback {
//public class UserRegistrationActivity extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Marker newsMarker;
    LatLng coordDefault;
    LocationManager locationManager;
    LocationListener locationListener;
    private String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String apiKey = "&key=AIzaSyBLa3dlf8IME79rwvWRB5L9oxgMrCtySro";
    private double lon;
    private double lat;


    //region Read JSON File - Don't forget to give internet permission (Google GeoCoder API)
    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (FileNotFoundException f) {
                Log.i("Data error","Data not found");
                return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Working on the JSON object & extract the information
            try {
                // Getting the JSON contents
                JSONObject jsonObject = new JSONObject(s);
                String results = jsonObject.getString("results");
                JSONArray arr = new JSONArray(results);
                String geometry = arr.getJSONObject(0).getString("geometry");
                JSONObject jsonObject1 = new JSONObject(geometry);
                String location = jsonObject1.getString("location");
                JSONObject jsonObject2 = new JSONObject(location);
                lat = jsonObject2.getDouble("lat");
                lon = jsonObject2.getDouble("lng");
                Toast.makeText(getApplicationContext(),"Lat: " + lat + ", " + "Lon: " + lon, Toast.LENGTH_LONG).show();
                Log.i("Default Coordinate",lat + "," + lon);
                coordDefault = new LatLng(lat,lon);

                // News Marker
                gmap.clear();
                newsMarker = gmap.addMarker(new MarkerOptions()
                        .position(coordDefault).title("Your default location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordDefault,12));
                newsMarker.showInfoWindow();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==  1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(UserRegistrationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Cancel Button
        Button buttonCancel = findViewById(R.id.buttonSignUpCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Get Default Location
        final EditText editTextLocation = findViewById(R.id.editLocationSignUp);
        editTextLocation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    UserRegistrationActivity.DownloadTask task = new UserRegistrationActivity.DownloadTask();
                    try {
                        String address = editTextLocation.getText().toString();
                        String addressURL = address.replace(" ","+");
                        String urlBuilder = url + addressURL + apiKey;
                        task.execute(urlBuilder);
                        Log.i("Geocode API",urlBuilder);
                        mapView.getMapAsync(UserRegistrationActivity.this);
                        return true;
                    } catch (Exception e) {
                        Log.i("Endpoint error",e.toString());
                        return false;
                    }
                } else {
                return false;
            }
        }
        });
        // Part of Code to display Map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapViewDefault);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // Send data to API
        Button buttonRegister = findViewById(R.id.buttonUserSignUp);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Accessing editTexts
                EditText editName = findViewById(R.id.editTextName);
                EditText editEmail = findViewById(R.id.editTextEmail);
                EditText editPass = findViewById(R.id.editTextPassword);

                // Current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(UserRegistrationActivity.this);

                    String URL = "https://news-geocode.herokuapp.com/register";

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("screenName", editName.getText().toString());
                    jsonBody.put("username", editEmail.getText().toString());
                    jsonBody.put("password", editPass.getText().toString());
                    jsonBody.put("timestamp", currentDateTime);
                    jsonBody.put("defaultLat", lat);
                    jsonBody.put("defaultLon", lon);


                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,URL, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
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
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

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

        // Get user coordinate
        LatLng place;
        if (coordDefault == null){
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            place = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            gmap.clear();
            newsMarker = gmap.addMarker(new MarkerOptions()
                    .position(place).title("Your last known location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
            newsMarker.showInfoWindow();
        }
    }
}


