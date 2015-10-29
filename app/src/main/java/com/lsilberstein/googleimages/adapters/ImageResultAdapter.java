package com.lsilberstein.googleimages.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lsilberstein.googleimages.R;
import com.lsilberstein.googleimages.model.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lsilberstein on 10/28/15.
 */
public class ImageResultAdapter extends RecyclerView.Adapter<ImageResultAdapter.ViewHolder>{

    private final List<ImageResult> imageResults;

    public ImageResultAdapter(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_image, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageResult imageResult = imageResults.get(position);

        // picasso to load it into the view -> holder.image
        Picasso.with(holder.context).load(imageResult.tbUrl).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imageResults.size();
    }

    // adds all the results and notifies that the dataset has changed
    public void addAll(List<ImageResult> imageResults) {
        this.imageResults.addAll(imageResults);
        notifyDataSetChanged();
    }

    // removes all current results - does not notify the view
    public void clear() {
        this.imageResults.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ivImageItem);
            this.context = context;
        }
    }
}
