package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SearchResultsActivity extends AppCompatActivity {

    TvShow[] seriesArray;
    ListView lvSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seriesArray = (TvShow[]) bundle.getSerializable("seriesArray");

        lvSeries = (ListView) findViewById(R.id.lvSearchResults);

        setAdapter();

    }

    private void setAdapter() {
        ListAdapter adapter = new SearchResultsAdapter(this, seriesArray);
        lvSeries.setAdapter(adapter);
        lvSeries.setOnItemClickListener(new ItemClickListener());
    }

    public class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), TvShowResultActivity.class);
            TvShow tvShowItem = seriesArray[i];
            Bundle bundle = new Bundle();
            bundle.putSerializable("tvShow", tvShowItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
