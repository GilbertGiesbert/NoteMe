package com.mediaworx.noteme.common.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.labledtypes.ColorType;

public class ColorPickerArrayAdapter extends ArrayAdapter<ColorType> {

    private final Activity context;
    private final ColorType[] colors;

    static class ViewHolder {
        public TextView text;
    }


    public ColorPickerArrayAdapter(Activity context, ColorType[] colors) {
        super(context, R.layout.color_picker_row, colors);
        this.context = context;
        this.colors = colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.color_picker_row, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.tv_row_colorpicker);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.text.setText(colors[position].getLabel());
        holder.text.setBackgroundColor(colors[position].getColorValue());

        return rowView;
    }
}