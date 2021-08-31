package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.MasterData;

import java.util.ArrayList;

public class SymptomsMasterAdapter extends RecyclerView.Adapter<SymptomsMasterAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MasterData> masterData;


    public SymptomsMasterAdapter(Context context, ArrayList<MasterData> masterData) {
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
    public SymptomsMasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_symptoms, parent, false);
        return new SymptomsMasterAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SymptomsMasterAdapter.ViewHolder holder, int position) {
       MasterData dataBean = masterData.get(position);
       holder.masterElements.setText(dataBean.getMasterElement());
       SymptomsSubElementAdapter symptomsSubElementAdapter = new SymptomsSubElementAdapter(mContext, dataBean);
       holder.subElements.setAdapter(symptomsSubElementAdapter);


    }

    @Override
    public int getItemCount() {
        return masterData.size();
    }


}
