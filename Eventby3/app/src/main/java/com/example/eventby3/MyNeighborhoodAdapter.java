package com.example.eventby3;
/*
public class MyNeighborhoodAdapter {
}

 */

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

import java.util.List;

public class MyNeighborhoodAdapter extends RecyclerView.Adapter<MyNeighborhoodAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mDataset;
//    private List<String> mDataset;
    Frag3 frag3;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLine1, textLine2, textLine3, textLine4;
        Button buttonViewMap;
        CardView cardViewNeighborhood;
        //View dividerTop;
        ViewHolder(View view) {
            super(view);
            textLine1 = view.findViewById(R.id.textNeighborhoodTitle);
            textLine3 = view.findViewById(R.id.textNeighborhoodLocation);
            textLine2 = view.findViewById(R.id.textNeighborhoodDate);
            textLine4 = view.findViewById(R.id.textNeighborhoodAuthor);
            //buttonView = view.findViewById(R.id.buttonReadNews);
            //buttonViewMap = view.findViewById(R.id.buttonPromoMap);
            cardViewNeighborhood = view.findViewById(R.id.cardViewNeighborhood);
            //dividerTop = view.findViewById(R.id.dividerTop);
        }
    }

    // data is passed into the constructor
    MyNeighborhoodAdapter(Context context, JSONArray promoData, Frag3 fragment) {
//    MyNeighborhoodAdapter(Context context, List<String> promoData, Frag3 fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = promoData;
        this.frag3 = fragment;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyNeighborhoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyNeighborhoodAdapter.ViewHolder vh;
        View view = mInflater.inflate(R.layout.frag3content_layout, parent, false);
        vh = new MyNeighborhoodAdapter.ViewHolder(view);
        return vh;
    }

    // binds the data to the TextView or CardView in each row
    @Override
    public void onBindViewHolder(final MyNeighborhoodAdapter.ViewHolder holder, int position) {

        /*holder.textLine1.setText(mDataset.get(position));
        holder.textLine2.setText(mDataset.get(position));
        holder.textLine3.setText(mDataset.get(position));
        holder.textLine4.setText(mDataset.get(position));*/


        /*// Get User location
        Location userCoord = frag3.getGPS();
        final double userLat = userCoord.getLatitude();
        final double userLon = userCoord.getLongitude();*/

        try {
            final double lat = mDataset.getJSONObject(position).getDouble("lat");
            final double lon = mDataset.getJSONObject(position).getDouble("lon");
            final String newsLocation = "Post @" + lat + "," + lon;
            final String title = mDataset.getJSONObject(position).getString("title");
            final String date = mDataset.getJSONObject(position).getString("date");
            final String content = mDataset.getJSONObject(position).getString("content");
            final String author = mDataset.getJSONObject(position).getString("createdBy");

            holder.textLine1.setText(title);
            holder.textLine2.setText(date);
            holder.textLine3.setText(newsLocation);
            holder.textLine4.setText(author);


/*
            //region Expand/Collapse card
            holder.cardViewNews.setOnClickListener(new View.OnClickListener() {
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
                    context.startActivity(intent);
                }
                /*
               @Override
               public void onClick(View view) {
                   if (holder.textView4.getVisibility()==View.GONE){
                       TransitionManager.beginDelayedTransition(holder.cardViewNews, new AutoTransition());
                       holder.textView2.setVisibility(View.VISIBLE);
                       holder.textView4.setVisibility(View.VISIBLE);
                       holder.dividerTop.setVisibility(View.VISIBLE);
                   } else {
                       //TransitionManager.beginDelayedTransition(holder.cardViewNews, new AutoTransition());
                       holder.textView2.setVisibility(View.GONE);
                       holder.textView4.setVisibility(View.GONE);
                       holder.dividerTop.setVisibility(View.GONE);
                   }
               }


           });
           //endregion

 */

            //region Press Button to goto Map
            //holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
            holder.cardViewNeighborhood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NeighborhoodLocationActivity.class);
                    // Passing values
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    intent.putExtra("title",title);
                    intent.putExtra("date",date);
                    intent.putExtra("content",content);
                    intent.putExtra("author",author);
                    Log.i("Post Coordinate", lat +"," + lon);
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
