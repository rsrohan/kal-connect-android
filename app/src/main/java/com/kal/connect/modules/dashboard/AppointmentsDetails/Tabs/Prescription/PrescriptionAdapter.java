package com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Prescription;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.kal.connect.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {


    // Step 1: Initialize By receiving the data via constructor
    Context mContext;
    ArrayList<HashMap<String, Object>> items;
    private static PrescriptionAdapter.ItemClickListener itemClickListener;

    public PrescriptionAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
        this.items = partnerItems;
        this.mContext = context;
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        public TextView lblPrescription, lblDiagnostics;
        public Button btnView;

        public ViewHolder(View view) {
            super(view);

            lblPrescription = (TextView) view.findViewById(R.id.lblPrescription);
            lblDiagnostics = (TextView) view.findViewById(R.id.lblDiagnostics);
            btnView = (Button) view.findViewById(R.id.btnView);

            btnView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            itemClickListener.onItemClick(getAdapterPosition(), v);
            notifyDataSetChanged();

        }

    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public PrescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_item, parent, false);
        return new PrescriptionAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(PrescriptionAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);
        String prescription = (item.get("prescription") != null) ? item.get("prescription").toString() : "";
        holder.lblPrescription.setText(prescription);

        String diagnostics = (item.get("diagnostics") != null) ? item.get("diagnostics").toString() : "";
        holder.lblDiagnostics.setText(diagnostics);
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
    public void setOnItemClickListener(PrescriptionAdapter.ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

}

