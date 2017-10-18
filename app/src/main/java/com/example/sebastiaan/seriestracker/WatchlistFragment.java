package com.example.sebastiaan.seriestracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class WatchlistFragment extends Fragment {

    private static final String TAG = "WatchlistFragment";
    Activity act;
    ListView lvWatchList;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_watchlist, container, false);

        act = getActivity();

        lvWatchList = act.findViewById(R.id.lvWatchlist);

        return rootView;
    }
}
