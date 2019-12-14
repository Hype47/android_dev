package com.example.eventby3;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.List;

public class MyPromoAdapter extends RecyclerView.Adapter<MyPromoAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mDataset;
    //private List<String> mDataset;
    Frag2 frag2;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLine1, textLine2, textLine3;
        Button buttonViewMap;
        CardView cardViewPromo;
        //View dividerTop;
        ViewHolder(View view) {
            super(view);
            textLine1 = view.findViewById(R.id.textPromoTitle);
            textLine2 = view.findViewById(R.id.textPromoLocation);
            textLine3 = view.findViewById(R.id.textPromoDate);
            //textView4 = view.findViewById(R.id.textLine4);
            //buttonView = view.findViewById(R.id.buttonReadNews);
            buttonViewMap = view.findViewById(R.id.buttonPromoMap);
            cardViewPromo = view.findViewById(R.id.cardViewPromo);
            //dividerTop = view.findViewById(R.id.dividerTop);
        }
    }

    // data is passed into the constructor
    MyPromoAdapter(Context context, JSONArray promoData, Frag2 fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = promoData;
        this.frag2 = fragment;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyPromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyPromoAdapter.ViewHolder vh;
        View view = mInflater.inflate(R.layout.frag2content_layout, parent, false);
        vh = new MyPromoAdapter.ViewHolder(view);
        return vh;
    }

    // binds the data to the TextView or CardView in each row
    @Override
    public void onBindViewHolder(final MyPromoAdapter.ViewHolder holder, int position) {

        //holder.textLine1.setText(mDataset.get(position));
        //holder.textLine2.setText(mDataset.get(position));
        //holder.textLine3.setText(mDataset.get(position));

        // Get User location
        Location userCoord = frag2.getGPS();
        final double userLat = userCoord.getLatitude();
        final double userLon = userCoord.getLongitude();

        try {
            final double lat = mDataset.getJSONObject(position).getDouble("lat");
            final double lon = mDataset.getJSONObject(position).getDouble("lon");
            //final String url = mDataset.getJSONObject(position).getString("url");
            final String title = mDataset.getJSONObject(position).getString("title");
            final String date = mDataset.getJSONObject(position).getString("startdate");
            final String enddate = mDataset.getJSONObject(position).getString("enddate");
            String period = date + " - " + enddate;
            //final String snips = mDataset.getJSONObject(position).getString("snips");

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

            // Display data onScreen
            holder.textLine1.setText(title);
            holder.textLine2.setText(period);
            holder.textLine3.setText("Promo and Event @" + newsLocation);
            //holder.textView4.setText(snips);


            //region Press Button to goto Map
            holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EventLocationActivity.class);
                    // Passing values
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    intent.putExtra("title",title);
                    intent.putExtra("date",date);
                    intent.putExtra("enddate",enddate);
                    //intent.putExtra("snips",snips);
                    //intent.putExtra("url",url);
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
