package com.lsilberstein.googleimages.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.lsilberstein.googleimages.R;
import com.lsilberstein.googleimages.model.ImageResult;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        ImageResult result = intent.getParcelableExtra(ImagesActivity.IMAGE_RESULT);

        ImageView ivFullscreen = (ImageView) findViewById(R.id.ivFullscreen);
        Picasso.with(this).load(result.url).into(ivFullscreen);
    }

}
