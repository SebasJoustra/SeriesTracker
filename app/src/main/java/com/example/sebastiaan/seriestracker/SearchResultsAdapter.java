package com.example.sebastiaan.seriestracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResultsAdapter extends ArrayAdapter<TvShow>{

    private String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    public SearchResultsAdapter(@NonNull Context context, TvShow[] shows) {
        super(context, R.layout.search_result_row, shows);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRow = inflater.inflate(R.layout.search_result_row, parent, false);

        TvShow listItem = getItem(position);

        TextView tvShowName = (TextView) customRow.findViewById(R.id.tvSearchRowName);
        TextView tvRating = (TextView) customRow.findViewById(R.id.tvSearchRowRating);
        ImageView ivSeriesImage = (ImageView) customRow.findViewById(R.id.ivSearchRowImage);

        tvShowName.setText(listItem.name);
        tvRating.setText(listItem.voteAvg);

        String imgUrl = listItem.image;

        new ImageAsyncTask(ivSeriesImage).execute(BASE_URL + imgUrl);


        return customRow;
    }
}
