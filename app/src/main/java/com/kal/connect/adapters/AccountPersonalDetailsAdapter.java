package com.kal.connect.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kal.connect.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountPersonalDetailsAdapter extends RecyclerView.Adapter<AccountPersonalDetailsAdapter.ViewHolder> {


    // Step 1: Initialize By receiving the data via constructor
    Context mContext;
    ArrayList<HashMap<String, Object>> items;
    private static AccountPersonalDetailsAdapter.ItemClickListener itemClickListener;

    public AccountPersonalDetailsAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
        this.items = partnerItems;
        this.mContext = context;
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        public TextView lblKey, lblValue;

        public ViewHolder(View view) {
            super(view);

            lblKey = (TextView) view.findViewById(R.id.lblKey);
            lblValue = (TextView) view.findViewById(R.id.lblValue);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            itemClickListener.onItemClick(getAdapterPosition(), v);
            notifyDataSetChanged();

        }

    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public AccountPersonalDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new AccountPersonalDetailsAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AccountPersonalDetailsAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);
        String prescription = (item.get("key") != null) ? item.get("key").toString() : "";
        holder.lblKey.setText(prescription);

        String diagnostics = (item.get("value") != null) ? item.get("value").toString() : "";
        holder.lblValue.setText(diagnostics);

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
    public void setOnItemClickListener(AccountPersonalDetailsAdapter.ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

}

