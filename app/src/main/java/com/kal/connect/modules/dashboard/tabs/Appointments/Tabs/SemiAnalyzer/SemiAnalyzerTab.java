package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.SemiAnalyzer;

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
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SemiAnalyzerTab extends Fragment implements View.OnClickListener{

    // MARK : UIElements
    View view;
    EditText semiAcid;
    Button updateButton;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.semi_analyzer, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

        semiAcid = (EditText) view.findViewById(R.id.txtAcid);
        updateButton = (Button) view.findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(this);

        try{
            JSONArray semiAnalysor = GlobValues.getAppointmentCompleteDetails().getJSONArray("SemiAnalyzer");
            if(semiAnalysor.length()>0){
                JSONObject singleObj = semiAnalysor.getJSONObject(0);
                semiAcid.setText(singleObj.getString("UricAcid").isEmpty()?"":singleObj.getString("UricAcid"));

            }

        }catch (Exception e){}
    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.updatebutton:
                    updateEMR();
                break;

        }
    }

    public boolean validate(){
        if(Utilities.validate(getContext(),semiAcid,"",false,1,10))
            return false;
        return true;
    }

    public void updateEMR(){

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        inputParams.put("uricAcid", semiAcid.getText().toString().isEmpty()?"0":semiAcid.getText().toString());
        inputParams.put("EMRSectionIndicator", "7");
        Utilities.getInstance().updateEMR(getContext(),inputParams);
    }
}