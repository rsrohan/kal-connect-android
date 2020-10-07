package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Cholestrol;

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

public class CholestrolTab extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    EditText txtCholestrol, txtDate;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cholestrol, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

        txtCholestrol = (EditText) view.findViewById(R.id.txtCholestrol);
        txtDate = (EditText) view.findViewById(R.id.txtDate);

        Button updateButton = (Button) view.findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(this);
        setupValues();

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


    public void updateEMR(){

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        inputParams.put("Cholesterol", txtCholestrol.getText().toString().isEmpty()?"0":txtCholestrol.getText().toString());
        inputParams.put("EMRSectionIndicator", "5");
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
                txtCholestrol.setText(singleObj.getString("Cholestrol").isEmpty()?"":singleObj.getString("Cholestrol"));
//                txtDate.setText(singleObj.getString("ModifiedDate").isEmpty()?"":singleObj.getString("ModifiedDate"));
                if(!singleObj.getString("ModifiedDate").isEmpty() && !singleObj.getString("ModifiedDate").equals("null"))
                {
                    String modifiedDate = Utilities.dateRT(singleObj.getString("ModifiedDate"));
                    modifiedDate = Utilities.changeStringFormat(modifiedDate,"yyyy-mm-dd","dd/mm/yyyy");
                    txtDate.setText(modifiedDate);
                }

//                txtDate.setText(singleObj.getString("ModifiedDate").isEmpty()?"":Utilities.dateRT(singleObj.getString("ModifiedDate")));
            }

        }catch (Exception e){}

    }
}