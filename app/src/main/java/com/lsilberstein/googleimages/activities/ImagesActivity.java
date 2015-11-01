package com.lsilberstein.googleimages.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lsilberstein.googleimages.R;
import com.lsilberstein.googleimages.adapters.ImageResultAdapter;
import com.lsilberstein.googleimages.fragments.SettingsDialog;
import com.lsilberstein.googleimages.model.ImageResult;
import com.lsilberstein.googleimages.network.GoogleImageClient;
import com.lsilberstein.googleimages.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private static final String SETTINGS_DIALOG_TAG = "settings_dialog";
    private RecyclerView rvImages;
    private ImageResultAdapter aImages;
    private List<ImageResult> images;
    private GoogleImageClient client;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private SettingsDialog settingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsDialog =  SettingsDialog.newInstance();

        // Set up the data source
        images = new ArrayList<>();

        // create the adapter
        aImages = new ImageResultAdapter(images);


        // create our client which will handle the data retrieval
        client = new GoogleImageClient(aImages);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        // set up the recycler view
        rvImages = (RecyclerView) findViewById(R.id.rvImages);
        rvImages.setAdapter(aImages);
        rvImages.setLayoutManager(layoutManager);

        onScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                client.getMoreResults();
            }
        };
        rvImages.addOnScrollListener(onScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_images, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                onScrollListener.reset();
                // perform query here
                client.searchGoogleFor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // open the settings dialog
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            settingsDialog.show(fragmentManager, SETTINGS_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFiltersSaved(String size, String colour, String type, String site) {
        client.setSize(size);
        client.setColour(colour);
        client.setType(type);
        client.setSite(site);
    }
}
