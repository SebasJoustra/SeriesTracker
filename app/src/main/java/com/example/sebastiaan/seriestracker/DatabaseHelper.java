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
}
