package com.kal.connect.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
//import android.support.annotationon.NoonNull;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.modules.dashboard.tabs.HomeScreen.DoctorsListActivity;
import com.kal.connect.modules.hospitals.AboutHospitalActivity;
import com.kal.connect.utilities.Utilities;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.MyViewHolder> {

    Context context;
    ArrayList<HospitalModel> hospitalLists;

    public HospitalListAdapter(Context context, ArrayList<HospitalModel> hospitalLists) {
        this.context = context;
        this.hospitalLists = hospitalLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hospitals_list_item, viewGroup, false);
        itemView.setBackgroundColor(Color.WHITE);
        HospitalListAdapter.MyViewHolder myViewHolder = new HospitalListAdapter.MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        HospitalModel hospital = hospitalLists.get(i);
        holder.hospitalName.setText(hospital.getHospitalName());
        holder.hospitalQualification.setText(hospital.getDiagnosticName());
        holder.location.setText(hospital.getCityName()+", "+hospital.getStateName());
        holder.hospitalName.setText(hospital.getHospitalName());
        holder.aboutBtn.setTag(hospital);
        holder.selectBtn.setTag(hospital);

    }

    @Override
    public int getItemCount() {

        return hospitalLists.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.hospital_name)
        TextView hospitalName;

        @BindView(R.id.hospital_qualification)
        TextView hospitalQualification;

        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.short_description)
        TextView shortDescription;

        @BindView(R.id.about_btn)
        LinearLayout aboutBtn;

        @BindView(R.id.select_btn)
        LinearLayout selectBtn;

        @OnClick(R.id.select_btn)
        void selectHospital(View view){
            HospitalModel hospital = (HospitalModel) view.getTag();
            Utilities.selectedHospitalModel = hospital;
            context.startActivity(new Intent(context, DoctorsListActivity.class));
        }

        @OnClick(R.id.about_btn)
        void hospitalInfo(View view){
            HospitalModel hospital = (HospitalModel) view.getTag();
            Utilities.selectedHospitalModel = hospital;
            context.startActivity(new Intent(context, AboutHospitalActivity.class));
        }

        @Override
        public void onClick(View v) {

        }

        public MyViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
