package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TvShowResultActivity extends AppCompatActivity {

    private static final String TAG = "TvShowResultActivity";

    TvShow tvShow;
    TextView tvName;
    TextView tvDescription;

    String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_result);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        tvShow = (TvShow) bundle.getSerializable("tvShow");

        setTitle("Tv Show: " + tvShow.name);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Read currect tv shows data to add the new one later
        ValueEventListener tvShowsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //tvShows = dataSnapshot.child("users").child("tvShows").getValue(TvShows.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(tvShowsListener);

        setComponents();

    }

    private void setComponents() {
        tvName = (TextView) findViewById(R.id.tvTvShowName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvName.setText(tvShow.name);
        tvDescription.setText(tvShow.description);

        new DownloadImageTask((ImageView) findViewById(R.id.ivCoverImage)).execute(BASE_URL + tvShow.image);
    }

    public void addToWatchList(View view) {
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child(tvShow.id).setValue(tvShow);
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.addTvShow(tvShow);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // Source: https://stackoverflow.com/questions/5776851/load-image-from-url
        private ImageView ivCoverImage;


        public DownloadImageTask(ImageView image) {
            this.ivCoverImage = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmpResult = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmpResult = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmpResult;
        }

        protected void onPostExecute(Bitmap result) {
            ivCoverImage.setImageBitmap(result);
        }
    }
}
