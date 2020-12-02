package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Vitals;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kal.connect.R;
import com.kal.connect.customLibs.Callbacks.UpdateEMRCallBack;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalsTab extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    EditText txtWeight, txtHeight, txtTemprature, txtSPO2, txtSys, txtPR, txtLFT, txtDate, txtDia;
    Button update;

    @BindView(R.id.covidRG)
    RadioGroup covidRG;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.vitals, container, false);
        ButterKnife.bind(this, view);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

        txtWeight = (EditText) view.findViewById(R.id.txtWeight);
        txtHeight = (EditText) view.findViewById(R.id.txtHeight);
        txtTemprature = (EditText) view.findViewById(R.id.txtTemprature);
        txtSPO2 = (EditText) view.findViewById(R.id.txtSPO2);

        txtSys = (EditText) view.findViewById(R.id.txtSys);
        txtDia = (EditText) view.findViewById(R.id.txtDia);
        txtPR = (EditText) view.findViewById(R.id.txtPR);
        txtLFT = (EditText) view.findViewById(R.id.txtLFT);
        txtDate = (EditText) view.findViewById(R.id.txtDate);
        update = (Button) view.findViewById(R.id.updatebutton);
        update.setOnClickListener(this);

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
//                "EMRSectionIndicator": "1",
//                "ComplaintID": 6103,
//                "patientname": "gajanan patient",
//                "ModifiedDate": "2/15/2019",
//                "sysrate": 10,
//                "diarate": 20,
//                "pulserate": 0,
//                "temperature": 102,
//                "oxypercentage": null,
//                "heartrate": null,
//                "height": 148,
//                "weight": 75,  "fv2": 1,  "fv1": 1}



        inputParams.put("weight", txtWeight.getText().toString().isEmpty()?"0":txtWeight.getText().toString());
        inputParams.put("height", txtHeight.getText().toString().isEmpty()?"0":txtHeight.getText().toString());

        String sys = txtSys.getText().toString().isEmpty()?"0":txtSys.getText().toString();
        String dia = txtDia.getText().toString().isEmpty()?"0":txtDia.getText().toString();
        inputParams.put("BloodPressure", sys+"/"+dia);
        inputParams.put("temperature", txtTemprature.getText().toString().isEmpty()?"0":txtTemprature.getText().toString());
        inputParams.put("pulserate", txtPR.getText().toString().isEmpty()?"0":txtPR.getText().toString());
        inputParams.put("fv1", txtLFT.getText().toString().isEmpty()?"0":txtLFT.getText().toString());
        inputParams.put("oxypercentage", txtSPO2.getText().toString().isEmpty()?"0":txtSPO2.getText().toString());

        String covid = "";
        if(covidRG.getCheckedRadioButtonId() != -1) {
            switch (covidRG.getCheckedRadioButtonId())
            {
                case R.id.positive:
                    covid = "Positive";
                    break;
                case R.id.negative:
                    covid = "Negative";
                    break;
                case R.id.not_tested:
                    covid = "";
                    break;
            }
        }
        inputParams.put("covid", covid);

        inputParams.put("ModifiedDate", Utilities.getCurrentDate("MM/dd/yyyy"));

        inputParams.put("EMRSectionIndicator", "1");
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
                txtWeight.setText(singleObj.getString("Weight").isEmpty()?"":singleObj.getString("Weight"));
                txtHeight.setText(singleObj.getString("Height").isEmpty()?"":singleObj.getString("Height"));




                txtTemprature.setText(singleObj.getString("Temperature").isEmpty()?"":singleObj.getString("Temperature"));
                txtPR.setText(singleObj.getString("PulseRate").isEmpty()?"":singleObj.getString("PulseRate"));





//                txtLFT.setText(singleObj.getString("fv1").isEmpty()?"":singleObj.getString("fv1"));
                txtSPO2.setText(singleObj.getString("oxypercentage").isEmpty()?"":singleObj.getString("oxypercentage"));

                if(!singleObj.getString("BloodPressure").isEmpty())
                {
                    String[] bp = singleObj.getString("BloodPressure").split("/");
                    if(bp.length == 2)
                    {
                        txtSys.setText(bp[0]);
                        txtDia.setText(bp[1]);
                    }
                }
//                    txtSys.setText(singleObj.getString("BloodPressure").isEmpty()?"":singleObj.getString("BloodPressure"));

                ((RadioButton)covidRG.findViewById(R.id.not_tested)).setChecked(true);

                if(!singleObj.getString("COVID").isEmpty())
                {
                    if(singleObj.getString("COVID").toLowerCase().equals("negative")) {
                        ((RadioButton)covidRG.findViewById(R.id.negative)).setChecked(true);
                    }else if(singleObj.getString("COVID").toLowerCase().equals("positive"))
                    {
                        ((RadioButton)covidRG.findViewById(R.id.positive)).setChecked(true);
                    }
                }

                if(!singleObj.getString("ModifiedDate").isEmpty()){
                    String modifiedDate = Utilities.dateRT(singleObj.getString("ModifiedDate"));
                    modifiedDate = Utilities.changeStringFormat(modifiedDate,"yyyy-mm-dd","dd/mm/yyyy");
                    txtDate.setText(singleObj.getString("ModifiedDate").isEmpty()?"":modifiedDate);
                }

            }

        }catch (Exception e){}

    }
}