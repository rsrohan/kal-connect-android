package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.BloodGlucose;

import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kal.connect.R;
import com.kal.connect.customLibs.Callbacks.UpdateEMRCallBack;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BloodGlucoseTab extends Fragment implements View.OnClickListener{

    // MARK : UIElements
    View view;
    EditText txtDate, txtRBS, txtPPBS, txtFBS;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.blood_glucose, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

        txtDate = (EditText) view.findViewById(R.id.txtDate);
        txtRBS = (EditText) view.findViewById(R.id.txtRBS);
        txtFBS = (EditText) view.findViewById(R.id.txtFBS);
        txtPPBS = (EditText) view.findViewById(R.id.txtPPBS);

        Button updateButton = (Button) view.findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(this);

        setupValues();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.updatebutton:
                updateEMR();
                break;
        }
    }


    public void updateEMR(){

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

//        {
//            "ClientID": 12,
//                "patientID": 4609,
//                "appointmentID": 5269,
//                "firstname": "gajanan",
//                "lastname": "Patient",
//                "EMRSectionIndicator": "2",
//                "ComplaintID": 6103,
//                "ModifiedDate": "2/15/2019",
//                "patientname": "gajanan patient",
//                "ppbs": 1,
//                "rbs": 2,
//                "fbs":3
//        }


        inputParams.put("fbs", txtFBS.getText().toString().isEmpty()?"0":txtFBS.getText().toString());
        inputParams.put("rbs", txtRBS.getText().toString().isEmpty()?"0":txtRBS.getText().toString());
        inputParams.put("ppbs", txtPPBS.getText().toString().isEmpty()?"0":txtPPBS.getText().toString());

        inputParams.put("EMRSectionIndicator", "3");
        Utilities.getInstance().updateEMR(getContext(), inputParams, new UpdateEMRCallBack() {
            @Override
            public void updateEMRCallback(Boolean success) throws JSONException {
                txtDate.setText(Utilities.getCurrentDate());
            }
        });

    }
    public void setupValues(){
        try{
            JSONArray semiAnalysor = GlobValues.getAppointmentCompleteDetails().getJSONArray("Vitals");
            if(semiAnalysor.length()>0){
                JSONObject singleObj = semiAnalysor.getJSONObject(0);
                txtFBS.setText(singleObj.getString("FBS").isEmpty()?"":singleObj.getString("FBS"));
                txtRBS.setText(singleObj.getString("RBS").isEmpty()?"":singleObj.getString("RBS"));
                txtPPBS.setText(singleObj.getString("PPBS").isEmpty()?"":singleObj.getString("PPBS"));
//                txtDate.setText(singleObj.getString("ModifiedDate").isEmpty()?"":singleObj.getString("ModifiedDate"));
                if(!singleObj.getString("ModifiedDate").isEmpty() && !singleObj.getString("ModifiedDate").equals("null"))
                {
                    String modifiedDate = Utilities.dateRT(singleObj.getString("ModifiedDate"));
                    modifiedDate = Utilities.changeStringFormat(modifiedDate,"yyyy-mm-dd","dd/mm/yyyy");
                    txtDate.setText(modifiedDate);
                }
            }

        }catch (Exception e){}

    }
}