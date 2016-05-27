package com.POI.main;

import java.util.List;

import com.POI.main.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
 
    Context context;
 
    public CustomListViewAdapter(Context context, int resourceId,
            List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list_item, null);
            holder = new ViewHolder();
            //holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title); 
            //Setting custom font
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "SnackerComic_PerosnalUseOnly.ttf");
            holder.txtTitle.setTypeface(font);
            holder.txtTitle.setTextSize(25);
            
            
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        //holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "SnackerComic_PerosnalUseOnly.ttf");
        holder.txtTitle.setTypeface(font);
        holder.txtTitle.setTextSize(25);
        holder.imageView.setImageResource(rowItem.getImageId());
 
        return convertView;
    }
}