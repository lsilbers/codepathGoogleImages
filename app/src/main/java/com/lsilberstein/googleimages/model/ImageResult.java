package com.lsilberstein.googleimages.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsilberstein on 10/28/15.
 */
public class ImageResult implements Parcelable {
    private static final String TAG = "IR";
    // where the image came from
    public String originalContextUrl;
    public String tbUrl;
    public int tbWidth;
    public int tbHeight;
    public String url;

    public ImageResult(String originalContextUrl, String tbUrl, int tbWidth, int tbHeight, String url) {
        this.originalContextUrl = originalContextUrl;
        this.tbUrl = tbUrl;
        this.tbWidth = tbWidth;
        this.tbHeight = tbHeight;
        this.url = url;
    }

    public static List<ImageResult> fromJson(JSONArray jsonArray) {
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i = 0; i < jsonArray.length();i++) {
            try {
                String originalContextUrl = jsonArray.getJSONObject(i).getString("originalContextUrl");
                String tbUrl = jsonArray.getJSONObject(i).getString("tbUrl");
                int tbWidth = jsonArray.getJSONObject(i).getInt("tbWidth");
                int tbHeight = jsonArray.getJSONObject(i).getInt("tbHeight");
                String url = jsonArray.getJSONObject(i).getString("url");
                ImageResult result = new ImageResult(originalContextUrl,tbUrl,tbWidth,tbHeight,url);
                results.add(result);
            } catch (JSONException e) {
                Log.e(TAG,"Item number " + i + " was malformed:" + e.getMessage());
            }
        }

        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalContextUrl);
        dest.writeString(this.tbUrl);
        dest.writeInt(this.tbWidth);
        dest.writeInt(this.tbHeight);
        dest.writeString(this.url);
    }

    protected ImageResult(Parcel in) {
        this.originalContextUrl = in.readString();
        this.tbUrl = in.readString();
        this.tbWidth = in.readInt();
        this.tbHeight = in.readInt();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ImageResult> CREATOR = new Parcelable.Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
