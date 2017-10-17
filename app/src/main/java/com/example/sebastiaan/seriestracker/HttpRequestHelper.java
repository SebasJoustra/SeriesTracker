package com.example.sebastiaan.seriestracker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestHelper {

    private static final String TAG = "HttpRequestHelper";

    protected static synchronized String downloadFromServer(String... params) {
        System.out.println(params);
        String result = "";
        String query = params[0];
        String type = params[1];
        URL url = null;

        try {
            String API_KEY = "?api_key=38aed79e99f1cb2b40c4574db2b9e0eb";
            String path = "https://api.themoviedb.org/3/";
            switch(type) {
                case "search":
                    url = new URL(path + "search/tv" + API_KEY + "&language=en-US&page=1&query=" + query);
                    break;
                case "tv_id":
                    url = new URL(path + "tv/"+ query + API_KEY + "&language=en-US");
                    Log.d(TAG, "downloadFromServer: " + url.toString());
                    break;
                default:
                    break;
            }

        } catch (MalformedURLException e) {
                e.printStackTrace();
        }

        HttpURLConnection connect;
        if(url != null) {
            try {
                connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("GET");
                Integer responseCode = connect.getResponseCode();
                if(responseCode >= 200 && responseCode < 300) {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;

                    while((line = bReader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
