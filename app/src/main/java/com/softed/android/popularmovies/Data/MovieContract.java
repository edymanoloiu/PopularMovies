package com.softed.android.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Edi on 22.01.2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.softed.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favoritemovies";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_IS_MOVIE = "isMovie";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}

