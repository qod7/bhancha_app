package com.bhanchha.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Shashwat on Nov 09.
 */

class NavDrawerListAdapter extends BaseAdapter {
    Context context;
    String[] itemNames;
    TypedArray itemIcons;

    public NavDrawerListAdapter(Context context, String[] itemNames, TypedArray itemIcons) {
        this.context = context;
        this.itemNames = itemNames;
        this.itemIcons = itemIcons;
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public Object getItem(int position) {
        return itemNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.nav_drawer_list_adapter, parent, false);
        } else {
            row = convertView;
        }

        TextView tv = (TextView) row.findViewById(R.id.listLayoutTextView);
        tv.setText(itemNames[position]);

        ImageView iv = (ImageView) row.findViewById(R.id.listLayoutImageView);
        iv.setImageDrawable(itemIcons.getDrawable(position));
        return row;
    }

}