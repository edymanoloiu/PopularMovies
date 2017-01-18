package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetails extends AppCompatActivity {

    TextView movieTitle;
    ImageView moviePoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieTitle = (TextView) findViewById(R.id.movie_details_title);
        moviePoster = (ImageView) findViewById(R.id.movie_details_poster);

        setTitle("SADD");
        movieTitle.setText("test");
        Glide.with(getBaseContext()).load("http://image.tmdb.org/t/p/w185/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg").into(moviePoster);
    }
}
