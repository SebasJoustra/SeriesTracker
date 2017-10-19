package com.example.sebastiaan.seriestracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter class to handle the listview with the tv-shows from the series-tracker
 */

class WatchListAdapter extends ArrayAdapter<TvShow> {

    WatchListAdapter(@NonNull Context context, ArrayList<TvShow> shows) {
        super(context, R.layout.watchlist_row, shows);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRow = inflater.inflate(R.layout.watchlist_row, parent, false);

        // Initialize tv-show object
        TvShow listItem = getItem(position);

        // Initialize Views
        TextView tvShowName = customRow.findViewById(R.id.tvWatchListRowName);
        ImageView ivSeriesImage = customRow.findViewById(R.id.ivWatchListRowImage);
        TextView tvProgress = customRow.findViewById(R.id.tvWatchListRowProgress);

        // Set View values to the corresponding tv-show values.
        assert listItem != null;
        tvShowName.setText(listItem.name);
        tvProgress.setText("Progress: " + getProgress(listItem) + "%");

        // Execute an asynctask to load the images.
        String imgUrl = listItem.image;
        new ImageAsyncTask(ivSeriesImage).execute("http://image.tmdb.org/t/p/w185//" + imgUrl);

        return customRow;
    }

    // Calculate the progress of the amount of episodes watched of a tv-show
    private int getProgress(TvShow listItem) {
        int count = 0;
        ArrayList<ArrayList<Boolean>> episodesCompletedList = listItem.getEpisodesCompletedList();
        for(ArrayList<Boolean> season : episodesCompletedList) {
            for(boolean bool : season) {
                if (bool) count++;
            }
        }
        // calculate percentage completed, rounded down and casted to int
        return (int) Math.floor( ((double)count/(double)listItem.getNumOfEpisodes()) *100);
    }
}
