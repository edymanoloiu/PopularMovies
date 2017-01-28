package com.softed.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softed.android.popularmovies.R;
import com.softed.android.popularmovies.Utilities.Episode;

import java.util.List;

/**
 * Created by Edi on 28.01.2017.
 */

public class EpisodesListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Episode> episodeList;

    public EpisodesListAdapter(Context context, List<Episode> data) {
        this.context = context;
        this.episodeList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return episodeList.size();
    }

    @Override
    public Object getItem(int i) {
        return episodeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.episodes_list_row_item, null);
        TextView textView = (TextView) view.findViewById(R.id.episode_name_row_item);
        textView.setText(episodeList.get(i).getName());
        textView = (TextView) view.findViewById(R.id.episode_rating_row_item);
        textView.setText(episodeList.get(i).getRating());
        return view;
    }
}
