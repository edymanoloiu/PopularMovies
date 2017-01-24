package com.softed.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softed.android.popularmovies.R;
import com.softed.android.popularmovies.Utilities.Review;

import java.util.List;

/**
 * Created by Edi on 22.01.2017.
 */

public class ReviewsListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Review> reviews;

    public ReviewsListAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.reviews_list_row_item, null);
        TextView reviewerName = (TextView) view.findViewById(R.id.reviewer_name);
        reviewerName.setText(reviews.get(i).getAuthor());
        TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
        reviewContent.setText(reviews.get(i).getContent());
        return view;
    }
}
