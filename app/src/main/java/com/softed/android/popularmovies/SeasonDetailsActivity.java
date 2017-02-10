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
import com.softed.android.popularmovies.Adapters.EpisodesListAdapter;
import com.softed.android.popularmovies.Utilities.NetworkUtils;
import com.softed.android.popularmovies.Utilities.Season;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class SeasonDetailsActivity extends AppCompatActivity {

    static String SEASONID;
    TextView seasonReleaseDateTextView;
    TextView seasonOverviewTextView;
    ImageView seasonPosterImageView;
    ListView episodesListView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_details);

        mContext = getApplicationContext();

        seasonReleaseDateTextView = (TextView) findViewById(R.id.season_release_date);
        seasonPosterImageView = (ImageView) findViewById(R.id.season_details_poster);
        seasonOverviewTextView = (TextView) findViewById(R.id.season_overview);
        episodesListView = (ListView) findViewById(R.id.episodes_list);

        //get intent information
        final String seasonID = getIntent().getStringExtra("ID");
        int seasonNo = getIntent().getIntExtra("season", -1);
        SEASONID = seasonID;

        //get Season detail info
        String query = NetworkUtils.TMDB_TV_BASE_URL + seasonID + "/season/" + seasonNo + "?" + NetworkUtils.API_KEY + "&language=en-US";
        Season season = new Season();
        try {
            season = NetworkUtils.getSeason(NetworkUtils.buildUrl(query));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setTitle(season.getName());
        Glide.with(getBaseContext()).load(season.getPosterPath()).error(R.mipmap.image_not_found).into(seasonPosterImageView);
        if (!season.getAirDate().equals("null"))
            seasonReleaseDateTextView.setText(season.getAirDate());
        seasonOverviewTextView.setText(season.getOverview());

        //populate trailer view
        episodesListView.setAdapter(new EpisodesListAdapter(this, season.getEpisodeList()));

        //set season scroll event event
        episodesListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        final Season finalSeason = season;
        episodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, EpisodeDetailsActivity.class);
                intent.putExtra("name", finalSeason.getEpisodeList().get(i).getName());
                intent.putExtra("poster", finalSeason.getEpisodeList().get(i).getPosterPath());
                intent.putExtra("overview", finalSeason.getEpisodeList().get(i).getOverview());
                intent.putExtra("rating", finalSeason.getEpisodeList().get(i).getRating());
                intent.putExtra("release", finalSeason.getEpisodeList().get(i).getAir_date());
                startActivity(intent);
            }
        });
    }
}
