package com.lsilberstein.googleimages.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsilberstein.googleimages.adapters.ImageResultAdapter;
import com.lsilberstein.googleimages.model.ImageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lsilberstein on 10/28/15.
 */
public class GoogleImageClient {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    private static final String QUERY_PARAM = "&q=";
    private static final String RESULTS_PARAM = "&rsz=8";
    private static final String START_PARAM = "&start=";
    private static final String TAG = "GIC";

    private AsyncHttpClient client;

    // set when the query successfully returns
    private String resultsUrl;
    private int currentPage;
    private int[] pageStarts = new int[]{0,0,0,0,0,0,0,0};

    private ImageResultAdapter adapter;

    public GoogleImageClient(ImageResultAdapter adapter) {
        client = new AsyncHttpClient();
        this.adapter = adapter;
    }

    public void searchGoogleFor(@NonNull String query) {
        Log.d(TAG, "Search for query " + query);
        currentPage = 1;
        resultsUrl = BASE_URL+RESULTS_PARAM+QUERY_PARAM+query;
        client.get(resultsUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    adapter.addNewList(ImageResult.fromJson(response.getJSONObject("responseData").getJSONArray("results")));
                    JSONArray pages = response.getJSONObject("responseData").getJSONObject("cursor").getJSONArray("pages");
                    for (int i = 0; i < pages.length(); i++) {
                        int label = pages.getJSONObject(i).getInt("label");
                        int start = pages.getJSONObject(i).getInt("start");
                        pageStarts[label-1] = start;
                    }
                    getMoreResults(); // initial query gets two pages
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // do something
            }
        });
    }

    public void getMoreResults() {
        if(resultsUrl != null) {
            currentPage++;
            if(currentPage > pageStarts.length){
                return;
            }
            Log.d(TAG, "retrieving results page" + currentPage);
            client.get(resultsUrl+START_PARAM+pageStarts[currentPage-1], new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        adapter.addAll(ImageResult.fromJson(response.getJSONObject("responseData").getJSONArray("results")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // do something
                }
            });
        }
    }
}
