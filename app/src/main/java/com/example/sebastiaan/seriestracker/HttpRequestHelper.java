package com.example.sebastiaan.seriestracker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * This class functions as a helper class to make a HTTP connection with the API and retrieve the
 * JSON result.
 */

class HttpRequestHelper {

    private static final String TAG = "HttpRequestHelper";

    static synchronized String downloadFromServer(String... params) {

        // The query parameter represents either the tv-show name given by the user, or the direct
        // ID requested from the application
        String query = params[0];

        // The type parameter will determine in what part of the api we're gonna look
        String type = params[1];

        // Get the URL of the API which will return the correct JSON
        URL url = getUrl(query, type);

        return getJSON(url);
    }

    // Make a HTTP connection to retrieve the JSON file.
    private static String getJSON(URL url) {
        StringBuilder result = new StringBuilder();
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
                        //result += line;
                        result.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    // Get the URL of the API which will return the correct JSON
    private static URL getUrl(String query, String type) {
        URL url = null;
        try {
            String API_KEY = "?api_key=38aed79e99f1cb2b40c4574db2b9e0eb";
            String path = "https://api.themoviedb.org/3/";
            switch(type) {
                // Search for series based on the query
                case "search":
                    url = new URL(path + "search/tv" + API_KEY + "&language=en-US&page=1&query=" + query);
                    break;
                // Find a specific series based on the given ID (query)
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
        return url;
    }

}
