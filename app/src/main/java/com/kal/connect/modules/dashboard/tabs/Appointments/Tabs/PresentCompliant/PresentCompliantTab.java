package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.PresentCompliant;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kal.connect.R;
import com.kal.connect.utilities.GlobValues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class PresentCompliantTab extends Fragment implements View.OnClickListener{

    // MARK : UIElements
    View view;
    TagGroup presentCompliants;
    EditText mEdtIllness;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.present_complaint, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

        presentCompliants = (TagGroup) view.findViewById(R.id.complaints);
        mEdtIllness = (EditText) view.findViewById(R.id.histor_of_illness);
        loadPresentComplaint();

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    // MARK : APIs
    private void loadPresentComplaint(){

        List<String> tags = new ArrayList<String>();
//        tags.add("Cold");
//        tags.add("Fever");
//        tags.add("Headache");

        try{

//            JSONArray presentComplaint = GlobValues.getAppointmentCompleteDetails().getJSONArray("PresentComp");
//            for (int loop = 0; loop < presentComplaint.length(); loop++) {
//                tags.add(presentComplaint.getString(loop));
//            }

            HashMap<String, Object> item = GlobValues.getSelectedAppointmentData();



            ArrayList<HashMap<String, Object>> symptoms = (ArrayList<HashMap<String, Object>>) item.get("symptoms");

//            List<String> tags = new ArrayList<String>();
            for(HashMap<String, Object> tag : symptoms) {
                tags.add(tag.get("title") + "");
            }

        }catch (Exception e){}
        presentCompliants.setTags(tags);

        try {
            JSONObject appointmentDetails = GlobValues.getInstance().getAppointmentCompleteDetails();
            if (appointmentDetails != null && appointmentDetails.getJSONArray("PatientHxDetails") != null) {
                JSONArray mJsonArray = appointmentDetails.getJSONArray("PatientHxDetails");
//                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                JSONObject mJsonObject = mJsonArray.getJSONArray(0).getJSONObject(0);
                if (mJsonObject != null) {
                    mEdtIllness.setText(mJsonObject.getString("HxOfPresentIllness"));
                }
            }
        }
        catch (Exception e){}

    }
}