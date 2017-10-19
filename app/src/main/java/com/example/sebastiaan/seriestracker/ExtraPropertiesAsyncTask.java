package com.example.sebastiaan.seriestracker;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class functions as a helper class to add new properties to a TvShow object asynchronously
 * and writes to the database. It will get called when a user adds a new tv show and could be used
 * to update existing items in the database as well.
 */

class ExtraPropertiesAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "ExtraPropertiesAsyncTas";

    private TvShow tvShow;

    ExtraPropertiesAsyncTask(TvShow aTvShow) {
        this.tvShow = aTvShow;
    }

    // Do a httpRequest in the background
    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    // After the API returns the information, navigate through the JSON result to find the
    // corresponding parameters for the tv-show
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            Log.d(TAG, "onPostExecute: "+jsonObj.getInt("number_of_episodes"));
            tvShow.setNumOfEpisodes(jsonObj.getInt("number_of_episodes"));
            tvShow.setNumOfSeasons(jsonObj.getInt("number_of_seasons"));
            JSONArray seasons = jsonObj.getJSONArray("seasons");
            tvShow.setEpisodesCompletedList(getEpisodesPerSeason(seasons));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeToDatabase();
    }

    // Returns a list of multiple boolean-lists, where each item in the boolean-list  corresponds
    // a tv-show episode and whether it is watched. Initially the values are set to false, of
    // course. The boolean-lists itself represent the individual seasons.
    private ArrayList<ArrayList<Boolean>> getEpisodesPerSeason(JSONArray seasonsJsonArray) {
        ArrayList<ArrayList<Boolean>> episodesPerSeason = new ArrayList<ArrayList<Boolean>>();
        for (int i = 0; i < seasonsJsonArray.length(); i++) {
            try {
                JSONObject seasonObj = seasonsJsonArray.getJSONObject(i);
                int count = seasonObj.getInt("episode_count");
                int seasonNum = seasonObj.getInt("season_number");

                // Skip season number 0, since that represents an "Extra" season, which is not
                // desired for this app.
                if(seasonNum != 0) {
                    episodesPerSeason.add(fillSeason(count));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return episodesPerSeason;
    }

    // Returns a season (List) with episodes that are not watched yet.
    private ArrayList<Boolean> fillSeason(int count) {
        ArrayList<Boolean> seasonEpisodes = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            seasonEpisodes.add(false);
        }
        return seasonEpisodes;
    }

    // Write the result to the database
    private void writeToDatabase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows").child(tvShow.id).setValue(tvShow);
    }

}
