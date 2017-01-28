package com.softed.android.popularmovies.Utilities;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by Edi on 16.01.2017.
 */

public class NetworkUtils {

    public final static String API_KEY = "api_key=23de07bffacf8bae0d55a99863d916bb";

    public final static String TMDB_MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public final static String TMDB_TV_BASE_URL = "https://api.themoviedb.org/3/tv/";

    public final static String POPULAR_SORT = "popular?";

    public final static String RATED_SORT = "top_rated?";

    public final static String NOW_PLAYING_SORT = "now_playing?";

    public final static String UPCOMING_SORT = "upcoming?";

    public final static String QUERY_END = "&language=en-US&page=";

    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public final static String AIRING_TODAY = "airing_today?";

    public final static String ON_THE_AIR = "on_the_air?";

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
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            movie = new Movie();
            movie.setName(jsonobject.getString("original_title"));
            movie.setPlot(jsonobject.getString("overview"));
            movie.setPosterURL(IMAGE_BASE_URL + jsonobject.getString("poster_path").substring(1));
            movie.setUserRating(jsonobject.getString("vote_average"));
            movie.setReleaseDate(jsonobject.getString("release_date"));
            movie.setID(jsonobject.getString("id"));
            movie.setIsMovie(true);
            movies.add(movie);
        }

        return movies;
    }

    public static List<Movie> getTVList(URL url) throws ExecutionException, InterruptedException, JSONException {
        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("results");

        List<Movie> movies = new LinkedList<>();
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            movie = new Movie();
            movie.setName(jsonobject.getString("original_name"));
            movie.setPlot(jsonobject.getString("overview"));
            movie.setPosterURL(IMAGE_BASE_URL + jsonobject.getString("poster_path").substring(1));
            movie.setUserRating(jsonobject.getString("vote_average"));
            movie.setReleaseDate(jsonobject.getString("first_air_date"));
            movie.setID(jsonobject.getString("id"));
            movie.setIsMovie(false);
            movies.add(movie);
        }

        return movies;
    }

    public static List<Trailer> getTrailersList(URL url) throws ExecutionException, InterruptedException, JSONException {
        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("results");
        List<Trailer> trailers = new LinkedList<>();
        Trailer trailer = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            trailer = new Trailer();
            trailer.setID(jsonobject.getString("id"));
            trailer.setType(jsonobject.getString("type"));
            trailer.setSize(jsonobject.getInt("size"));
            trailer.setName(jsonobject.getString("name"));
            trailer.setKey(jsonobject.getString("key"));
            trailers.add(trailer);
        }
        return trailers;
    }

    public static List<Review> getReviewsList(URL url) throws ExecutionException, InterruptedException, JSONException {
        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("results");
        List<Review> reviews = new LinkedList<>();
        Review review;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            review = new Review();
            review.setId(jsonobject.getString("id"));
            review.setAuthor(jsonobject.getString("author"));
            review.setContent(jsonobject.getString("content"));
            review.setUrl(jsonobject.getString("url"));
            reviews.add(review);
        }
        return reviews;
    }

    public static List<Season> getSeasonList(URL url) throws ExecutionException, InterruptedException, JSONException {
        List<Season> seasons = new LinkedList<>();

        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("seasons");

        Season season;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            season = new Season();
            season.setID(jsonObject.getString("id"));
            season.setNumber(jsonObject.getInt("season_number"));
            season.setEpisodesCount(jsonObject.getInt("episode_count"));
            season.setAirDate(jsonObject.getString("air_date"));
            seasons.add(season);
        }

        Collections.sort(seasons, new Comparator<Season>() {
            public int compare(Season emp1, Season emp2) {
                return emp1.getNumber() - (emp2.getNumber());
            }
        });

        return seasons;
    }

    public static Season getSeason(URL url) throws ExecutionException, InterruptedException, JSONException {
        Season season = new Season();

        String jsonResponse = new TMDBQueryTask().execute(url).get();
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray jsonArray = object.getJSONArray("episodes");

        season.setID(object.getString("id"));
        season.setPosterPath(IMAGE_BASE_URL + object.getString("poster_path").substring(1));
        season.setName(object.getString("name"));
        season.setOverview(object.getString("overview"));
        season.setAirDate(object.getString("air_date"));

        List<Episode> episodes = new LinkedList<>();
        Episode episode;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            episode = new Episode();
            episode.setID(jsonObject.getString("id"));
            episode.setOverview(jsonObject.getString("overview"));
            episode.setName(jsonObject.getString("name"));
            episode.setPosterPath(IMAGE_BASE_URL + jsonObject.getString("still_path").substring(1));
            episode.setAir_date(jsonObject.getString("air_date"));
            episode.setRating(jsonObject.getString("vote_average"));
            episodes.add(episode);
        }

        season.setEpisodeList(episodes);

        return season;
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
