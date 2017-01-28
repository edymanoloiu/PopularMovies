package com.softed.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TvDetailsActivity extends AppCompatActivity {

    static String MOVIEID;
    TextView tvTitleTextView;
    TextView tvReleaseDateTextView;
    TextView tvUserRatingDateTextView;
    TextView tvOverviewTextView;
    ImageView moviePosterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);

        tvTitleTextView = (TextView) findViewById(R.id.tv_details_title);
        tvReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        tvUserRatingDateTextView = (TextView) findViewById(R.id.tv_user_ratings);
        moviePosterTextView = (ImageView) findViewById(R.id.tv_details_poster);
        tvOverviewTextView = (TextView) findViewById(R.id.tv_overview);

        //get intent information
        final String title = getIntent().getStringExtra("title");
        String posterURL = getIntent().getStringExtra("posterPath");
        String plot = getIntent().getStringExtra("plot");
        String releaseDate = "First aired: " + getIntent().getStringExtra("releaseDate");
        String userRatings = "User rating: " + getIntent().getStringExtra("userRating");
        final String movieID = getIntent().getStringExtra("ID");
        MOVIEID = movieID;

        //populate the UI
        setTitle(title);
        Glide.with(getBaseContext()).load(posterURL).into(moviePosterTextView);
        tvTitleTextView.setText(title);
        tvReleaseDateTextView.setText(releaseDate);
        tvUserRatingDateTextView.setText(userRatings);
        tvOverviewTextView.setText(plot);
    }
}
