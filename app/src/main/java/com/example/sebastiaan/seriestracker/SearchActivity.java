package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    EditText etShowName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etShowName = (EditText) findViewById(R.id.etSearchSeries);
        String showName = etShowName.getText().toString();

    }

    public void seriesStartIntent(TvShow[] seriesArray) {
        if(seriesArray != null) {
            Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("seriesArray", seriesArray);
            intent.putExtras(bundle);
            startActivity(intent);

            etShowName.getText().clear();
        } else {
            Toast.makeText(this, "Did not receive information from server...", Toast.LENGTH_LONG).show();
        }
    }

    public void searchClicked(View view) {
        String test = "breaking";
        SeriesAsyncTask aSyncTask = new SeriesAsyncTask(this);
        aSyncTask.execute(test, "search");
    }
}
