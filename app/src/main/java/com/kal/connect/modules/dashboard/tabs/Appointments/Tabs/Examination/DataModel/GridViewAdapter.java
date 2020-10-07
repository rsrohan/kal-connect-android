package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel;

import android.app.Activity;
import android.app.ProgressDialog;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

//import com.aayurveda.doctor.R;
//import com.medi360.doctor.doctorsapp.config.Configuration;
//import com.medi360.doctor.doctorsapp.werservice.WebServiceLogin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.utilities.GlobValues;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

//import static com.medi360.doctor.doctorsapp.Doctor_login.PREFS_NAME;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    List<AyurvedaModule> mAlSub;
    Activity mActivity;
    TextView mTxtSub;
    Button mBtnUpdate;
    EditText mEdtPatientExam;
    ArrayList<GetPatientPojo> SelectedPatient;
    WeakHashMap<String, String> mWeak = new WeakHashMap<>();
    ProgressDialog pd;

    public GridViewAdapter(Activity mActivity, List<AyurvedaModule> ayurvedaModules, TextView mTxtSub, Button mBtnUpdate, EditText mEdtPatientExam, ArrayList<GetPatientPojo> SelectedPatient) {
        this.mInflater = LayoutInflater.from(mActivity);
        this.mAlSub = ayurvedaModules;
        this.mActivity = mActivity;
        this.mTxtSub = mTxtSub;
        this.mBtnUpdate = mBtnUpdate;
        this.mEdtPatientExam = mEdtPatientExam;
        this.SelectedPatient = SelectedPatient;
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
            holder.mCheckBox.setClickable(false);
            holder.mCheckBox.setEnabled(false);
            holder.mCheckBox.setText(mAyurvedaModule.getModule());
        } else {
            holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setClickable(false);
            holder.mCheckBox.setEnabled(false);
            holder.mCheckBox.setText(mAyurvedaModule.getModule());
        }

        mEdtPatientExam.setClickable(false);
        mEdtPatientExam.setEnabled(false);


        if (mAyurvedaModule.getSubElement() != null && !mAyurvedaModule.getSubElement().equalsIgnoreCase("General")) {
            mTxtSub.setText(mAyurvedaModule.getSubElement());
        } else {
            mTxtSub.setVisibility(View.GONE);
        }

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    JSONObject mJsonObject = new JSONObject();
//                    mJsonObject.put("PatientID", Configuration.Selected_patient_id);
//                    mJsonObject.put("ComplaintID", Configuration.Selected_complaint_id);
////                    mJsonObject.put("ComplaintID", "1103");
//                    mJsonObject.put("SystematicExam", mEdtPatientExam.getText().toString());
//                    JSONArray mJsonArray = new JSONArray();
//                    for (int i = 0; i < Configuration.mAlDuplicate.size(); i++) {
//                        if (Configuration.mAlDuplicate.get(i).getCheck() != null && Configuration.mAlDuplicate.get(i).getCheck()) {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("ModuleID", Configuration.mAlDuplicate.get(i).getModuleID());
//                            jsonObject.put("SpecialistID", mActivity.getSharedPreferences(PREFS_NAME, 0).getString("SPECIALISTID", ""));
//                            mJsonArray.put(jsonObject);
//                        }
//                    }
//                    mJsonObject.put("objPatExam", mJsonArray);
//                    System.out.println("JSON_UPDATE_RESPONSE" + mJsonObject);
//
//                    new PatientUpdate().execute(mJsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AyurvedaModule ayurvedaModule = mAlSub.get(position);
                ayurvedaModule.setCheck(b);
//                Configuration.mAlDuplicate.add(ayurvedaModule);
                if (GlobValues.mAlDuplicate != null && GlobValues.mAlDuplicate.size() > 0) {
                    for (int i = 0; i < GlobValues.mAlDuplicate.size(); i++) {
                        AyurvedaModule duplicateAyurveda = GlobValues.mAlDuplicate.get(i);
                        if (duplicateAyurveda.getModuleID() != ayurvedaModule.getModuleID()) {
                            if (b) {
                                String id = String.valueOf(ayurvedaModule.getModuleID());
                                if (!mWeak.containsKey(id)) {
                                    mWeak.put(id, id);
                                    GlobValues.mAlDuplicate.add(ayurvedaModule);
                                }
                            }
                        } else {
                            if (!b) {
                                if (duplicateAyurveda.getModuleID() == ayurvedaModule.getModuleID()) {
                                    GlobValues.mAlDuplicate.remove(i);
                                }
                            }
                        }
                    }
                } else {
                    GlobValues.mAlDuplicate.add(ayurvedaModule);
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

//    public class PatientUpdate extends AsyncTask<String, Void, String> {
//
//        public void onPreExecute() {
//            super.onPreExecute();
//            pd = new ProgressDialog(mActivity);
//            pd.setMessage("Please Wait...");
//            pd.setCancelable(false);
//            pd.setIndeterminate(true);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String responseString = null;
//            responseString = WebServiceLogin.WebserviceAuthenticateUser(params[0], "SavePatExaminationDetails");
//            return responseString;
//        }
//
//        @Override
//        public void onPostExecute(String responseString) {
//            if ((pd != null) && pd.isShowing())
//                pd.dismiss();
//            if (responseString == null) {
//                return;
//            }
//            try {
//                if (responseString == null) {
//                    return;
//                }
//
//                Toast.makeText(mActivity, new JSONArray(responseString).getJSONObject(0).getString("Message"), Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                System.out.print("Usha exception in asynctask");
//            }
//
//        }
//    }

}