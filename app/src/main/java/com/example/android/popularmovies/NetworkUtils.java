package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by Edi on 16.01.2017.
 */

public class NetworkUtils {

    final static String API_KEY = "api_key=23de07bffacf8bae0d55a99863d916bb";

    final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    final static String POPULAR_SORT = "popular?";

    final static String RATED_SORT = "top_rated?";

    final static String QUERY_END = "&language=en-US&page=";

    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public static URL buildUrl(String tmdbQuery) {
        Uri builtUri = Uri.parse(tmdbQuery);

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

    public static List<Movie> getMoviesList(URL url) throws IOException, JSONException, ExecutionException, InterruptedException {
        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("results");

        List<Movie> movies = new LinkedList<>();
        Movie movie = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            movie = new Movie();
            movie.setName(jsonobject.getString("title"));
            movie.setPlot(jsonobject.getString("overview"));
            movie.setPosterURL(IMAGE_BASE_URL + jsonobject.getString("poster_path").substring(1));
            movie.setUserRating(jsonobject.getString("popularity"));
            movie.setReleaseDate(jsonobject.getString("release_date"));
            movies.add(movie);
        }

        return movies;
    }

    private static class TMDBQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String tmdbSearchResults = null;
            try {
                tmdbSearchResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmdbSearchResults;
        }
    }
}
