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
import com.kal.connect.models.healthseeker.DiseaseSurgeryHospitalizationModel;
import com.kal.connect.models.healthseeker.HospitalizationModel;

import java.util.ArrayList;

public class HospitalizationAdapter extends RecyclerView.Adapter<HospitalizationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HospitalizationModel> mediModels;


    public HospitalizationAdapter(Context context, ArrayList<HospitalizationModel> mediModels) {
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
    public HospitalizationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_any_hospitalization, parent, false);
        return new HospitalizationAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HospitalizationAdapter.ViewHolder holder, int position) {
        holder.txt1.setText(mediModels.get(position).getYear());
        holder.txt2.setText(mediModels.get(position).getCondition());
        holder.txt3.setText(mediModels.get(position).getMethod());

        holder.txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setYear(s+"");
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
                mediModels.get(position).setCondition(s+"");
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
                mediModels.get(position).setMethod(s+"");

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
