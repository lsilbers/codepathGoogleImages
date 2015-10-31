package com.lsilberstein.googleimages.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.Arrays;

/**
 * code adapted from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    private int current_page = 1;

    private StaggeredGridLayoutManager layoutManager;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.layoutManager = staggeredGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        int[] firstVisInSpan = layoutManager.findFirstVisibleItemPositions(null);
        int min = firstVisInSpan[0];
        for (int i : firstVisInSpan) {
            if (i < min) {
                min = i;
            }
        }
        // first visible item is the index in the adapter of the first item on the screen
        firstVisibleItem = min;

        Log.d(TAG, "onScrolled called: visibleItemCount:" + visibleItemCount +
                ", totalItemCount:" + totalItemCount + ", firstVisibleItem:" + firstVisibleItem +
                ", firstVisInSpan:"+ Arrays.toString(firstVisInSpan));
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        // the index of the last item that we can see on the screen is the firstVisibleItem + the
        // visibleItemCount - if the total number of items is equal to or less than that value then
        // we need to retrieve more values (we are actually preloading an extra 5 items off the screen here)
        if (!loading && totalItemCount
                <= (firstVisibleItem + visibleItemCount + 5)) {
            // End has been reached

            // Do something
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    // resets the listener for the next query
    public void reset(){
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        current_page = 1;
        previousTotal = 0;
    }

    public abstract void onLoadMore(int current_page);
}