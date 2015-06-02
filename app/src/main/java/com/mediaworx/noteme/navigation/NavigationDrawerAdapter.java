package com.mediaworx.noteme.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaworx.noteme.R;

public class NavigationDrawerAdapter extends BaseAdapter{

    private Activity activity;

    static class ViewHolder{
        public ImageView icon;
        public TextView text;
    }

    public NavigationDrawerAdapter(Activity context){
        this.activity = context;
    }

    @Override
    public int getCount() {
        return NavigationDrawerItem.values().length;
    }

    @Override
    public Object getItem(int position) {
        return NavigationDrawerItem.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.navigationdrawer_row, parent, false);

            viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_navigationDrawerItem);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tv_navigationDrawerItem);
            convertView.setTag(viewHolder);
        }

        NavigationDrawerItem item = NavigationDrawerItem.values()[position];

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.icon.setImageResource(item.getIconId());
        viewHolder.text.setText(item.getLabel());

        return convertView;
    }
}