package com.example.sebastiaan.seriestracker;

import android.os.AsyncTask;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ExtraPropertiesAsyncTask extends AsyncTask<String, Integer, String> {
    TvShow tvShow;
    private DatabaseReference mDatabase;

    private static final String TAG = "ExtraPropertiesAsyncTas";

    public ExtraPropertiesAsyncTask(TvShow aTvShow) {
        this.tvShow = aTvShow;
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

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

    private ArrayList<ArrayList<Boolean>> getEpisodesPerSeason(JSONArray seasonsJsonArray) {
        ArrayList<ArrayList<Boolean>> episodesPerSeason = new ArrayList<ArrayList<Boolean>>();
        for (int i = 0; i < seasonsJsonArray.length(); i++) {
            try {
                JSONObject seasonObj = seasonsJsonArray.getJSONObject(i);
                int count = seasonObj.getInt("episode_count");
                ArrayList<Boolean> tempList = new ArrayList<>();
                for(int j = 0; j<count; j++) {
                    tempList.add(false);
                }
                episodesPerSeason.add(tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return episodesPerSeason;
    }

    private void writeToDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tvShow.id).setValue(tvShow);
    }

}
