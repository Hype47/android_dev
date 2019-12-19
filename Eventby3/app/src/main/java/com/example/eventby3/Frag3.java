package com.example.eventby3;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
    Fragment for Neighborhood Tab
 */

public class Frag3 extends Fragment {

    // Variable declarations
    private View rootView;

    // Location variables should be retrieved from user settings
    public static double userLat;
    public static double userLon;
    public static String screenName;
    public static String userid;
    private int radiusKm = 10;


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
                String newsInfo = jsonObject.getString("neighborhood_posts");
                JSONArray arr = new JSONArray(newsInfo);

                // 1. get a reference to recyclerView
                RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewNeighborhood);

                // 2. set layoutManger
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // 3. create an adapter
                MyNeighborhoodAdapter mAdapter = new MyNeighborhoodAdapter(getActivity(),arr,Frag3.this);

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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        String msg = "Credentials: " + UserSignInActivity.jwtToken + "_" + UserSignInActivity.loginName;
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

        if (UserSignInActivity.jwtToken != null && UserSignInActivity.loginName != null) {

            try{
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String URL = "https://news-geocode.herokuapp.com/userlogin/" + UserSignInActivity.loginName;
                Log.i("URL Request",URL);

                StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("User Information", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            // Fetching information
                            userLat = jsonObject.getDouble("defaultLat");
                            userLon = jsonObject.getDouble("defaultLon");
                            screenName = jsonObject.getString("screenName");
                            userid = jsonObject.getString("userid");
                            Log.e("User Location",userLat + "," + userLon);

                            // Modify Toolbar on Main Activity
                            MainActivity.toolbar.setTitle("Hi " + screenName);
                            MainActivity.toolbar.setNavigationIcon(R.drawable.ic_account_edit);

                            // Connecting to API
                            DownloadTask task = new DownloadTask();
                            String endPoint = "https://news-geocode.herokuapp.com/neighborhood_coord/";
                            task.execute(endPoint + userLat + "," + userLon + "," + radiusKm);
                            Log.e("EndPoint",endPoint + userLat + "," + userLon + "," + radiusKm);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error is ", "" + error);
                    }
                }) {

                    //This is for Headers If You Needed --> Authorization JWT
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "JWT " + UserSignInActivity.jwtToken);
                        Log.e("Params",params.toString());
                        return params;
                    }
                };
                queue.add(request);

            } catch (Exception e){
                e.printStackTrace();
            }

            // Retrieving Views in Fragments
            rootView = inflater.inflate(R.layout.frag3_layout, container, false);

            JSONArray listData = new JSONArray();

            // 1. get a reference to recyclerView
            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewNeighborhood);

            // 2. set layoutManager
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // 3. create an adapter
            MyNeighborhoodAdapter mAdapter = new MyNeighborhoodAdapter(getActivity(), listData, Frag3.this);
            //MyAdapter mAdapter = new MyAdapter(getContext(), listData, Frag1.this);

            // 4. set adapter
            recyclerView.setAdapter(mAdapter);

            // 5. set item animator to DefaultAnimator
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            //region Floating Button : Move to create post screen
            FloatingActionButton fab = rootView.findViewById(R.id.floatingCreatePost);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), CreatePostActivity.class);
                    getContext().startActivity(intent);
                }
            });
            //endregion

            return rootView;

        } else {
            // Retrieving Views in Fragments --> if No sign In detected
            rootView = inflater.inflate(R.layout.frag3nouser_layout, container, false);

            // Access to Buttons
            Button buttonLogin = rootView.findViewById(R.id.buttonFrag3Login);
            Button buttonSignUp = rootView.findViewById(R.id.buttonFrag3SignUp);

            // Login Codes
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), UserSignInActivity.class);
                    getContext().startActivity(intent);

                }
            });

            // Sign Up Codes
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), UserRegistrationActivity.class);
                    getContext().startActivity(intent);
                }
            });
        }

        return rootView;
    }

    public double getLat(){
        return this.userLat;
    }

    public double getLon(){
        return this.userLon;
    }

}