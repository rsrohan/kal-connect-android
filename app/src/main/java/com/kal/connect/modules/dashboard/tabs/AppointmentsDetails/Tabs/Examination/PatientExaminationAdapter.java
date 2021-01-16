package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination;

import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.aayurveda.doctor.R;
//import com.medi360.doctor.doctorsapp.pojo.GetPatientPojo;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.AyurvedaModule;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.MasterElement;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.SubElement;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.AyurvedaModule;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.GetPatientPojo;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.GridViewAdapter;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.MasterElement;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.SubElement;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class PatientExaminationAdapter extends Section {

    private final ArrayList<MasterElement> masterElements;
    Activity mActivity;
    Button mBtnUpdate;
    GridViewAdapter mAdapter;
    RecyclerView mRecyclerView;
    EditText mEdtPatientExam;
    ArrayList<GetPatientPojo> SelectedPatient;

    public PatientExaminationAdapter(Activity mActivity, @NonNull final ArrayList<MasterElement> masterElements, Button mBtnUpdate, EditText mEdtPatientExam, ArrayList<GetPatientPojo> SelectedPatient) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.patient_tiem)
                .build());
        this.masterElements = masterElements;
        this.mActivity = mActivity;
        this.mBtnUpdate = mBtnUpdate;
        this.mEdtPatientExam = mEdtPatientExam;
        this.SelectedPatient = SelectedPatient;
    }


    @Override
    public int getContentItemsTotal() {
        return masterElements.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        final MasterElement masterElement = masterElements.get(position);

        if (position != 0) {
            for (int k = 0; k < masterElement.getSubElements().size(); k++) {
                SubElement subElement = masterElement.getSubElements().get(k);
                LinearLayout mLinearLayout = new LinearLayout(mActivity);
                mLinearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView mTxtView = new TextView(mActivity);

                mTxtView.setTextColor(mActivity.getResources().getColor(R.color.home_text_color));
                mRecyclerView = new RecyclerView(mActivity);
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRecyclerView.setLayoutParams(mParams);
                mRecyclerView.setHasFixedSize(true);
                mLinearLayout.addView(mTxtView);


                for (int j = 0; j < subElement.getAyurvedaModules().size(); j++) {
                    AyurvedaModule mAyurvedaModule = subElement.getAyurvedaModules().get(j);
                    for (int i = 0; i < SelectedPatient.size(); i++) {
                        GetPatientPojo mGetPatientPojo = SelectedPatient.get(i);
                        if (mAyurvedaModule.getModuleID() == Integer.parseInt(mGetPatientPojo.getModuleID().trim())) {
                            AyurvedaModule DuplicateAyur = new AyurvedaModule();
                            DuplicateAyur.setCheck(true);
                            DuplicateAyur.setModule(mAyurvedaModule.getModule());
                            subElement.getAyurvedaModules().set(j, DuplicateAyur);
                        }
                    }
                }

                mAdapter = new GridViewAdapter(mActivity, subElement.getAyurvedaModules(), mTxtView, mBtnUpdate, mEdtPatientExam, SelectedPatient);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 3);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mLinearLayout.addView(mRecyclerView);
                itemHolder.mTxtHeader.setText(subElement.getAyurvedaModules().get(0).getMasterElement());
                ((ItemViewHolder) holder).mLlRoot.addView(mLinearLayout);
            }
        }


    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        LinearLayout mLlRoot;
        TextView mTxtHeader;

        ItemViewHolder(@NonNull View view) {
            super(view);
            rootView = view;
            mLlRoot = view.findViewById(R.id.ll_root);
            mTxtHeader = view.findViewById(R.id.txt_header);

        }
    }


//    if (position != 0) {
//            for (int k = 0; k < masterElement.getSubElements().size(); k++) {
//                SubElement subElement = masterElement.getSubElements().get(k);
//                LinearLayout mLinearLayout = new LinearLayout(mActivity);
//                mLinearLayout.setOrientation(LinearLayout.VERTICAL);
//                TextView mTxtView = new TextView(mActivity);
//                mTxtView.setTextColor(mActivity.getResources().getColor(R.color.home_text_color));
//                mLinearLayout.addView(mTxtView);
//                for (int j = 0; j < subElement.getAyurvedaModules().size(); j++) {
//                    AyurvedaModule ayurvedaModule = subElement.getAyurvedaModules().get(j);
//                    LinearLayout subLinearLayout = new LinearLayout(mActivity);
//                    subLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    CheckBox mCheckBox = new CheckBox(mActivity);
//                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    mCheckBox.setLayoutParams(mParams);
//                    mCheckBox.setText(ayurvedaModule.getModule());
//                    subLinearLayout.addView(mCheckBox);
//                    mLinearLayout.addView(subLinearLayout);
//                    mTxtView.setText(ayurvedaModule.getSubElement());
//                    itemHolder.mTxtHeader.setText(ayurvedaModule.getMasterElement());
//                }
//                ((ItemViewHolder) holder).mLlRoot.addView(mLinearLayout);
//            }
//        }


}