package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.DiseaseModel;
import com.kal.connect.models.healthseeker.DiseaseSurgeryHospitalizationModel;

import java.util.ArrayList;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {

    Context mContext;
    ArrayList<DiseaseModel> mediModels;


    public DiseaseAdapter(Context context, ArrayList<DiseaseModel> mediModels) {
        this.mContext = context;
        this.mediModels = mediModels;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText txt1, txt2, txt3;

        public ViewHolder(View view) {
            super(view);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            txt3 = view.findViewById(R.id.txt3);

        }
    }

    @Override
    public DiseaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_describe_issue_past_disease, parent, false);
        return new DiseaseAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DiseaseAdapter.ViewHolder holder, int position) {
        holder.txt1.setText(mediModels.get(position).getDisease());
        holder.txt2.setText(mediModels.get(position).getPeriod());
        holder.txt3.setText(mediModels.get(position).getTreatment());

        holder.txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setDisease(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setPeriod(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.txt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setTreatment(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mediModels.size();
    }


}
