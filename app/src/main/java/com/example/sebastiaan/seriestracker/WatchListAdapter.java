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

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class WatchListAdapter extends ArrayAdapter<TvShow> {

    public WatchListAdapter(@NonNull Context context, ArrayList<TvShow> shows) {
        super(context, R.layout.watchlist_row, shows);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRow = inflater.inflate(R.layout.watchlist_row, parent, false);

        TvShow listItem = getItem(position);

        TextView tvShowName = customRow.findViewById(R.id.tvWatchListRowName);
        ImageView ivSeriesImage = customRow.findViewById(R.id.ivWatchListRowImage);
        TextView tvProgress = customRow.findViewById(R.id.tvWatchListRowProgress);

        tvShowName.setText(listItem.name);
        String imgUrl = listItem.image;
        String baseUrl = "http://image.tmdb.org/t/p/w185//";
        new ImageAsyncTask(ivSeriesImage).execute(baseUrl + imgUrl);

        int progress = getProgress(listItem);
        tvProgress.setText("Progress: "+progress+"%");

        return customRow;
    }

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
