package com.softed.android.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softed.android.popularmovies.MovieDetailsActivity;
import com.softed.android.popularmovies.R;
import com.softed.android.popularmovies.TvDetailsActivity;
import com.softed.android.popularmovies.Utilities.Movie;

import java.util.List;

/**
 * Created by Edi on 15.01.2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;

    public MoviesAdapter(Context mContext, List<Movie> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getName());
        holder.rating.setText(movie.getUserRating());

        // loading album cover using Glide library
        Glide.with(mContext).load(moviesList.get(position).getPosterURL()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesList.get(position).getIsMovie()) {
                    Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                    intent.putExtra("title", moviesList.get(position).getName());
                    intent.putExtra("posterPath", moviesList.get(position).getPosterURL());
                    intent.putExtra("plot", moviesList.get(position).getPlot());
                    intent.putExtra("releaseDate", moviesList.get(position).getReleaseDate());
                    intent.putExtra("userRating", moviesList.get(position).getUserRating());
                    intent.putExtra("ID", moviesList.get(position).getID());
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, TvDetailsActivity.class);
                    intent.putExtra("title", moviesList.get(position).getName());
                    intent.putExtra("posterPath", moviesList.get(position).getPosterURL());
                    intent.putExtra("plot", moviesList.get(position).getPlot());
                    intent.putExtra("releaseDate", moviesList.get(position).getReleaseDate());
                    intent.putExtra("userRating", moviesList.get(position).getUserRating());
                    intent.putExtra("ID", moviesList.get(position).getID());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, rating;
        public ImageView thumbnail;

        public MyViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            rating = (TextView) view.findViewById(R.id.rating);
        }
    }
}
