package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.UrineAnalyzer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kal.connect.R;


public class UrineAnalyzerTab extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.urine_analyzer, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }
}