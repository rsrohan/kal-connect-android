package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.DemographyData;
import com.kal.connect.models.healthseeker.MasterData;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F1Questions;

import java.util.ArrayList;

public class DemographyMasterAdapter extends RecyclerView.Adapter<DemographyMasterAdapter.ViewHolder> {

    Context mContext;
    ArrayList<DemographyData> masterData;


    public DemographyMasterAdapter(Context context, ArrayList<DemographyData> masterData) {
        this.mContext = context;
        this.masterData = masterData;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView masterElements;
        RecyclerView subElements;
        public ViewHolder(View view) {
            super(view);
            masterElements = view.findViewById(R.id.masterElements);
            subElements = view.findViewById(R.id.subElements);

        }
    }

    @Override
    public DemographyMasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_demog, parent, false);
        return new DemographyMasterAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DemographyMasterAdapter.ViewHolder holder, int position) {
       DemographyData dataBean = masterData.get(position);
       holder.masterElements.setText(dataBean.getDemographyType());
       DemographySubElementAdapter symptomsSubElementAdapter = new DemographySubElementAdapter(mContext, dataBean);

       holder.subElements.setAdapter(symptomsSubElementAdapter);

    }

    @Override
    public int getItemCount() {
        return masterData.size();
    }


}
