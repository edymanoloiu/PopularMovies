package com.softed.android.popularmovies.Utilities;

import java.util.TreeMap;

/**
 * Created by Edi on 21.01.2017.
 */

public class MovieSortEnums {

    public static TreeMap<MovieSortType, String> sortTitlesMovies = new TreeMap<MovieSortType, String>() {{
        put(MovieSortType.User_Rating, "Highest rated");
        put(MovieSortType.Most_Popular, "Most popular");
        put(MovieSortType.Now_playing, "Now playing");
        put(MovieSortType.Upcoming, "Upcoming");
        put(MovieSortType.Favorites, "Favorites movies");
    }};

    public static TreeMap<MovieSortType, String> sortTitlesTv = new TreeMap<MovieSortType, String>() {{
        put(MovieSortType.User_Rating, "Highest rated");
        put(MovieSortType.Most_Popular, "Most popular");
        put(MovieSortType.Now_playing, "Airing today");
        put(MovieSortType.Upcoming, "On the air");
        put(MovieSortType.Favorites, "Favorites TV series");
    }};

    public enum MovieSortType {
        User_Rating,
        Most_Popular,
        Now_playing,
        Upcoming,
        Favorites
    }
}
