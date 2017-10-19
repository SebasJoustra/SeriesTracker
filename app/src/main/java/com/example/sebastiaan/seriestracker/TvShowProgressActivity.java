package com.example.sebastiaan.seriestracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

/**
 * This activity will show the user a tv-show from their tracker-list
 */

public class TvShowProgressActivity extends AppCompatActivity {

    TextView tvProgress;
    TextView tvDescription;
    LinearLayout layout;
    ImageView ivCoverImage;

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

        // Get information from the previous Activity (fragment WatchListFragment)
        Intent intent = getIntent();
        mTvShow = (TvShow) intent.getSerializableExtra("tvShow");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Set toolbar title to the show name
        setTitle(mTvShow.name);

        // Initialize Views
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvDescription = (TextView) findViewById(R.id.tvProgressDescription);
        ivCoverImage = (ImageView) findViewById(R.id.ivProgressCover);
        layout = (LinearLayout) findViewById(R.id.llShow);

        // Set Views
        setViews();

        // Set episode tracker
        setEpisodeTracker();
    }

    // Set the Views based on the object information
    private void setViews() {
        tvDescription.setText(mTvShow.description);
        int progress = getProgress();
        tvProgress.setText("Progress: "+progress+"%");

        // Download the cover image in an asyntask
        new ImageAsyncTask(ivCoverImage).execute("http://image.tmdb.org/t/p/w185//" + mTvShow.image);

        episodesCompletedList = mTvShow.getEpisodesCompletedList();
    }

    private void setEpisodeTracker() {
        // Loop over seasons
        for(int i = 0; i<episodesCompletedList.size(); i++) {
            int seasonLength = episodesCompletedList.get(i).size();

            // Add textview representing the season number
            setSeasonTextView(i);

            // Loop over episodes in season
            for(int j = 0; j<seasonLength; j++) {

                // Add Checbbox representing the episode number
                addCheckbox(i, j);
            }
        }
    }

    // Add textview representing the season number
    private void setSeasonTextView(int season_number) {
        TextView tv = new TextView(this);
        tv.setText("Season "+(season_number+1));// add +1 to start episodes at 1 instead of 0 in the views
        tv.setPadding(0,20,0,0);
        layout.addView(tv);
    }

    // Add Checbbox representing the episode number and whether it is completed or not
    private void addCheckbox(int season_num, int episode_num) {
        boolean completed = episodesCompletedList.get(season_num).get(episode_num);
        CheckBox cb = new CheckBox(this);

        // Set checkbox text and check accordingly (default is unchecked)
        cb.setText("Episode "+(episode_num+1));
        if(completed) {
            cb.setChecked(true);
        }

        // Set onclick-listener to the checkbox
        setListenerToCheckBox(cb, season_num, episode_num);

        // Add checkbox to the corresponding Layout
        layout.addView(cb);
    }

    // Set onclick-listener to the checkbox. When clicked, write to database
    private void setListenerToCheckBox(final CheckBox cb, final int season_num, final int episode_num) {
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create temp list to replace the old List
                ArrayList<Boolean> newList = episodesCompletedList.get(season_num);

                // If user clicked the checkbox, change in the list,
                if(cb.isChecked())
                    newList.set(episode_num, true);
                else newList.set(episode_num, false);

                // Set the temp list into the list of lists, replacing the old one
                episodesCompletedList.set(season_num, newList);

                // Set this new list to the Tv-show object, as well as the database
                mTvShow.setEpisodesCompletedList(episodesCompletedList);
                writeToDB();

                // Update the progress percentage in the View
                int progress = getProgress();
                tvProgress.setText("Progress: "+progress+"%");
            }
        });
    }

    // Write new information to the database
    private void writeToDB() {
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows")
                .child(mTvShow.id).setValue(mTvShow);
    }

    // Calculate the progress of the amount of episodes watched of a tv-show
    private int getProgress() {
        int count = 0;
        ArrayList<ArrayList<Boolean>> episodesCompletedList = mTvShow.getEpisodesCompletedList();
        for(ArrayList<Boolean> season : episodesCompletedList) {
            for(boolean bool : season) {
                if (bool) count++;
            }
        }
        // Calculate percentage completed, rounded down and casted to int
        return (int) Math.floor( ((double)count/(double)mTvShow.getNumOfEpisodes()) *100);
    }

    // Delete item from watch-list with a confirmation dialog.
    public void removeFromWatchList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TvShowProgressActivity.this);

        builder.setTitle("Delete tv show")
                .setMessage("Are you sure you want to delete this tv show?")
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
