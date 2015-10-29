package com.lsilberstein.googleimages.network;

import android.support.annotation.NonNull;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsilberstein.googleimages.adapters.ImageResultAdapter;
import com.lsilberstein.googleimages.model.ImageResult;

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

    private AsyncHttpClient client;

    // set when the query successfully returns
    private String moreResultsUrl;

    private ImageResultAdapter adapter;

    public GoogleImageClient() {
        client = new AsyncHttpClient();
    }

    public void searchGoogleFor(@NonNull String query, @NonNull final ImageResultAdapter adapter) {
        this.adapter = adapter;
        client.get(BASE_URL+RESULTS_PARAM+QUERY_PARAM+query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    adapter.clear();
                    adapter.addAll(ImageResult.fromJson(response.getJSONObject("responseData").getJSONArray("results")));
                    moreResultsUrl = response.getJSONObject("responseData").getString("moreResultsUrl");
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
        client.get(moreResultsUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    adapter.addAll(ImageResult.fromJson(response.getJSONObject("responseData").getJSONArray("results")));
                    moreResultsUrl = response.getJSONObject("responseData").getString("moreResultsUrl");
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
