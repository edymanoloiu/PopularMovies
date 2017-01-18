package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetails extends AppCompatActivity {

    TextView movieTitleTextView;
    TextView releaseDateTextView;
    TextView userRatingDateTextView;
    ImageView moviePosterTextView;


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

        setTitle(title);
        movieTitleTextView.setText(plot);
        releaseDateTextView.setText(releaseDate);
        userRatingDateTextView.setText(userRatings);
        Glide.with(getBaseContext()).load(posterURL).into(moviePosterTextView);
    }
}
