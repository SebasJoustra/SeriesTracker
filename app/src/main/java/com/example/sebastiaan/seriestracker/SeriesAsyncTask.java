package com.example.sebastiaan.seriestracker;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Asynctask to search for series in the API, given a user query.
 */

class SeriesAsyncTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private SearchActivity searchAct;

    SeriesAsyncTask(SearchActivity search) {
        this.searchAct = search;
        this.context = this.searchAct.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Searching for shows...", Toast.LENGTH_SHORT).show();
    }

    // Do a httpRequest in the background
    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        TvShow[] seriesArray = null;
        try {
            //Navigate through the JSON result to find the corresponding parameters for the tv-shows
            JSONObject jsonObj = new JSONObject(result);
            JSONArray resultArray = jsonObj.getJSONArray("results");

            // Make TvShow objects based on the API information
            seriesArray = makeTvShowObjects(resultArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Start intent to show the results, called from the SearchActivity
        this.searchAct.seriesStartIntent(seriesArray);
    }

    // Returns an array of TvShow objects based on the information given by the API
    private TvShow[] makeTvShowObjects(JSONArray jsonArray) {
        TvShow[] seriesArray = new TvShow[jsonArray.length()];
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tvshowObj = jsonArray.getJSONObject(i);

                String name = tvshowObj.getString("original_name");
                String id = tvshowObj.getString("id");
                String imageUrl = tvshowObj.getString("poster_path");
                String description = tvshowObj.getString("overview");
                String voteAvg = tvshowObj.getString("vote_average");

                TvShow tvShow = new TvShow(name, id);
                tvShow.setImage(imageUrl);
                tvShow.setDescription(description);
                tvShow.setVoteAvg(voteAvg);

                seriesArray[i] = tvShow;
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return seriesArray;
    }
}
