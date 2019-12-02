package com.example.eventby3;



import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;
        Button buttonView, buttonViewMap;
        CardView cardViewNews;
        ViewGroup expandableLayout;
        MapView mapViewNews;
        ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textLine1);
            textView2 = view.findViewById(R.id.textLine2);
            textView3 = view.findViewById(R.id.textLine3);
            buttonView = view.findViewById(R.id.buttonReadNews);
            buttonViewMap = view.findViewById(R.id.buttonShowMap);
            cardViewNews = view.findViewById(R.id.cardViewNews);
            mapViewNews = view.findViewById(R.id.mapViewNews);
            //expandableLayout = view.findViewById(R.id.expandableLayout);

        }
    }


    // data is passed into the constructor
    MyAdapter(Context context, JSONArray newsData) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = newsData;
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
        holder.textView3.setVisibility(View.GONE);
        holder.mapViewNews.setVisibility(View.GONE);

       try {
            final String url = mDataset.getJSONObject(position).getString("url");
            holder.textView1.setText(mDataset.getJSONObject(position).getString("title"));
            holder.textView2.setText(mDataset.getJSONObject(position).getString("date"));
            holder.textView3.setText(mDataset.getJSONObject(position).getString("snips"));

            //region Go to news Link button
            holder.buttonView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Uri uriUrl = Uri.parse(url);
                    Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW,uriUrl);
                    context.startActivity(intent);

                }
            });
            //endregion

            //region Expand/Collapse card
           holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (holder.textView3.getVisibility()==View.GONE){
                       TransitionManager.beginDelayedTransition(holder.cardViewNews, new AutoTransition());
                       holder.textView3.setVisibility(View.VISIBLE);
                       holder.mapViewNews.setVisibility(View.VISIBLE);
                       holder.buttonViewMap.setText(R.string.button_text_1A);
                   } else {
                       //TransitionManager.beginDelayedTransition(holder.cardViewNews, new AutoTransition());
                       holder.textView3.setVisibility(View.GONE);
                       holder.mapViewNews.setVisibility(View.GONE);
                       holder.buttonViewMap.setText(R.string.button_text_1);
                   }
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
        //return mDataset.size();
        return mDataset.length();
    }
}
