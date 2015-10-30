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
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem;
    int visibleItemCount;
    int totalItemCount;

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
        int max = firstVisInSpan[0];
        for (int i : firstVisInSpan) {
            if (i > max) {
                max = i;
            }
        }
        firstVisibleItem = max;

        Log.d(TAG, "onScrolled called: visibleItemCount:" + visibleItemCount +
                ", totalItemCount:" + totalItemCount + ", firstVisibleItem:" + firstVisibleItem +
                ", firstVisInSpan:"+ Arrays.toString(firstVisInSpan));
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && totalItemCount
                <= (firstVisibleItem + visibleItemCount+ visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}