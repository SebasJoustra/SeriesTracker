package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity to create a new user account.
 */

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    private FirebaseAuth mAuth;
    
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Connect to database
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        etEmail = (EditText) findViewById(R.id.etCreateEmail);
        etPassword = (EditText) findViewById(R.id.etCreatePassword);
    }

    public void createAccount(View view) {
        // User clicked button to create account
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            // Creating account was unsuccessful, show to user
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed, user not created",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Creating account was successful, show to user and log in
                            Toast.makeText(CreateAccountActivity.this, "Created user, trying to log in",
                                    Toast.LENGTH_SHORT).show();
                            logInUser(new View(getApplicationContext()));
                        }

                        // ...
                    }
                });
    }
    public void logInUser(View view) {
        // User clicked button to create account gets logged in automatically
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            // Login was unsuccessful, show to user
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                        }else {
                            // Login was successful, show to user and redirect to Main Activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
