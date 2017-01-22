package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.Adapters.TrailerListAdapter;
import com.example.android.popularmovies.Data.MovieContract;
import com.example.android.popularmovies.Utilities.Movie;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.Utilities.Review;
import com.example.android.popularmovies.Utilities.Trailer;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView movieTitleTextView;
    TextView releaseDateTextView;
    TextView userRatingDateTextView;
    ImageView moviePosterTextView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieTitleTextView = (TextView) findViewById(R.id.movie_details_title);
        releaseDateTextView = (TextView) findViewById(R.id.movie_details_release_date);
        userRatingDateTextView = (TextView) findViewById(R.id.movie_details_user_ratings);
        moviePosterTextView = (ImageView) findViewById(R.id.movie_details_poster);

        //get intent information
        final String title = getIntent().getStringExtra("title");
        String posterURL = getIntent().getStringExtra("posterPath");
        String plot = getIntent().getStringExtra("plot");
        String releaseDate = "Release date: " + getIntent().getStringExtra("releaseDate");
        String userRatings = "User rating: " + getIntent().getStringExtra("userRating");
        final String movieID = getIntent().getStringExtra("ID");

        setTitle(title);
        movieTitleTextView.setText(plot);
        releaseDateTextView.setText(releaseDate);
        userRatingDateTextView.setText(userRatings);
        Glide.with(getBaseContext()).load(posterURL).into(moviePosterTextView);

        //get trailer list fro current movie
        String query = NetworkUtils.TMDB_BASE_URL + movieID + "/videos?" + NetworkUtils.API_KEY + "&language=en-US";
        List<Trailer> trailerList = null;
        try {
            trailerList = NetworkUtils.getTrailersList(NetworkUtils.buildUrl(query));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //populate trailer view
        listView = (ListView) findViewById(R.id.trailer_list);
        LinkedList<String> trailerNames = new LinkedList<>();
        for (Trailer t : trailerList)
            trailerNames.add(t.getName());
        listView.setAdapter(new TrailerListAdapter(this, trailerNames.toArray(new String[trailerList.size()])));

        //create on click listener for list view
        final List<Trailer> finalTrailerList = trailerList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + finalTrailerList.get(i).getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + finalTrailerList.get(i).getKey()));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        //create on click listener for the review button
        Button reviewButton = (Button) findViewById(R.id.show_reviews_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if there are any reviews to show
                String query = NetworkUtils.TMDB_BASE_URL + movieID + "/reviews?" + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + "1";
                List<Review> reviews = null;
                try {
                    reviews = NetworkUtils.getReviewsList(NetworkUtils.buildUrl(query));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!reviews.isEmpty()) {
                    Intent intent = new Intent(view.getContext(), ReviewsActivity.class);
                    intent.putExtra("ID", movieID);
                    intent.putExtra("title", title);
                    view.getContext().startActivity(intent);
                } else
                    Toast.makeText(getBaseContext(), "There are no reviews for this movies", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_view_menu, menu);
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

        Cursor cursor = getAllFavoriteMovies();
        boolean isFavorite = false;
        int cursorPosition = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)).equals(movieID)) {
                isFavorite = true;
                cursorPosition = i;
                break;
            }
        }
        if (!isFavorite) {
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
        } else {
            cursor.moveToPosition(cursorPosition);
            removeMovie(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            Toast.makeText(getBaseContext(), "Removed " + title + " to favorites list", Toast.LENGTH_SHORT).show();
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

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
    }

    private Cursor getAllFavoriteMovies() {
        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_TIMESTAMP);
    }

    private void removeMovie(long id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id + "").build();
        getContentResolver().delete(uri, null, null);
    }
}
