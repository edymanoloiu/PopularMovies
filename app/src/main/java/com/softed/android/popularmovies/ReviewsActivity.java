package com.softed.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.softed.android.popularmovies.Adapters.ReviewsListAdapter;
import com.softed.android.popularmovies.Utilities.NetworkUtils;
import com.softed.android.popularmovies.Utilities.Review;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReviewsActivity extends AppCompatActivity {

    ListView listView;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        String movieTitle = getIntent().getStringExtra("title");
        setTitle(movieTitle);

        String movieID = getIntent().getStringExtra("ID");
        String query = NetworkUtils.TMDB_BASE_URL + movieID + "/reviews?" + NetworkUtils.API_KEY + NetworkUtils.QUERY_END + page;

        //get reviews
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

        //populate trailer view
        listView = (ListView) findViewById(R.id.reviews_list);
        listView.setAdapter(new ReviewsListAdapter(this, reviews));
    }
}
