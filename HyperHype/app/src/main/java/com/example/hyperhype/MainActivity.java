package com.example.hyperhype;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.util.Arrays.asList;


public class MainActivity extends AppCompatActivity {

    // Variable declarations
    LocationManager locationManager;
    LocationListener locationListener;
    TextView locationText;
    ListView listViewNews;

    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;

    private SeekBar seekBarRadiusWidget;


    /*//region Checking if user is giving access to location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }
    //endregion*/

    //region Read JSON File - Dont forget to give internet permission
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

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("JSON",s);

            // Working on the JSON object & extract the information
            try {
                JSONObject jsonObject = new JSONObject(s);

                // Getting the JSON Object
                String newsInfo = jsonObject.getString("location");

                // Getting JSON contents
                ArrayList<String> listData = new ArrayList<>();
                JSONArray arr = new JSONArray(newsInfo);
                for (int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    //Log.i("title",jsonPart.getString("title"));
                    //Log.i("location",jsonPart.getString("lat") + "," + jsonPart.getString("lon" ));
                    String newsData = jsonPart.getString("title") + "\r\n" + jsonPart.getString("lat") + "," + jsonPart.getString("lon" ) + "\r\n" +
                            jsonPart.getString("url" );
                    Log.i("newsData",newsData);
                    listData.add(newsData);
                }
                //region News List View
                listViewNews = findViewById(R.id.listViewNews);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listData);
                listViewNews.setAdapter(arrayAdapter);
                //endregion
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Code for SeekBar
//        final SeekBar seekBarRadiusWidget;
        TextView textViewRadiusWidget = (TextView) findViewById(R.id.textViewRadius);
        seekBarRadiusWidget = findViewById(R.id.seekBarRadius);

        int progressRadius = 10;
        int maxRadius = 30;
        seekBarRadiusWidget.setMax(maxRadius);
        seekBarRadiusWidget.setProgress(progressRadius);
        textViewRadiusWidget.setText(progressRadius + " km");


        seekBarRadiusWidget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int minRadius = 1;
                int barRadius;
                if ( i < minRadius) {
                    barRadius = minRadius;
                    seekBarRadiusWidget.setProgress(minRadius);
                } else {
                    barRadius = i;
                }
                TextView textViewRadiusWidget = findViewById(R.id.textViewRadius);
                textViewRadiusWidget.setText(barRadius + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //region Demo Code for ListView
        /* Code for ListView Demo
        ListView friendsListView = findViewById(R.id.listViewNews);
        final ArrayList<String> myFriends = new ArrayList<String>(asList("Title 1","Title 2","Title 3","Title 4","Title 5","Title 6","Title 7","Title 8","Title 9","Title 10","Title 11","Title 12"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myFriends);
        friendsListView.setAdapter(arrayAdapter);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Hello " + myFriends.get(i), Toast.LENGTH_LONG).show();
            }
        });
         */
        //endregion

        /*//region Part of getting user Coordinates
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationText = findViewById(R.id.textViewLocation);
                locationText.setText("Latitude: " + location.getLatitude() + "\r\n" + "Longitude: " + location.getLongitude());
                DownloadTask task = new DownloadTask();
                int radiusKm = seekBarRadiusWidget.getProgress();
                String endPoint = "https://news-geocode.herokuapp.com/coordinate/";
                task.execute(endPoint + location.getLatitude() + "," + location.getLongitude() + "," + radiusKm);
                Log.i("EndPoint",endPoint + location.getLatitude() + "," + location.getLongitude() + "," + radiusKm);
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
        //endregion*/

        // Location
        locationText = findViewById(R.id.textViewLocation);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }


    // Todo : Move to add entry activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* --- */
    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                                    locationText.setText("Latitude: " + location.getLatitude() + "\r\n" + "Longitude: " + location.getLongitude());
                                    DownloadTask downloadTask = new DownloadTask();
                                    int radiusKm = seekBarRadiusWidget.getProgress();
                                    String endPoint = "https://news-geocode.herokuapp.com/coordinate/";
                                    downloadTask.execute(endPoint + location.getLatitude() + "," + location.getLongitude() + "," + radiusKm);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            latTextView.setText(mLastLocation.getLatitude()+"");
//            lonTextView.setText(mLastLocation.getLongitude()+"");
            locationText.setText("Latitude: " + mLastLocation.getLatitude() + "\r\n" + "Longitude: " + mLastLocation.getLongitude());
            DownloadTask downloadTask = new DownloadTask();
            int radiusKm = seekBarRadiusWidget.getProgress();
            String endPoint = "https://news-geocode.herokuapp.com/coordinate/";
            downloadTask.execute(endPoint + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "," + radiusKm);
        }
    };
}
