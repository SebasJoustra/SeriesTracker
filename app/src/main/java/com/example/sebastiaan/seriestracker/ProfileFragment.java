package com.example.sebastiaan.seriestracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Fragment which handles the user profile
 */

public class ProfileFragment extends Fragment {

    TextView tvEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your profile");

        // Initialize View
        tvEmail = rootView.findViewById(R.id.tvEmail);

        // Connect to database, and display the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            tvEmail.setText(email);
        }

        return rootView;
    }
}
