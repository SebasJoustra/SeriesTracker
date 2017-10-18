package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        etShowName = (EditText) findViewById(R.id.etSearchSeries);

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
        SeriesAsyncTask aSyncTask = new SeriesAsyncTask(this);
        aSyncTask.execute(etShowName.getText().toString(), "search");
    }
}
