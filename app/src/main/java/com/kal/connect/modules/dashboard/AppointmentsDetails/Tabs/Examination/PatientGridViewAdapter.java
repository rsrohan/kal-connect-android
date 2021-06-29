package com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination;

import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

//import com.aayurveda.doctor.R;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.AyurvedaModule;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.AyurvedaModule;

import java.util.List;

public class PatientGridViewAdapter extends RecyclerView.Adapter<PatientGridViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    List<AyurvedaModule> mAlSub;
    Activity mActivity;
    OnItemCheck onItemCheck;

    public OnItemCheck getOnItemCheck() {
        return onItemCheck;
    }

    public void setOnItemCheck(OnItemCheck onItemCheck) {
        this.onItemCheck = onItemCheck;
    }

    public PatientGridViewAdapter(Activity mActivity, List<AyurvedaModule> ayurvedaModules) {
        this.mInflater = LayoutInflater.from(mActivity);
        this.mAlSub = ayurvedaModules;
        this.mActivity = mActivity;

    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gridview_adapter, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AyurvedaModule mAyurvedaModule = mAlSub.get(position);

        if (mAyurvedaModule.getCheck() != null && mAyurvedaModule.getCheck()) {
            holder.mCheckBox.setChecked(true);
//            holder.mCheckBox.setClickable(false);

            holder.mCheckBox.setText(mAyurvedaModule.getModule());
        } else {
            holder.mCheckBox.setChecked(false);


            holder.mCheckBox.setText(mAyurvedaModule.getModule());

        }
        holder.mCheckBox.setTag(""+mAyurvedaModule.getModuleID());

        if(onItemCheck == null){
            holder.mCheckBox.setEnabled(false);
            holder.mCheckBox.setClickable(false);
        } else{
            holder.mCheckBox.setEnabled(true);
            holder.mCheckBox.setClickable(true);
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(onItemCheck != null){
                    onItemCheck.onItemCheck(b, (String)compoundButton.getTag());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlSub.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView mTxtSub;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_name);
        }

    }

    public interface OnItemCheck{
        public void onItemCheck(boolean check, String checkID);
    }


}