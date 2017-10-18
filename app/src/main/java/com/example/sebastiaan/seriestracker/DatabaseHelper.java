package com.example.sebastiaan.seriestracker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class DatabaseHelper {

    private DatabaseReference mDatabase;

    public DatabaseHelper() {
    }

    public void addTvShow(TvShow tvShow) {
        ExtraPropertiesAsyncTask asyncTask = new ExtraPropertiesAsyncTask(tvShow);
        asyncTask.execute(tvShow.id, "tv_id");
    }

    public void removeTvShow(TvShow tvShow) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child(tvShow.id).removeValue();
    }

    public void updateEpisode(TvShow tvShow, int season, int episode) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //TODO complete
        //mDatabase.child(tvShow.id).child("episodesCompletedList").getC
    }
}
