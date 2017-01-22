package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.Data.MovieContract;
import com.example.android.popularmovies.MovieDetailsActivity;
import com.example.android.popularmovies.R;

/**
 * Created by Edi on 22.01.2017.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavoriteMoviesAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        mCursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position))
            return;

        final String movieTitle = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
        final String movieRating = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING));
        final String posterPath = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
        final String plot = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT));
        final String releaseDate = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        final String movieID = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));

        holder.title.setText(movieTitle);
        holder.rating.setText(movieRating);
        Glide.with(mContext).load(posterPath).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("title", movieTitle);
                intent.putExtra("posterPath", posterPath);
                intent.putExtra("plot", plot);
                intent.putExtra("releaseDate", releaseDate);
                intent.putExtra("userRating", movieRating);
                intent.putExtra("ID", movieID);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
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

