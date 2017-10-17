package com.example.sebastiaan.seriestracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class TvShowResultActivity extends AppCompatActivity {

    private static final String TAG = "TvShowResultActivity";

    TvShow tvShow;
    TextView tvName;
    TextView tvDescription;

    String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_result);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        tvShow = (TvShow) bundle.getSerializable("tvShow");

        setComponents();

    }

    private void setComponents() {
        tvName = (TextView) findViewById(R.id.tvTvShowName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvName.setText(tvShow.name);
        tvDescription.setText(tvShow.description);

        new DownloadImageTask((ImageView) findViewById(R.id.ivCoverImage)).execute(BASE_URL + tvShow.image);
    }

    public void addToWatchList(View view) {
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child(tvShow.id).setValue(tvShow);
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.addTvShow(tvShow);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // Source: https://stackoverflow.com/questions/5776851/load-image-from-url
        private ImageView ivCoverImage;


        public DownloadImageTask(ImageView image) {
            this.ivCoverImage = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmpResult = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmpResult = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmpResult;
        }

        protected void onPostExecute(Bitmap result) {
            ivCoverImage.setImageBitmap(result);
        }
    }
}
