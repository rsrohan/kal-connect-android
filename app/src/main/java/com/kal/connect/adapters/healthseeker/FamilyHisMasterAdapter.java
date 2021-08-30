package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.FamilyHistory;
import com.kal.connect.models.healthseeker.MasterData;

import java.util.ArrayList;

public class FamilyHisMasterAdapter extends RecyclerView.Adapter<FamilyHisMasterAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FamilyHistory> masterData;


    public FamilyHisMasterAdapter(Context context, ArrayList<FamilyHistory> masterData) {
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
    public FamilyHisMasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_symptoms, parent, false);
        return new FamilyHisMasterAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(FamilyHisMasterAdapter.ViewHolder holder, int position) {
       FamilyHistory dataBean = masterData.get(position);
       holder.masterElements.setText(dataBean.getDiseaseName());
       FamilyHisSubElementAdapter symptomsSubElementAdapter = new FamilyHisSubElementAdapter(mContext, dataBean);
       holder.subElements.setAdapter(symptomsSubElementAdapter);

    }

    @Override
    public int getItemCount() {
        return masterData.size();
    }


}
