package com.kal.connect.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {


    // Step 1: Initialize By receiving the data via constructor
    Context mContext;
    ArrayList<HashMap<String, Object>> items;
    private static LanguageListAdapter.ItemClickListener itemClickListener;

    public LanguageListAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
        this.items = partnerItems;
        this.mContext = context;
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        public ImageView selected;
        public TextView languageName, localeName;

        RelativeLayout container;

        public ViewHolder(View view) {
            super(view);

            languageName = (TextView) view.findViewById(R.id.language_name);
            localeName = (TextView) view.findViewById(R.id.language_locale_name);
            container = (RelativeLayout) view.findViewById(R.id.container);
            selected = (ImageView) view.findViewById(R.id.selected);
            container.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            itemClickListener.onItemClick(getAdapterPosition(), v);
            notifyDataSetChanged();

        }

    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public LanguageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);
        return new LanguageListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(LanguageListAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);

        String language = (item.get("languageName") != null)? item.get("languageName").toString() : "";
        holder.languageName.setText(language);
        String localeLanguage = (item.get("localeName") != null)? item.get("localeName").toString() : "";
        holder.localeName.setText(localeLanguage);


        if(AppPreferences.getInstance().retrieveLanguage().equals(item.get("languageCode").toString())){
            holder.container.setBackgroundColor(Utilities.getColor(mContext, R.color.colorPrimaryLight));
            holder.languageName.setTextColor(Utilities.getColor(mContext, R.color.colorPrimaryDark));
            holder.localeName.setTextColor(Utilities.getColor(mContext, R.color.colorPrimaryDark));
            holder.selected.setVisibility(View.VISIBLE);
        }else{
            holder.container.setBackgroundColor(Utilities.getColor(mContext, R.color.white));
            holder.languageName.setTextColor(Utilities.getColor(mContext, R.color.black));
            holder.localeName.setTextColor(Utilities.getColor(mContext, R.color.black));
            holder.selected.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Step 4: Create Interface and it's methods to Receive Click event
    public interface ItemClickListener {
        public void onItemClick(int position, View v);
    }

    // Method to receive Click event
    public void setOnItemClickListener(LanguageListAdapter.ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

}

