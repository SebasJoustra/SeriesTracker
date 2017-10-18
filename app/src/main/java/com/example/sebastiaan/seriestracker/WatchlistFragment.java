package com.example.sebastiaan.seriestracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    private static final String TAG = "WatchlistFragment";
    Activity act;
    ListView lvWatchList;
    ArrayList<TvShow> tvShowArray = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private ListAdapter mListAdapter;

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("tvShows");
        } else {
            Intent intent = new Intent(act, LoginActivity.class);
            startActivity(intent);
            act.finish();
        }
        lvWatchList = rootView.findViewById(R.id.lvWatchlist);
        addDBchangeListener();
        lvWatchList.setOnItemClickListener(new ItemClickListener());

        return rootView;
    }

    public class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
            Intent intent = new Intent(act, TvShowProgressActivity.class);
            intent.putExtra("tvShow", tvShowArray.get(itemIndex));
            startActivity(intent);
        }
    }

    private void addDBchangeListener() {
        ValueEventListener seriesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                tvShowArray = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    tvShowArray.add(data.getValue(TvShow.class));
                }
                if(lvWatchList == null) {
                    lvWatchList = act.findViewById(R.id.lvWatchlist);
                }

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
