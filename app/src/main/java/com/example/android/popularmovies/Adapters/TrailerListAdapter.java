package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.R;

/**
 * Created by Edi on 21.01.2017.
 */

public class TrailerListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    String[] data;

    public TrailerListAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.trailer_list_row_item, null);
        TextView textView = (TextView) view.findViewById(R.id.trailer_list_item_text);
        textView.setText(data[i]);
        return view;
    }
}
