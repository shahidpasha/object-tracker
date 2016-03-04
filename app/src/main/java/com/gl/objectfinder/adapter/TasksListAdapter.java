package com.gl.objectfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gl.objectfinder.R;
import com.gl.objectfinder.entity.Annotation;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by shahidpasha on 10/10/15.
 */
public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {
    private ArrayList mDataset;
    List<Annotation> mItemList;
    View.OnClickListener mListClickListener;


    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView locationTextView;
        public ImageView statusImageView;
        public View contentView;


        public ViewHolder(View view) {
            super(view);

            titleTextView = (TextView) view.findViewById(R.id.tv_title);
            locationTextView = (TextView) view.findViewById(R.id.tv_desc);
            contentView = view.findViewById(R.id.card_view);

        }
    }


    public TasksListAdapter(List<Annotation> itemList, Context context, View.OnClickListener listClickListener) {

        this.mItemList = itemList;
        this.mContext = context;
        this.mListClickListener = listClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_list_item, parent, false);


        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Annotation item = mItemList.get(position);
        holder.titleTextView.setText(item.getDescription());
        holder.locationTextView.setText(item.getScore());

        holder.contentView.setTag(position);
        holder.contentView.setOnClickListener(mListClickListener);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemList.size();
    }






}