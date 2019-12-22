package com.example.eventby3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class UserProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap gmap;
    private Marker newsMarker;
    private double lat, lon;
    private LatLng coordDefault;

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
//                Toast.makeText(getApplicationContext(),"Lat: " + lat + ", " + "Lon: " + lon, Toast.LENGTH_LONG).show();
                Log.e("Default Coordinate",lat + "," + lon);
                coordDefault = new LatLng(lat,lon);

                // News Marker
                gmap.clear();
                newsMarker = gmap.addMarker(new MarkerOptions()
                        .position(coordDefault).title("Your neighborhood")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordDefault,12));
                newsMarker.showInfoWindow();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Access to Toolbar
        Toolbar toolbarUserProfile = findViewById(R.id.toolbarUserProfile);
        toolbarUserProfile.setTitle("User Profile");

        // Access to Text Fields
        final EditText editLocation = findViewById(R.id.editTextEditLocation);
        editLocation.setVisibility(View.GONE);
        final EditText editName = findViewById(R.id.editTextEditName);
        final EditText editEmail = findViewById(R.id.editTextEditEmail);
        final TextView textLocation = findViewById(R.id.textViewLocation);

        // Access to Buttons
        Button buttonLogout = findViewById(R.id.buttonLogout);
        final Button buttonEditProfile = findViewById(R.id.buttonEditProfile);
        final Button buttonSave = findViewById(R.id.buttonSaveEdit);
        buttonSave.setVisibility(View.GONE);

        // Fill in the text edit
        editName.setText(Frag3.screenName);
        editEmail.setText(UserSignInActivity.loginName);

        // EditText cannot be edited at first
        editLocation.setEnabled(false);
        editName.setEnabled(false);
        editEmail.setEnabled(false);


        // Logging out
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignInActivity.jwtToken = null;
                UserSignInActivity.loginName = null;
                Frag3.screenName = null;

                // Modify Toolbar on Main Activity
                MainActivity.toolbar.setTitle("Welcome to " + getString(R.string.app_name));
                MainActivity.toolbar.setNavigationIcon(R.drawable.ic_action_add);

                // Restart Main Activity
                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

                // System message
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_LONG).show();
            }
        });

        // Edit Profile
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonCheck = (String) buttonEditProfile.getText();
                switch (buttonCheck){
                    case "Edit":
                        editLocation.setEnabled(true);
                        textLocation.setVisibility(View.GONE);
                        editLocation.setVisibility(View.VISIBLE);
                        editName.setEnabled(true);
                        editEmail.setEnabled(true);
                        buttonSave.setVisibility(View.VISIBLE);
                        buttonEditProfile.setText("Cancel");
                        break;
                    case "Cancel":
                        finish();
                        break;
                }
            }
        });

        // Editing location
        editLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    UserProfileActivity.DownloadTask task = new UserProfileActivity.DownloadTask();
                    try {
                        String address = editLocation.getText().toString();
                        String addressURL = address.replace(" ","+");
                        String urlBuilder = UserRegistrationActivity.url + addressURL + UserRegistrationActivity.apiKey;
                        task.execute(urlBuilder);
                        Log.e("Geocode API",urlBuilder);
                        mapView.getMapAsync(UserProfileActivity.this);
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

        // Save Profile
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = editEmail.getText().toString();
                String nameText = editName.getText().toString();
                String address = editLocation.getText().toString();
                // Mandatory fields cannot be empty
                if (emailText.matches("") | nameText.matches("") | address.matches("")){
                    inputErrorDialog();
                } else {
                    saveAlertDialog();
                }
            }
        });

        // For displaying Map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapViewEditProfile);
        mapView.onCreate(mapViewBundle);

        if (!editLocation.isEnabled()){
            mapView.getMapAsync(this);
        }
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
        LatLng place,userPlace;
        userPlace = new LatLng(Frag3.userLat,Frag3.userLon);
        place = userPlace;


        // Get location information from a set of coordinates
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String newsLocation;
        try {
            List<Address> listAddresses = geocoder.getFromLocation(place.latitude,place.longitude,2);
            TextView textLocation = findViewById(R.id.textViewLocation);

            // Show the location in Text
            if (listAddresses != null && listAddresses.size() > 0){
                newsLocation = listAddresses.get(0).getAddressLine(0);
                textLocation.setText(newsLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // News Marker
        newsMarker = gmap.addMarker(new MarkerOptions()
                .position(place).title("Your Neighborhood")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(place));
        newsMarker.showInfoWindow();
    }

    // Alert Dialog on Save
    public void saveAlertDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Update Profile");
        alertDialogBuilder.setMessage("Click Yes to confirm changes")
                .setCancelable(true)
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            // Retrieving Data
                            final EditText editEmail = findViewById(R.id.editTextEditEmail);
                            final EditText editName = findViewById(R.id.editTextEditName);
                            EditText editLocation = findViewById(R.id.editTextEditLocation);
                            final String address = editLocation.getText().toString();

                            // Init Volley
                            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
                            String URL = "https://news-geocode.herokuapp.com/edituserprofile";
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("userid", Frag3.userid);
                            jsonBody.put("screenName", editName.getText().toString());
                            jsonBody.put("username", editEmail.getText().toString());
                            jsonBody.put("defaultLat", lat);
                            jsonBody.put("defaultLon", lon);
                            if (address.matches("")){
                                jsonBody.put("defaultLat", Frag3.userLat);
                                jsonBody.put("defaultLon", Frag3.userLon);
                            } else {
                                jsonBody.put("defaultLat", lat);
                                jsonBody.put("defaultLon", lon);
                            }
                            final String requestBody = jsonBody.toString();

                            // System logging
                            Log.e("Request",requestBody);
                            Log.e("Address", address);

                            // Sending Volley
                            StringRequest stringRequest = new StringRequest(Request.Method.PUT,URL, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("VOLLEY", response);

                                    // Update the global variables with new profile
                                    UserSignInActivity.loginName = editEmail.getText().toString();
                                    Frag3.screenName = editName.getText().toString();
                                    Frag3.userLat = lat;
                                    Frag3.userLon = lon;

                                    // Update MainActivity's toolbar
                                    MainActivity.toolbar.setTitle("Hi " + Frag3.screenName);

                                    // Success message
                                    saveSuccessDialog();

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("VOLLEY", "Cannot update profile " + error.toString());
                                    // Error messages
                                    saveErrorDialog();

                                    /*if (emailText.matches("") | nameText.matches("")){
                                        inputErrorDialog();
                                    } else {
                                        saveErrorDialog();
                                    }*/
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
                            };
                            requestQueue.add(stringRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Alert save success
    public void saveSuccessDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Profile updated")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
                        // Restart Main Activity
                        Context context = UserProfileActivity.this;
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Alert save error
    public void saveErrorDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Input Error");
        alertDialogBuilder.setMessage("User is not found or email has been used by other user")
                .setCancelable(false)
                .setNegativeButton("Back",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Alert incomplete entry
    public void inputErrorDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Input Error");
        alertDialogBuilder.setMessage("The fields cannot be empty")
                .setCancelable(false)
                .setNegativeButton("Back",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
