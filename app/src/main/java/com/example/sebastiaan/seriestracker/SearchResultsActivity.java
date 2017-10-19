package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Activity which shows the search results to the user in a listview
 */

public class SearchResultsActivity extends AppCompatActivity {

    TvShow[] seriesArray;
    ListView lvSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get information from SearchActivity
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seriesArray = (TvShow[]) bundle.getSerializable("seriesArray");

        // Initialize View
        lvSeries = (ListView) findViewById(R.id.lvSearchResults);

        // Set adapter to the listview
        setAdapter();

    }

    // Set adapter to the listview
    private void setAdapter() {
        ListAdapter adapter = new SearchResultsAdapter(this, seriesArray);
        lvSeries.setAdapter(adapter);
        lvSeries.setOnItemClickListener(new ItemClickListener());
    }

    // Clicklistener applied to the listview, which triggers when a tv-show is clicked and will
    // redirect the user to the specific tv-show activity
    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TvShow tvShowItem = seriesArray[i];

            // Store information into the intent and redirect to TvShowResultActivity
            Intent intent = new Intent(getApplicationContext(), TvShowResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("tvShow", tvShowItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
