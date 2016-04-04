package com.gl.objectfinder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gl.objectfinder.R;
import com.gl.objectfinder.entity.Annotation;
import com.gl.objectfinder.utilities.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by shahidpasha on 10/10/15.
 */
public class PreviewGridListAdapter extends RecyclerView.Adapter<PreviewGridListAdapter.ViewHolder> {
    private ArrayList mDataset;
    List<Bitmap> mItemList;
    View.OnClickListener mListClickListener;


    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView thumbImageView;
        public View contentView;


        public ViewHolder(View view) {
            super(view);

            thumbImageView = (ImageView) view.findViewById(R.id.iv_thumb);
            contentView = view.findViewById(R.id.card_view);

        }
    }


    public PreviewGridListAdapter(List<Bitmap> itemList, Context context) {

        this.mItemList = itemList;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gridview, parent, false);


        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.thumbImageView.setImageBitmap(mItemList.get(position));

        holder.contentView.setTag(position);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemList.size();
    }






}