package com.example.sebastiaan.seriestracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;

public class TvShowProgressActivity extends AppCompatActivity {

    private static final String TAG = "TvShowProgressActivity";

    TextView tvProgress;
    TextView tvDescription;
    LinearLayout layout;

    TvShow mTvShow;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    ArrayList<ArrayList<Boolean>> episodesCompletedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_progress);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        mTvShow = (TvShow) intent.getSerializableExtra("tvShow");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Set toolbar title to the show name
        setTitle(mTvShow.name);

        // Initialize Views
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvDescription = (TextView) findViewById(R.id.tvProgressDescription);
        layout = (LinearLayout) findViewById(R.id.llShow);

        // Set views
        tvDescription.setText(mTvShow.description);
        int progress = getProgress();
        tvProgress.setText("Progress: "+progress+"%");

        String baseUrl = "http://image.tmdb.org/t/p/w185//";
        new DownloadImageTask((ImageView) findViewById(R.id.ivProgressCover)).execute(baseUrl + mTvShow.image);

        episodesCompletedList = mTvShow.getEpisodesCompletedList();

        setEpisodeTracker();

    }

    private void setEpisodeTracker() {

        // Loop over seasons
        for(int i = 0; i<episodesCompletedList.size(); i++) {
            int seasonLength = episodesCompletedList.get(i).size();

            // Add textview representing the season number
            TextView tv = new TextView(this);
            tv.setText("Season "+(i+1));// add +1 to start episodes at 1 instead of 0 in the views
            tv.setPadding(0,20,0,0);
            layout.addView(tv);

            // Loop over episodes in season
            for(int j = 0; j<seasonLength; j++) {
                boolean completed = episodesCompletedList.get(i).get(j);

                // Add Checbbox representing the episode number
                final CheckBox cb = new CheckBox(this);
                cb.setText("Episode "+(j+1));
                if(completed) {
                    cb.setChecked(true);
                }
                final int index_i = i;
                final int index_j = j;

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(""+index_i+" "+index_j);
                        // Create temp list to replace the old List
                        ArrayList<Boolean> newList = episodesCompletedList.get(index_i);
                        if(cb.isChecked())
                            newList.set(index_j, true);
                        else newList.set(index_j, false);

                        episodesCompletedList.set(index_i, newList);
                        mTvShow.setEpisodesCompletedList(episodesCompletedList);
                        int progress = getProgress();
                        tvProgress.setText("Progress: "+progress+"%");
                        writeToDB();
                    }
                });
                layout.addView(cb);
            }
        }
    }

    private void writeToDB() {
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows")
                .child(mTvShow.id).setValue(mTvShow);

    }

    private int getProgress() {
        int count = 0;
        ArrayList<ArrayList<Boolean>> episodesCompletedList = mTvShow.getEpisodesCompletedList();
        for(ArrayList<Boolean> season : episodesCompletedList) {
            for(boolean bool : season) {
                if (bool) count++;
            }
        }
        // calculate percentage completed, rounded down and casted to int
        return (int) Math.floor( ((double)count/(double)mTvShow.getNumOfEpisodes()) *100);
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

    public void removeFromWatchList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TvShowProgressActivity.this);

        builder.setTitle("Delete to-do item")
                .setMessage("Are you sure you want to delete this to-do item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows")
                                .child(mTvShow.id).removeValue();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
