package com.example.sebastiaan.seriestracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Fragment which handles the series-tracker watch list
 */

public class WatchlistFragment extends Fragment {

    private static final String TAG = "WatchlistFragment";

    Activity act;
    ListView lvWatchList;
    ArrayList<TvShow> tvShowArray = new ArrayList<>();

    private DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth;

    private ListAdapter mListAdapter;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Gets called when fragmentview gets opened
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_watchlist, container, false);
        act = getActivity();

        // Set actionBar title
        ((AppCompatActivity) act ).getSupportActionBar().setTitle("Series Tracker");

        // Get database information and set listener
        getDatabaseInformation();

        // Set Listview and its listener
        lvWatchList = rootView.findViewById(R.id.lvWatchlist);
        TextView emptyText = rootView.findViewById(R.id.emptyText);
        lvWatchList.setEmptyView(emptyText);
        addDBchangeListener();
        lvWatchList.setOnItemClickListener(new ItemClickListener());

        return rootView;
    }

    // Get database information. Only allowed when the user is logged in.
    private void getDatabaseInformation() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows");
        } else {
            Intent intent = new Intent(act, LoginActivity.class);
            startActivity(intent);
            act.finish();
        }
    }

    // Onclick listener for the listview items
    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
            Intent intent = new Intent(act, TvShowProgressActivity.class);
            intent.putExtra("tvShow", tvShowArray.get(itemIndex));
            startActivity(intent);
        }
    }

    // Get the user watch-list from the database by adding a listener to the database
    private void addDBchangeListener() {
        ValueEventListener seriesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                tvShowArray = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    tvShowArray.add(data.getValue(TvShow.class));
                }

                // The listener would sometimes get called when the listview is (still) empty,
                // add a null-checker to prevent this.
                if(lvWatchList == null) {
                    lvWatchList = act.findViewById(R.id.lvWatchlist);
                }

                // Set adapter
                mListAdapter = new WatchListAdapter(act, tvShowArray);
                lvWatchList.setAdapter(mListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseRef.addValueEventListener(seriesListener);
    }
}
