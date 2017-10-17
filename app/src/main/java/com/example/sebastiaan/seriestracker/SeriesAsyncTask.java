package com.example.sebastiaan.seriestracker;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeriesAsyncTask extends AsyncTask<String, Integer, String> {
    Context context;
    SearchActivity searchAct;

    public SeriesAsyncTask(SearchActivity search) {
        this.searchAct = search;
        this.context = this.searchAct.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Searching for shows...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        TvShow[] seriesArray = null;

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray resultArray = jsonObj.getJSONArray("results");
            seriesArray = makeTvShowObjects(resultArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.searchAct.seriesStartIntent(seriesArray);
    }

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

                seriesArray[i] = new TvShow(name, id, imageUrl, description, voteAvg);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return seriesArray;
    }
}
