package com.example.sebastiaan.seriestracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DatabaseHelper {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public DatabaseHelper() {
        mAuth = FirebaseAuth.getInstance();
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

    public void setChecked(TvShow tvShow, int season, int episode) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows")
                .child(tvShow.id);
    }
}
