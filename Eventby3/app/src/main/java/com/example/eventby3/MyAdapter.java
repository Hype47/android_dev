package com.example.eventby3;



import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mDataset;
    Frag1 frag1;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4;
        Button buttonView, buttonViewMap;
        CardView cardViewNews;
        View dividerTop;
        ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textLine1);
            textView2 = view.findViewById(R.id.textLine2);
            textView3 = view.findViewById(R.id.textLine3);
            textView4 = view.findViewById(R.id.textLine4);
            buttonView = view.findViewById(R.id.buttonReadNews);
            buttonViewMap = view.findViewById(R.id.buttonShowMap);
            cardViewNews = view.findViewById(R.id.cardViewNews);
            dividerTop = view.findViewById(R.id.dividerTop);

        }
    }


    // data is passed into the constructor
    MyAdapter(Context context, JSONArray newsData, Frag1 fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = newsData;
        this.frag1 = fragment;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View view = mInflater.inflate(R.layout.frag1content_layout, parent, false);
        vh = new ViewHolder(view);
        return vh;
    }

    // binds the data to the TextView or CardView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Set initial visibility
        holder.textView2.setVisibility(View.VISIBLE);
        holder.textView4.setVisibility(View.GONE);
        holder.dividerTop.setVisibility(View.GONE);
        holder.buttonView.setVisibility(View.GONE);

        // Get User location
        final Location userCoord = frag1.getGPS();
        final double userLat = userCoord.getLatitude();
        final double userLon = userCoord.getLongitude();

        try {
            final double lat = mDataset.getJSONObject(position).getDouble("lat");
            final double lon = mDataset.getJSONObject(position).getDouble("lon");
            //final String newsLocation = lat + "," + lon;
            final String url = mDataset.getJSONObject(position).getString("url");
            final String title = mDataset.getJSONObject(position).getString("title");
            final String date = mDataset.getJSONObject(position).getString("date");
            final String snips = mDataset.getJSONObject(position).getString("snips");

            // Calculate distance between news and user
            Location newsPosition = new Location("");
            newsPosition.setLatitude(lat);
            newsPosition.setLongitude(lon);
            double distanceInMeter = newsPosition.distanceTo(userCoord);
            DecimalFormat df = new DecimalFormat("#.00");
            String newsLocation;
            if (distanceInMeter < 1000){
                newsLocation = df.format(distanceInMeter) + " meter from your location";
            } else {
                newsLocation = df.format(distanceInMeter/1000) + " km from your location";
            }

            Snackbar.make(frag1.getView(), "Displaying news...", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

            // Display on screen
            holder.textView1.setText(title);
            holder.textView2.setText(date);
            holder.textView3.setText("News @" + newsLocation);
            holder.textView4.setText(snips);



            //region Press Button to goto Map
            holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NewsLocationActivity.class);
                    // Passing values
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    intent.putExtra("title",title);
                    intent.putExtra("date",date);
                    intent.putExtra("snips",snips);
                    intent.putExtra("url",url);
                    intent.putExtra("userLat",userLat);
                    intent.putExtra("userLon",userLon);
                    Log.i("User Coordinate", userLat +"," + userLon);
                    context.startActivity(intent);
                }
            });
            //endregion

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }

}
