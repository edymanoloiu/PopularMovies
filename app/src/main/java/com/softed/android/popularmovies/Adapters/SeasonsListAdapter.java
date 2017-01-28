package com.softed.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softed.android.popularmovies.R;
import com.softed.android.popularmovies.Utilities.Season;

import java.util.List;

/**
 * Created by Edi on 28.01.2017.
 */

public class SeasonsListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Season> data;

    public SeasonsListAdapter(Context context, List<Season> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.season_list_row_item, null);
        TextView textView = (TextView) view.findViewById(R.id.season_count_row_item);
        textView.setText("Season #" + data.get(i).getNumber());
        textView = (TextView) view.findViewById(R.id.no_episodes_row_item);
        textView.setText("Episodes count: " + data.get(i).getEpisodesCount());
        textView = (TextView) view.findViewById(R.id.air_date_row_item);
        textView.setText("Air date: " + data.get(i).getAirDate());
        return view;
    }
}
