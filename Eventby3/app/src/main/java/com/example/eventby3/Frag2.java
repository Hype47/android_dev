package com.example.eventby3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
    Fragment for Event Tab
 */

public class Frag2 extends Fragment {

    private View rootView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String endPoint = "https://news-geocode.herokuapp.com/promo_coord/";
    private int radiusKm = 10;
    private Location coord;
    //Button button;
    //CardView cardView;
    //TextView textView;

    //region Read JSON File - Don't forget to give internet permission
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
//                Snackbar.make(getView(), "No events found for this location...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Working on the JSON object & extract the information
            try {
                JSONObject jsonObject = new JSONObject(s);

                // Getting the JSON contents
                String newsInfo = jsonObject.getString("promo_location");
                JSONArray arr = new JSONArray(newsInfo);

                // 1. get a reference to recyclerView
                RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewPromo);

                // 2. set layoutManger
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // 3. create an adapter
                //MyPromoAdapter mAdapter = new MyPromoAdapter(getActivity(),arr);
                MyPromoAdapter mAdapter = new MyPromoAdapter(getActivity(), arr, Frag2.this);

                /*
                ArrayList<String> arr2 = new ArrayList<>();
                arr2.add("Mark");
                arr2.add("Tony");
                arr2.add("Peter");
                arr2.add("Bruce");
                arr2.add("Thor");
                arr2.add("Natasya");
                arr2.add("Stephen");
                MyAdapter mAdapter = new MyAdapter(getContext(), arr2);
                 */

                // 4. set adapter
                recyclerView.setAdapter(mAdapter);

                // 5. set item animator to DefaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Declaring the fragment layout
        rootView = inflater.inflate(R.layout.frag2_layout, container, false);


/*
        //region Dummy data for testing
        // data to populate the RecyclerView with
        ArrayList<String> listData = new ArrayList<>();
        listData.add("Horse");
        listData.add("Cow");
        listData.add("Camel");
        listData.add("Sheep");
        listData.add("Goat");
        listData.add("Dog");
        listData.add("Cat");
        listData.add("Pig");
        listData.add("Crocodile");
        listData.add("Turtle");
        //endregion

 */

        JSONArray listData = new JSONArray();

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewPromo);

        // 2. set layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 3. create an adapter
        //MyPromoAdapter mAdapter = new MyPromoAdapter(getContext(),listData);
        MyPromoAdapter mAdapter = new MyPromoAdapter(getContext(), listData, Frag2.this);

        // 4. set adapter
        recyclerView.setAdapter(mAdapter);

        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Frag2.DownloadTask task = new Frag2.DownloadTask();
        coord = getGPS();
        task.execute(endPoint + coord.getLatitude() + "," + coord.getLongitude() + "," + radiusKm);
        Log.i("EndPoint",endPoint + coord.getLatitude() + "," + coord.getLongitude() + "," + radiusKm);

        //region Floating Button
        FloatingActionButton fab = rootView.findViewById(R.id.floatingPromoRefresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Searching for local events...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Frag2.DownloadTask task = new Frag2.DownloadTask();
                coord = getGPS();
                task.execute(endPoint + coord.getLatitude() + "," + coord.getLongitude() + "," + radiusKm);
                Log.i("EndPoint",endPoint + coord.getLatitude() + "," + coord.getLongitude() + "," + radiusKm);
            }
        });
        //endregion

        return rootView;
    }

    // Getting adHoc user Locations
    public Location getGPS() {
        LocationManager locationManager = (LocationManager) Frag2.this.getContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

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
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location gps = null;

        for (int i=providers.size()-1; i>=0; i--) {
            gps = locationManager.getLastKnownLocation(providers.get(i));
            if (gps != null) break;
        }

        return gps;
    }
}
