package com.softed.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EpisodeDetailsActivity extends AppCompatActivity {

    TextView episodeReleaseDateTextView;
    TextView episodeOverviewTextView;
    TextView episodeRatingTextView;
    ImageView episodePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);

        episodeReleaseDateTextView = (TextView) findViewById(R.id.episode_release_date);
        episodePosterImageView = (ImageView) findViewById(R.id.episode_details_poster);
        episodeOverviewTextView = (TextView) findViewById(R.id.episode_overview);
        episodeRatingTextView = (TextView) findViewById(R.id.episode_rating);

        //get intent info
        String name = getIntent().getStringExtra("name");
        String overview = getIntent().getStringExtra("overview");
        String releaseDate = "Air date: " + getIntent().getStringExtra("release");
        String rating = "User ratings: " + getIntent().getStringExtra("rating");
        String posterPath = getIntent().getStringExtra("poster");

        setTitle(name);
        episodeOverviewTextView.setText(overview);
        episodeRatingTextView.setText(rating);
        if (!releaseDate.equals("Air date: null"))
            episodeReleaseDateTextView.setText(releaseDate);
        Glide.with(getBaseContext()).load(posterPath).into(episodePosterImageView);
    }
}
