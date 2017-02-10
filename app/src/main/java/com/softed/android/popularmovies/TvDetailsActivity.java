package com.softed.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softed.android.popularmovies.Adapters.SeasonsListAdapter;
import com.softed.android.popularmovies.Data.MovieContract;
import com.softed.android.popularmovies.Utilities.Movie;
import com.softed.android.popularmovies.Utilities.NetworkUtils;
import com.softed.android.popularmovies.Utilities.Season;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TvDetailsActivity extends AppCompatActivity {

    private static String TVID;
    private TextView tvTitleTextView;
    private TextView tvReleaseDateTextView;
    private TextView tvUserRatingDateTextView;
    private TextView tvOverviewTextView;
    private ImageView moviePosterTextView;
    private ListView seasonsListView;
    private Context mContext;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);

        mContext = getApplicationContext();

        tvTitleTextView = (TextView) findViewById(R.id.tv_details_title);
        tvReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        tvUserRatingDateTextView = (TextView) findViewById(R.id.tv_user_ratings);
        moviePosterTextView = (ImageView) findViewById(R.id.tv_details_poster);
        tvOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        seasonsListView = (ListView) findViewById(R.id.tv_seasons_list);

        //get intent information
        final String title = getIntent().getStringExtra("title");
        String posterURL = getIntent().getStringExtra("posterPath");
        String plot = getIntent().getStringExtra("plot");
        String releaseDate = "First aired: " + getIntent().getStringExtra("releaseDate");
        String userRatings = "User rating: " + getIntent().getStringExtra("userRating");
        final String tvID = getIntent().getStringExtra("ID");
        TVID = tvID;

        //populate the UI
        setTitle(title);
        Glide.with(getBaseContext()).load(posterURL).error(R.mipmap.image_not_found).into(moviePosterTextView);
        tvTitleTextView.setText(title);
        tvReleaseDateTextView.setText(releaseDate);
        tvUserRatingDateTextView.setText(userRatings);
        tvOverviewTextView.setText(plot);

        //get seasons list
        String query = NetworkUtils.TMDB_TV_BASE_URL + tvID + "?" + NetworkUtils.API_KEY + "&language=en-US";
        List<Season> seasons = null;
        try {
            seasons = NetworkUtils.getSeasonList(NetworkUtils.buildUrl(query));
            TextView endDate = (TextView) findViewById(R.id.last_episode_air_time_text_view);
            endDate.setText("Last aired: " + seasons.get(0).getAirDate());
            seasons.remove(0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //populate trailer view
        seasonsListView.setAdapter(new SeasonsListAdapter(this, seasons));

        //set season scroll event event
        seasonsListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });

        //set on item click listener
        final List<Season> finalSeasons = seasons;
        seasonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, SeasonDetailsActivity.class);
                intent.putExtra("ID", tvID);
                intent.putExtra("season", finalSeasons.get(i).getNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_view_menu, menu);

        this.menu = menu;
        MenuItem settingsItem = menu.findItem(R.id.action_favorite);

        if (checkIfMoviesIsFavorite(TVID) < 0)
            settingsItem.setIcon(getResources().getDrawable(R.drawable.favorite_image));
        else
            settingsItem.setIcon(getResources().getDrawable(R.drawable.full_favorite_icon));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String posterURL = getIntent().getStringExtra("posterPath");
        String movieID = getIntent().getStringExtra("ID");
        String userRatings = getIntent().getStringExtra("userRating");
        String title = getIntent().getStringExtra("title");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        String plot = getIntent().getStringExtra("plot");

        Cursor cursor = getAllFavoriteTVs();
        int cursorPosition = checkIfMoviesIsFavorite(movieID);

        if (cursorPosition < 0) {
            Movie movie = new Movie();
            movie.setName(title);
            movie.setID(movieID);
            movie.setPosterURL(posterURL);
            movie.setUserRating(userRatings);
            movie.setReleaseDate(releaseDate);
            movie.setPlot(plot);

            //add movie to the DB
            addNewMovie(movie);

            Toast.makeText(getBaseContext(), "Added " + title + " to favorites list", Toast.LENGTH_SHORT).show();
            MenuItem settingsItem = menu.findItem(R.id.action_favorite);
            settingsItem.setIcon(getResources().getDrawable(R.drawable.full_favorite_icon));
        } else {
            cursor.moveToPosition(cursorPosition);
            removeMovie(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            Toast.makeText(getBaseContext(), "Removed " + title + " to favorites list", Toast.LENGTH_SHORT).show();
            MenuItem settingsItem = menu.findItem(R.id.action_favorite);
            settingsItem.setIcon(getResources().getDrawable(R.drawable.favorite_image));
        }

        return true;
    }


    private void addNewMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getName());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getID());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterURL());
        cv.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getUserRating());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_PLOT, movie.getPlot());
        cv.put(MovieContract.MovieEntry.COLUMN_IS_MOVIE, "0");
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
    }

    private Cursor getAllFavoriteTVs() {
        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "isMovie = 0", null, MovieContract.MovieEntry.COLUMN_TIMESTAMP + " DESC");
    }

    private void removeMovie(long id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id + "").build();
        getContentResolver().delete(uri, null, null);
    }

    private int checkIfMoviesIsFavorite(String movieID) {
        Cursor cursor = getAllFavoriteTVs();
        boolean isFavorite = false;
        int cursorPosition = -1;
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)).equals(movieID)) {
                    isFavorite = true;
                    cursorPosition = i;
                    break;
                }
            }
        }
        if (isFavorite)
            return cursorPosition;
        else
            return -1;
    }
}
