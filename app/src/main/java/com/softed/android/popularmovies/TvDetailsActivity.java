package com.softed.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softed.android.popularmovies.Adapters.SeasonsListAdapter;
import com.softed.android.popularmovies.Utilities.NetworkUtils;
import com.softed.android.popularmovies.Utilities.Season;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TvDetailsActivity extends AppCompatActivity {

    static String TVID;
    TextView tvTitleTextView;
    TextView tvReleaseDateTextView;
    TextView tvUserRatingDateTextView;
    TextView tvOverviewTextView;
    ImageView moviePosterTextView;
    ListView seasonsListView;
    Context mContext;

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
        Glide.with(getBaseContext()).load(posterURL).into(moviePosterTextView);
        tvTitleTextView.setText(title);
        tvReleaseDateTextView.setText(releaseDate);
        tvUserRatingDateTextView.setText(userRatings);
        tvOverviewTextView.setText(plot);

        //get seasons list
        String query = NetworkUtils.TMDB_TV_BASE_URL + tvID + "?" + NetworkUtils.API_KEY + "&language=en-US";
        List<Season> seasons = null;
        try {
            seasons = NetworkUtils.getSeasonList(NetworkUtils.buildUrl(query));
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
}
