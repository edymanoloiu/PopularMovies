package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.Adapters.TrailerListAdapter;
import com.example.android.popularmovies.Utilities.NetworkUtils;
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
        String title = getIntent().getStringExtra("title");
        String posterURL = getIntent().getStringExtra("posterPath");
        String plot = getIntent().getStringExtra("plot");
        String releaseDate = "Release date: " + getIntent().getStringExtra("releaseDate");
        String userRatings = "User rating: " + getIntent().getStringExtra("userRating");
        String movieID = getIntent().getStringExtra("ID");

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getBaseContext(), "Favorite presses", Toast.LENGTH_SHORT).show();
        return true;
    }
}
