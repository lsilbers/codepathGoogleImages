package com.lsilberstein.googleimages.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * code adapted from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int lastVisibleItem;
    private int totalItemCount;
    int[] lastVisibleItemPositions;

    private int current_page = 1;

    private StaggeredGridLayoutManager layoutManager;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.layoutManager = staggeredGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = layoutManager.getItemCount();
        lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(lastVisibleItemPositions);
        int max = lastVisibleItemPositions[0];
        for (int i : lastVisibleItemPositions) {
            if (i > max) {
                max = i;
            }
        }

        // last visible item on the screen is the highest of the values in the spans
        lastVisibleItem = max;

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        // if the last item on screen is the last item in the list we need to load more
        if (!loading && totalItemCount
                <= (lastVisibleItem + 5)) {
            // End has been reached

            // Do something
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    // resets the listener for the next query
    public void reset(){
        lastVisibleItem = 0;
        totalItemCount = 0;
        current_page = 1;
        previousTotal = 0;
        lastVisibleItemPositions = null;
    }

    public abstract void onLoadMore(int current_page);
}