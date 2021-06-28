package com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.BloodInvestigation;

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

import lib.kingja.switchbutton.SwitchMultiButton;

public class BloodInvestigationTab extends Fragment implements View.OnClickListener{

    // MARK : UIElements
    View view;
    SwitchMultiButton choiceMalaria, choiceThyphoid, choiceHCV, choiceVDRL, choiceHbAg, choiceTroponin, choiceDengue, choiceTB;
    EditText txtHbPercent;
    Button updateButton;
    JSONObject singleObj;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.blood_investigation, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

//        txtMalaria =(EditText) view.findViewById(R.id.txtMalaria);
//        txtThyphoid =(EditText) view.findViewById(R.id.txtMalaria);
        txtHbPercent =(EditText) view.findViewById(R.id.txtHB);
//        txtHCV =(EditText) view.findViewById(R.id.txtMalaria);
//        txtVDRL =(EditText) view.findViewById(R.id.txtMalaria);
//        txtHbAg =(EditText) view.findViewById(R.id.txtMalaria);
//        txtTroponin =(EditText) view.findViewById(R.id.txtMalaria);
//        txtDengue =(EditText) view.findViewById(R.id.txtMalaria);
//        txtTB =(EditText) view.findViewById(R.id.txtMalaria);
        choiceMalaria = (SwitchMultiButton) view.findViewById(R.id.malaria);
        choiceThyphoid = (SwitchMultiButton) view.findViewById(R.id.typhoid);

        choiceHCV = (SwitchMultiButton) view.findViewById(R.id.hcv);
        choiceVDRL = (SwitchMultiButton) view.findViewById(R.id.vdrl);
        choiceHbAg = (SwitchMultiButton) view.findViewById(R.id.hbsag);
        choiceTroponin = (SwitchMultiButton) view.findViewById(R.id.troponin);
        choiceDengue = (SwitchMultiButton) view.findViewById(R.id.dengue);
        choiceTB = (SwitchMultiButton) view.findViewById(R.id.tb);
        updateButton = (Button) view.findViewById(R.id.updatebutton);

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

    public void setupValues(){
        try{
            JSONArray semiAnalysor = GlobValues.getAppointmentCompleteDetails().getJSONArray("Vitals");
            if(semiAnalysor.length()>0){
                singleObj = semiAnalysor.getJSONObject(0);
                choiceMalaria.setSelectedTab(getChoicenumber("Malaria"));
                choiceThyphoid.setSelectedTab(getChoicenumber("Typhoid"));
                choiceHCV.setSelectedTab(getChoicenumber("HCV"));
                choiceVDRL.setSelectedTab(getChoicenumber("VDR"));
                choiceHbAg.setSelectedTab(getChoicenumber("HBS"));

                choiceTroponin.setSelectedTab(getChoicenumber("TroponinI"));
                choiceDengue.setSelectedTab(getChoicenumber("Dengue"));
                choiceTB.setSelectedTab(getChoicenumber("TB"));

                txtHbPercent.setText(singleObj.getString("HB").isEmpty()?"":singleObj.getString("HB"));






//                txtMalaria.setText(singleObj.getString("Malaria").isEmpty()?"":singleObj.getString("Malaria"));
//                txtThyphoid.setText(singleObj.getString("Typhoid").isEmpty()?"":singleObj.getString("Typhoid"));
//                txtHbPercent.setText(singleObj.getString("HB").isEmpty()?"":singleObj.getString("HB"));
//                txtHCV.setText(singleObj.getString("HCV").isEmpty()?"":singleObj.getString("HCV"));
//                txtVDRL.setText(singleObj.getString("VDR").isEmpty()?"":singleObj.getString("VDR"));
//                txtHbAg.setText(singleObj.getString("HBS").isEmpty()?"":singleObj.getString("HBS"));
//                txtTroponin.setText(singleObj.getString("TroponinI").isEmpty()?"":singleObj.getString("TroponinI"));
//                txtDengue.setText(singleObj.getString("Dengue").isEmpty()?"":singleObj.getString("Dengue"));
//                txtTB.setText(singleObj.getString("TB").isEmpty()?"":singleObj.getString("TB"));
            }

        }catch (Exception e){}

    }
    public int getChoicenumber(String key){
        try{
            if(singleObj.has(key) && !singleObj.getString(key).isEmpty())
                return (Integer.parseInt(singleObj.getString(key)))+1;

        }catch (Exception e){

        }
        return 0;
    }

    public void updateEMR(){

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        inputParams.put("malaria", choiceV(choiceMalaria));
        inputParams.put("typhoid", choiceV(choiceThyphoid));
        inputParams.put("hcv", choiceV(choiceHCV));
        inputParams.put("vdr", choiceV(choiceVDRL));
        inputParams.put("hbs", choiceV(choiceHbAg));
        inputParams.put("troponinI", choiceV(choiceTroponin));
        inputParams.put("dengue", choiceV(choiceDengue));
        inputParams.put("tb", choiceV(choiceTB));
        inputParams.put("hb", txtHbPercent.getText().toString().isEmpty()?"0":txtHbPercent.getText().toString());

//        "chikumguniya": 0,  "kalaAzar": 0,  "upt": 0,  "hiv": 0

        inputParams.put("chikumguniya","");
        inputParams.put("kalaAzar","");
        inputParams.put("upt","");
        inputParams.put("hiv","");


        inputParams.put("EMRSectionIndicator", "4");
        Utilities.getInstance().updateEMR(getContext(),inputParams);
    }

    public String choiceV(SwitchMultiButton s){
        return s.getSelectedTab()==0?"":s.getSelectedTab()-1+"";

    }
}