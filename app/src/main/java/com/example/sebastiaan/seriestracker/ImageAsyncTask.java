package com.example.sebastiaan.seriestracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Asynctask to download images in the background
 * Source: https://stackoverflow.com/questions/5776851/load-image-from-url
 */
class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView ivTrackImage;

    ImageAsyncTask(ImageView image) {
        this.ivTrackImage = image;
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
        ivTrackImage.setImageBitmap(result);
    }
}