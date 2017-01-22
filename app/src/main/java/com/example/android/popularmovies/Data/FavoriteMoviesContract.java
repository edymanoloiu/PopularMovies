package com.example.android.popularmovies.Data;

import android.provider.BaseColumns;

/**
 * Created by Edi on 22.01.2017.
 */

public class FavoriteMoviesContract {
    private FavoriteMoviesContract() {
    }

    public static final class FavoriteMoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favoritemovies";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
