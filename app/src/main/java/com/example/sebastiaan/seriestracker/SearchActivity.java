package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity which lets the user search for tv-shows
 */

public class SearchActivity extends AppCompatActivity {

    EditText etShowName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize View
        etShowName = (EditText) findViewById(R.id.etSearchSeries);

    }

    // This function will be called when the app gets a result from the API and stores it into a List
    public void seriesStartIntent(TvShow[] seriesArray) {
        if(seriesArray != null) {
            // Call intent to show the results
            Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("seriesArray", seriesArray);
            intent.putExtras(bundle);
            startActivity(intent);

            etShowName.getText().clear();
        } else {
            // Could not connect to the server. Could be because of the internet connection or when
            // the API is not available.
            Toast.makeText(this, "Did not receive information from server...", Toast.LENGTH_LONG).show();
        }
    }

    // Search button was clicked, redirect to Asynctask to handle the results.
    public void searchClicked(View view) {
        SeriesAsyncTask aSyncTask = new SeriesAsyncTask(this);
        aSyncTask.execute(etShowName.getText().toString(), "search");
    }
}
