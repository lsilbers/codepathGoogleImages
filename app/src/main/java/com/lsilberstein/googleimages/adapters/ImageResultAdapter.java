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
    private OnItemClickListener itemClickListener;

    public ImageResultAdapter(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view, context, itemClickListener);
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

    // adds all the results and notifies that the items have been added
    public void addAll(List<ImageResult> imageResults) {
        int initialSize = getItemCount();
        for (ImageResult result : imageResults) {
            this.imageResults.add(result);
            notifyItemInserted(initialSize);
            initialSize++;
        }
    }

    //
    public void addNewList(List<ImageResult> imageResults) {
        clear();
        this.imageResults.addAll(imageResults);
        notifyDataSetChanged();
    }

    // removes all current results
    public void clear() {
        this.imageResults.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public Context context;

        public ViewHolder(View itemView, Context context, final OnItemClickListener listener) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ivImageItem);
            this.context = context;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
