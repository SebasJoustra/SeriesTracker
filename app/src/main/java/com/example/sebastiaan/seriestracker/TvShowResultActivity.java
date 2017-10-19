package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

/**
 * Activity which displays the selected tv-show from the result list
 */

public class TvShowResultActivity extends AppCompatActivity {

    private static final String TAG = "TvShowResultActivity";

    TvShow tvShow;
    TextView tvName;
    TextView tvDescription;
    ImageView ivCoverImage;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_result);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get information from previous activity
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        tvShow = (TvShow) bundle.getSerializable("tvShow");

        // Set toolbar title
        setTitle("Tv Show: " + tvShow.name);

        // Get database information
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Set Views
        setComponents();

    }

    private void setComponents() {
        tvName = (TextView) findViewById(R.id.tvTvShowName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivCoverImage = (ImageView) findViewById(R.id.ivCoverImage);

        tvName.setText(tvShow.name);
        tvDescription.setText(tvShow.description);

        new ImageAsyncTask(ivCoverImage).execute("http://image.tmdb.org/t/p/w185//" + tvShow.image);
    }

    // Add tv show and go back to main activity
    public void addToWatchList(View view) {
        addTvShow(tvShow);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void addTvShow(TvShow tvShow) {
        ExtraPropertiesAsyncTask asyncTask = new ExtraPropertiesAsyncTask(tvShow);
        asyncTask.execute(tvShow.id, "tv_id");
    }
}
