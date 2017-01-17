package com.example.android.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Edi on 16.01.2017.
 */

public class NetworkUtils {

    final static String API_KEY = "23de07bffacf8bae0d55a99863d916bb";

    final static String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    public String testURL = "https://api.themoviedb.org/3/movie/popular?api_key=23de07bffacf8bae0d55a99863d916bb&language=en-US&page=1";

    public static URL buildUrl(String tmdbQuery) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL);
//                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
//                .appendQueryParameter(PARAM_SORT, sortBy)
//                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
