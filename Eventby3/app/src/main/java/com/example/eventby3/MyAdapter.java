package com.example.eventby3;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import static androidx.core.content.ContextCompat.startActivity;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // stores and recycles views as they are scrolled off screen
    //public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;
        Button buttonView;

        ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textLine1);
            textView2 = view.findViewById(R.id.textLine2);
            textView3 = view.findViewById(R.id.textLine3);
            buttonView = view.findViewById(R.id.buttonReadNews);
            //view.setOnClickListener(this);
        }
    }

    // data is passed into the constructor
    //MyAdapter(Context context, List<String> newsData) {
    MyAdapter(Context context, JSONArray newsData) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = newsData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.frag1content_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView or CardView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //region Debug codes
/*
        String news = mDataset.get(position);
        holder.textView1.setText(news);
        holder.textView2.setText(news);
        holder.textView3.setText(news);

 */
        //endregion

        try {
            final String url = mDataset.getJSONObject(position).getString("url");
            holder.textView1.setText(mDataset.getJSONObject(position).getString("title"));
            holder.textView2.setText(mDataset.getJSONObject(position).getString("date"));
            //holder.textView3.setText(mDataset.getJSONObject(position).getString("url"));
            holder.buttonView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Uri uriUrl = Uri.parse(url);
                    Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW,uriUrl);
                    context.startActivity(intent);

                }
            });
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
