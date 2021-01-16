package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.FamilyHistory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kal.connect.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyHistoryAdapter extends BaseAdapter {

    private Context context;

    public FamilyHistoryAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
        this.context = context;
        this.items = items;
    }

    ArrayList<HashMap<String, Object>> items;
    @Override
    public int getCount() {
        return items.size();
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
        TextView issueTxtVw = null;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

//            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.family_history_item, null);

            // set image based on selected text


        } else {
            gridView = (View) convertView;
        }

        issueTxtVw = (TextView) gridView
                .findViewById(R.id.issue_txt_vw);
        HashMap<String, Object> item = items.get(position);
        String issueName = (item.get("issueName") != null) ? item.get("issueName").toString() : "";
        issueTxtVw.setText(issueName);


        if((boolean)item.get("isSelected") == true)
            issueTxtVw.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        else
            issueTxtVw.setBackgroundColor(Color.GRAY);

        return gridView;
    }
}
