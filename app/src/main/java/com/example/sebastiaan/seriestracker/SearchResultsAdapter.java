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

/**
 * Adapter class to handle the listview with the tv-shows from the query
 */

class SearchResultsAdapter extends ArrayAdapter<TvShow>{

    SearchResultsAdapter(@NonNull Context context, TvShow[] shows) {
        super(context, R.layout.search_result_row, shows);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRow = inflater.inflate(R.layout.search_result_row, parent, false);

        // Initialize tv-show object
        TvShow listItem = getItem(position);

        // Initialize Views
        TextView tvShowName = customRow.findViewById(R.id.tvSearchRowName);
        TextView tvRating = customRow.findViewById(R.id.tvSearchRowRating);
        ImageView ivSeriesImage = customRow.findViewById(R.id.ivSearchRowImage);

        // Set View values to the corresponding tv-show values.
        assert listItem != null;
        tvShowName.setText(listItem.name);
        tvRating.setText(listItem.voteAvg);

        // Execute an asynctask to load the images.
        String imgUrl = listItem.image;
        new ImageAsyncTask(ivSeriesImage).execute("http://image.tmdb.org/t/p/w185//" + imgUrl);

        return customRow;
    }
}
