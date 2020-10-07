package com.kal.connect.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.models.LocationModel;

import java.util.ArrayList;

public class LocationAdapter extends BaseAdapter {

    ArrayList<LocationModel> locationList = new ArrayList<>();
    Context context;

    public LocationAdapter(ArrayList<LocationModel> locationList, Context context) {
        this.locationList = locationList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locationList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LocationModel loc = locationList.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.location_item, null);
        }

        final ImageView locationIcon = (ImageView)convertView.findViewById(R.id.location_icon);
        final TextView locationNameTextView = (TextView)convertView.findViewById(R.id.location_txt_vw);
        locationNameTextView.setText(loc.getName());

        if(loc.isSelected()){

            locationNameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            locationIcon.setImageResource(R.drawable.location_icon);
        }else{
            locationNameTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
            locationIcon.setImageResource(R.drawable.loc_grey);
        }


        return convertView;
    }
}

