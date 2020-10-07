package com.kal.connect.modules.dashboard.tabs.Appointments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.communicate.OpenTokConfig;
import com.kal.connect.modules.communicate.VideoConference;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.viewpager.widget.ViewPager;

public class AppointmentDetail extends CustomActivity implements View.OnClickListener {

    // MARK : Properties
    TabLayout tabContainer;
    ViewPager tabPager;
    TabsAdapter tabAdapter;

    TextView doctorName, qualification, appointmentTime, cosultMode, appStatus;
    ImageView consultModeImgVw;

    FloatingActionMenu floatingMenu;
    FloatingActionButton btnTechnician, btnVideoConference, btnLocation, btnStatus, btnConsultNow;
    HashMap<String, Object> selectedAppointmentData;

    // MARK : Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail);
        buildUI();

    }

    // MARK : Instance Methods
    private void buildUI() {

        setHeaderView(R.id.headerView, AppointmentDetail.this, AppointmentDetail.this.getResources().getString(R.string.appointment_detail_title));
        headerView.showBackOption();

        //Header for details

        doctorName = (TextView) findViewById(R.id.lblName);
        qualification = (TextView) findViewById(R.id.lblDegree);
        appointmentTime = (TextView) findViewById(R.id.lblTimeStamp);
        cosultMode = (TextView) findViewById(R.id.appointmemt_mode_name);
        appStatus = (TextView) findViewById(R.id.appointmemt_status_name);
        consultModeImgVw = (ImageView) findViewById(R.id.appointmemt_mode_img);



        tabContainer = (TabLayout) findViewById(R.id.tabs);
        tabPager = (ViewPager) findViewById(R.id.tabPager);

        buildTabs();
        buildFloatingMenu();
        setupHeaderValues();


    }

    public void setupHeaderValues() {
        try {
            selectedAppointmentData = GlobValues.getSelectedAppointmentData();
            doctorName.setText(selectedAppointmentData.get("doctorName").toString());
            qualification.setText(selectedAppointmentData.get("qualification").toString());

//            String appointmentDate = (selectedAppointmentData.get("appointmentData") != null)? selectedAppointmentData.get("appointmentData").toString() : "";
//            if(!appointmentDate.isEmpty())
//            {
//                appointmentDate = Utilities.changeStringFormat(appointmentDate,"yyyy-mm-dd","dd/mm/yyyy");
//            }
            appointmentTime.setText(selectedAppointmentData.get("appointmentDate").toString());

            if ((int) selectedAppointmentData.get("consultationMode") == 1) {
                consultModeImgVw.setImageResource(R.drawable.icon_video_call);
                cosultMode.setText(getResources().getString(R.string.appointment_option_video_conference));
            } else {
                consultModeImgVw.setImageResource(R.drawable.icon_center);
                cosultMode.setText("Clinic Center");
            }

            if(selectedAppointmentData.get("status").toString().toLowerCase().equals("active") ||
                    selectedAppointmentData.get("status").toString().toLowerCase().equals("not consulted")){
                floatingMenu.setVisibility(View.VISIBLE);
            }else{
                floatingMenu.setVisibility(View.GONE);
            }

            appStatus.setText(Utilities.getStatus(AppointmentDetail.this,selectedAppointmentData.get("status").toString()));
//            appStatus.setText(selectedAppointmentData.get("status").toString());

        } catch (Exception e) {
        }

    }

    private void buildFloatingMenu() {

        // Floating menu items
        floatingMenu = (FloatingActionMenu) findViewById(R.id.floatingOptions);

        // Options under the menu
        btnTechnician = (FloatingActionButton) findViewById(R.id.optionTechnical);
        btnLocation = (FloatingActionButton) findViewById(R.id.optionLocation);
        btnVideoConference = (FloatingActionButton) findViewById(R.id.optionVideoConference);
        btnConsultNow = (FloatingActionButton) findViewById(R.id.optionConsultNow);
        btnStatus = (FloatingActionButton) findViewById(R.id.optionStatus);

        // floatingMenu.setOnClickListener(this);
        btnTechnician.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnVideoConference.setOnClickListener(this);
        btnConsultNow.setOnClickListener(this);
        btnStatus.setOnClickListener(this);

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        if (floatingMenu != null) {
            floatingMenu.close(true);
        }

        switch (v.getId()) {

            case R.id.optionConsultNow:
                getVideoCallConfigurations();

                break;

            case R.id.optionTechnical:

                break;

        }

    }


    private void buildTabs() {

        Resources res = getApplicationContext().getResources();

        // Build Tabs

        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_present_compliant_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.personal_history)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_vitals_title)));
//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_ecg_title)));

        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_records_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.patient_examination)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_prescription_title)));


//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_blood_glucose_title)));
//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_blood_investigation_title)));

//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_cholestrol_title)));
//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_semi_analyser_title)));




//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_urine_analyser_title)));

//        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_family_history_title)));






        // For reload reference : https://stackoverflow.com/questions/28494637/android-how-to-stop-refreshing-fragments-on-tab-change
        // tabPager.setOffscreenPageLimit(tabContainer.getTabCount());

        // To Set Icon
        // tabContainer.getTabAt(0).setIcon(SELECTED_ICON[0]);

        // Build Adapter and Set Pager
        tabAdapter = new TabsAdapter(AppointmentDetail.this.getSupportFragmentManager(), tabContainer.getTabCount());
        tabPager.setAdapter(tabAdapter);
        tabPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabContainer));

        // Build Tab Selection Listeners
        tabContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabPager.setCurrentItem(0);
    }

    void setAppointmentParams()
    {
        try{
            GlobValues.getInstance().setupAddAppointmentParams();
            GlobValues g = GlobValues.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String selectedTime = Utilities.getCurrentTime("hh:mm");
            g.addAppointmentInputParams("AppointmentDate", Utilities.getCurrentDate("MM/dd/yyyy"));
            g.addAppointmentInputParams("AppointmentTime", selectedTime = Utilities.getCurrentTime("hh:mm"));
            g.addAppointmentInputParams("Offset", "-330");
            g.addAppointmentInputParams("ConsultationMode", "Video Conference");
            g.addAppointmentInputParams("DoctorRole", "1");
//            g.addAppointmentInputParams("isTechnician", "0");
//            g.addAppointmentInputParams("PatLoc", "");
//            g.addAppointmentInputParams("Lattitude", "");
//            g.addAppointmentInputParams("Longitude", "");
            g.addAppointmentInputParams("isInstant", ""+"1");
            g.addAppointmentInputParams("ConsultNow", "1");
            g.addAppointmentInputParams("ComplaintDescp", "");
            g.addAppointmentInputParams("ClientID", AppPreferences.getInstance().getUserInfo().getString("ClientID"));

            g.addAppointmentInputParams("PresentComplaint", selectedAppointmentData.get("complaints").toString());
            g.addAppointmentInputParams("SelectedComplaintId", selectedAppointmentData.get("ComplaintID").toString());
            g.addAppointmentInputParams("SelectedAppointmentId", selectedAppointmentData.get("appointmentId").toString());
            g.addAppointmentInputParams("AppointmentID", selectedAppointmentData.get("appointmentId").toString());

            g.addAppointmentInputParams("SpecialistID", selectedAppointmentData.get("doctorId").toString());
            g.addAppointmentInputParams("SpecialistName", selectedAppointmentData.get("doctorName").toString());

//            item.put("complaints", singleObj.getString("PresentComplaint"));
            GlobValues.getInstance().addAppointmentInputParams("ComplaintID", selectedAppointmentData.get("ComplaintID").toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getVideoCallConfigurations(){
//        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
//        inputParams.put("ComplaintID",GlobValues.getInstance().getSelectedAppointment());
        setAppointmentParams();
        HashMap<String, Object> appointmentinputParams = GlobValues.getInstance().getAddAppointmentParams();
        SoapAPIManager apiManager = new SoapAPIManager(this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(context,commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(context,"Please check again!",false);
                            }
                            return;

                        }
//                        loadAppointments(responseAry);
                        if(commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()){
//                            String TOKEN = commonDataInfo.getString("VCToekn");
//                            String SESSION = commonDataInfo.getString("VSSessionID");
//
//                            Intent intent = new Intent(context, VideoCaller.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            intent.putExtra("SESSION_ID",SESSION);
//                            intent.putExtra("TOKEN",TOKEN);
//
//                            intent.putExtra("CALER_NAME",docName.getText().toString());
//                            intent.putExtra("CALL_TYPE",2);
//                            startActivity(intent);
//                            Utilities.pushAnimation(context);

                            Intent intent = new Intent(context, VideoConference.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                            intent.putExtra("SESSION_ID",commonDataInfo.getString("VSSessionID"));
//                            intent.putExtra("TOKEN",commonDataInfo.getString("VCToekn"));

                            OpenTokConfig.SESSION_ID = commonDataInfo.getString("VSSessionID");
                            OpenTokConfig.TOKEN = commonDataInfo.getString("VCToekn");
                            intent.putExtra("CALER_NAME",doctorName.getText().toString());
                            intent.putExtra("CALL_TYPE",2);

                            context.startActivity(intent);
                            Utilities.pushAnimation((Activity) context);

                        }





                    }
                }catch (Exception e){

                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.INITIATE_VIDEO_CALL,"POST"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        }else{

        }
    }
//    public void getVideoCallConfigurations(){
//        final HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
//
//
////        inputParams.put("ComplaintID",GlobValues.getInstance().getSelectedAppointment());
////        GlobValues g = GlobValues.getInstance();
////
////        final HashMap<String, Object> appointmentinputParams  = g.getAddAppointmentParams();
//        inputParams.put("SpecialistID",selectedAppointmentData.get("doctorId").toString());
//        inputParams.put("SpecialistName",selectedAppointmentData.get("doctorName").toString());
//
//        inputParams.put("AppointmentDate",selectedAppointmentData.get("date").toString());
//        inputParams.put("AppointmentTime",selectedAppointmentData.get("time").toString());
//        inputParams.put("ComplaintDescp",selectedAppointmentData.get("symptoms").toString());
//
//
//        inputParams.put("ComplaintID",selectedAppointmentData.get("appointmentId").toString());
//
//        inputParams.put("ConsultationMode","Video Conference");
//
//
//
//
//        inputParams.put("DoctorRole","1");
//
//
//        SoapAPIManager apiManager = new SoapAPIManager(AppointmentDetail.this, inputParams, new APICallback() {
//            @Override
//            public void responseCallback(Context context, String response) throws JSONException {
//                Log.e("***response***",response);
//
//                try{
//                    JSONArray responseAry = new JSONArray(response);
//                    if(responseAry.length()>0){
//                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
//                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
//                            if(commonDataInfo.has("Message") && !commonDataInfo.getString("Message").isEmpty()){
//                                Utilities.showAlert(AppointmentDetail.this,commonDataInfo.getString("Message"),false);
//                            }else{
//                                Utilities.showAlert(AppointmentDetail.this,"Please check again!",false);
//                            }
//                            return;
//
//                        }
////                        loadAppointments(responseAry);
//                        if(commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
//                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()){
//                            String TOKEN = commonDataInfo.getString("VCToekn");
//                            String SESSION = commonDataInfo.getString("VSSessionID");
//
//                            Intent intent = new Intent(AppointmentDetail.this, VideoCaller.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//                            intent.putExtra("SESSION_ID",SESSION);
//                            intent.putExtra("TOKEN",TOKEN);
//
//                            intent.putExtra("CALER_NAME",inputParams.get("SpecialistName").toString());
//                            intent.putExtra("CALL_TYPE",2);
//                            startActivity(intent);
//
//                            Utilities.pushAnimation(AppointmentDetail.this);
//
//                        }
//
//                    }
//                }catch (Exception e){
//
//                }
//            }
//        },true);
//        String[] url = {Config.WEB_Services1,Config.INITIATE_VIDEO_CALL,"POST"};
//
//        if (Utilities.isNetworkAvailable(AppointmentDetail.this)) {
//            apiManager.execute(url);
//        }else{
//
//        }
//    }

}
