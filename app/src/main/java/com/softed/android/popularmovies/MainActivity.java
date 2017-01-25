package com.softed.android.popularmovies;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.softed.android.popularmovies.Adapters.FavoriteMoviesAdapter;
import com.softed.android.popularmovies.Adapters.MoviesAdapter;
import com.softed.android.popularmovies.Data.MovieContract;
import com.softed.android.popularmovies.Utilities.Movie;
import com.softed.android.popularmovies.Utilities.MovieSortEnums;
import com.softed.android.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private FavoriteMoviesAdapter favoriteMoviesAdapter;
    private List<Movie> moviesList;
    private MovieSortEnums.MovieSortType sortType = MovieSortEnums.MovieSortType.Now_playing;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        moviesList = new ArrayList<>();
        adapter = new MoviesAdapter(this, moviesList);

        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //set title
        setTitle(MovieSortEnums.sortTitles.get(sortType));

        //get popular movies from TMDB
        String query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.NOW_PLAYING_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
        loadMovies(query);

        //add recycler view scroll listener
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLastItemDisplaying(recyclerView) && sortType != MovieSortEnums.MovieSortType.Favorites) {
                    page++;
                    String query = null;

                    if (sortType == MovieSortEnums.MovieSortType.User_Rating)
                        query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.RATED_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                    else if (sortType == MovieSortEnums.MovieSortType.Most_Popular)
                        query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.POPULAR_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                    else if (sortType == MovieSortEnums.MovieSortType.Now_playing)
                        query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.NOW_PLAYING_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                    else if (sortType == MovieSortEnums.MovieSortType.Upcoming)
                        query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.UPCOMING_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                    loadMovies(query);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sortType == MovieSortEnums.MovieSortType.Favorites)
            favoriteMoviesAdapter.swapCursor(getAllFavoriteMovies());
        setTitle(MovieSortEnums.sortTitles.get(sortType));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        moviesList.clear();
        String query = null;

        switch (item.getItemId()) {
            case R.id.highest_rated_option:
                page = 1;
                query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.RATED_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                sortType = MovieSortEnums.MovieSortType.User_Rating;
                recyclerView.setAdapter(adapter);
                break;
            case R.id.most_popular_option:
                page = 1;
                query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.POPULAR_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                sortType = MovieSortEnums.MovieSortType.Most_Popular;
                recyclerView.setAdapter(adapter);
                break;
            case R.id.now_playing_option:
                page = 1;
                query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.NOW_PLAYING_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                sortType = MovieSortEnums.MovieSortType.Now_playing;
                recyclerView.setAdapter(adapter);
                break;
            case R.id.upcoming_option:
                page = 1;
                query = NetworkUtils.TMDB_BASE_URL + NetworkUtils.UPCOMING_SORT + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;
                sortType = MovieSortEnums.MovieSortType.Upcoming;
                recyclerView.setAdapter(adapter);
                break;
            case R.id.favorite_option:
                sortType = MovieSortEnums.MovieSortType.Favorites;
                setTitle(MovieSortEnums.sortTitles.get(sortType));
                favoriteMoviesAdapter = new FavoriteMoviesAdapter(this, getAllFavoriteMovies());
                recyclerView.setAdapter(favoriteMoviesAdapter);
                return true;
        }

        setTitle(MovieSortEnums.sortTitles.get(sortType));

        if (item.getItemId() != R.id.favorite_option)
            loadMovies(query);
        return true;
    }

    private void loadMovies(String query) {
        try {
            List<Movie> list = NetworkUtils.getMoviesList(NetworkUtils.buildUrl(query));
            for (Movie m : list) {
                moviesList.add(m);
            }
            //recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Cursor getAllFavoriteMovies() {
        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_TIMESTAMP + " DESC");
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}