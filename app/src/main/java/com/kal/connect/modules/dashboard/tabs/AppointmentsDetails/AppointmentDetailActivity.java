package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails;

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
import com.kal.connect.adapters.TabsAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.communicate.ChatActivity;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.modules.communicate.VideoConferenceActivity;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.PrescriptionUploadActivity;
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

public class AppointmentDetailActivity extends CustomActivity implements View.OnClickListener {

    private static final String TAG = "AppointmentDetail";
    // MARK : Properties
    TabLayout tabContainer;
    ViewPager tabPager;
    TabsAdapter tabAdapter;

    TextView doctorName, qualification, appointmentTime, cosultMode, appStatus;
    ImageView consultModeImgVw;

    FloatingActionMenu floatingMenu;
    FloatingActionButton btnTechnician, btnVideoConference, btnLocation, btnStatus, btnConsultNow;
    HashMap<String, Object> selectedAppointmentData;
    String mStrDocName = "", mStrDocId = "", mStrDocQual = "";
    String mStrAppointmentTime = "";
    String mStrAppointmentStatus = "";
    String mStrAppointmentID = "";
    String mStrComplaintID = "";


    // MARK : Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        buildUI();

    }

    // MARK : Instance Methods
    private void buildUI() {

        Bundle mBundle = getIntent().getExtras();
        if (mBundle.containsKey("doctorName") && mBundle.getString("doctorName") != null) {
            mStrDocName = mBundle.getString("doctorName");
            mStrDocId = mBundle.getString("docId");
            mStrDocQual = mBundle.getString("docQual");
            mStrAppointmentTime = mBundle.getString("appointmentTime");
            mStrAppointmentStatus = mBundle.getString("appointmentStatus");
            mStrAppointmentID = mBundle.getString("appointmentID");
            mStrComplaintID = mBundle.getString("complaintID");

        }

        setHeaderView(R.id.headerView, AppointmentDetailActivity.this, AppointmentDetailActivity.this.getResources().getString(R.string.appointment_detail_title));
        headerView.showBackOption();

        //Header for details

        doctorName = (TextView) findViewById(R.id.lblName);
        doctorName.setText(mStrDocName);
        qualification = (TextView) findViewById(R.id.lblDegree);
        qualification.setText(mStrDocQual);
        appointmentTime = (TextView) findViewById(R.id.lblTimeStamp);
        appointmentTime.setText(mStrAppointmentTime);
        cosultMode = (TextView) findViewById(R.id.appointmemt_mode_name);
        appStatus = (TextView) findViewById(R.id.appointmemt_status_name);
        appStatus.setText(mStrAppointmentStatus);
        consultModeImgVw = (ImageView) findViewById(R.id.appointmemt_mode_img);
        tabContainer = (TabLayout) findViewById(R.id.tabs);
        tabPager = (ViewPager) findViewById(R.id.tabPager);

        buildTabs();
        buildFloatingMenu();


    }



    private void buildFloatingMenu() {

        // Floating menu items
        floatingMenu = (FloatingActionMenu) findViewById(R.id.floatingOptions);
        if(mStrAppointmentStatus.toLowerCase().equals("active")){
            floatingMenu.setVisibility(View.VISIBLE);
        }else{
            floatingMenu.setVisibility(View.GONE);
        }

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
        btnTechnician.setVisibility(View.GONE);

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
                getChatConfigurations();

                break;

        }

    }


    private void buildTabs() {

        Resources res = getApplicationContext().getResources();

        // Build Tabs
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_prescription_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_present_compliant_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.personal_history)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_vitals_title)));

        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_records_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.patient_examination)));



        // Build Adapter and Set Pager
        tabAdapter = new TabsAdapter(AppointmentDetailActivity.this.getSupportFragmentManager(), tabContainer.getTabCount());
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

    void setAppointmentParams() {
        try {
            GlobValues.getInstance().setupAddAppointmentParams();
            GlobValues g = GlobValues.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String selectedTime = Utilities.getCurrentTime("hh:mm");
            g.addAppointmentInputParams("AppointmentID", mStrAppointmentID);
            g.addAppointmentInputParams("ComplaintID", mStrComplaintID);
//            g.addAppointmentInputParams("AppointmentDate", Utilities.getCurrentDate("MM/dd/yyyy"));
//            g.addAppointmentInputParams("AppointmentTime", selectedTime = Utilities.getCurrentTime("hh:mm"));
//            g.addAppointmentInputParams("Offset", "-330");
//            g.addAppointmentInputParams("ConsultationMode", "Video Conference");
//            g.addAppointmentInputParams("DoctorRole", "1");
//
//            g.addAppointmentInputParams("isInstant", "" + "1");
//            g.addAppointmentInputParams("ConsultNow", "1");
//            g.addAppointmentInputParams("ComplaintDescp", "");
//
//            g.addAppointmentInputParams("PresentComplaint", selectedAppointmentData.get("complaints").toString());
//            g.addAppointmentInputParams("SelectedComplaintId", selectedAppointmentData.get("ComplaintID").toString());
//            g.addAppointmentInputParams("SelectedAppointmentId", selectedAppointmentData.get("appointmentId").toString());
//
            g.addAppointmentInputParams("SpecialistID", mStrDocId);
//            g.addAppointmentInputParams("SpecialistName", selectedAppointmentData.get("doctorName").toString());
//
//            g.addAppointmentInputParams("ClientID", AppPreferences.getInstance().getUserInfo().getString("ClientID"));

//            Log.e(TAG, "setAppointmentParams: "+selectedAppointmentData.get("doctorId") );
        } catch (Exception e) {
            Log.e(TAG, "setAppointmentParams: "+e );
            e.printStackTrace();
        }

    }

    public void getVideoCallConfigurations() {

        setAppointmentParams();
        HashMap<String, Object> appointmentinputParams = GlobValues.getInstance().getAddAppointmentParams();
        Log.e(TAG, appointmentinputParams.toString());
        SoapAPIManager apiManager = new SoapAPIManager(this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus")
                                && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus")
                                    && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(context, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(context, "Please check again!", false);
                            }
                            return;

                        }

                        if (commonDataInfo.has("VCToekn") &&
                                !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") &&
                                !commonDataInfo.getString("VSSessionID").isEmpty()) {


                            Intent intent = new Intent(context, VideoConferenceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //intent.putExtra("DocterId", selectedAppointmentData.get("doctorId").toString());
                            intent.putExtra("DocterId", mStrDocId);


                            OpenTokConfigConstants.SESSION_ID = commonDataInfo.getString("VSSessionID");
                            OpenTokConfigConstants.TOKEN = commonDataInfo.getString("VCToekn");
                            intent.putExtra("CALER_NAME", doctorName.getText().toString());
                            intent.putExtra("CALL_TYPE", 2);
                            Log.e("OpenTok", "responseCallback: "+commonDataInfo.toString() );

                            context.startActivity(intent);
                            Utilities.pushAnimation((Activity) context);

                        }


                    }
                } catch (Exception e) {
                    Log.e(TAG, ""+e);

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.INITIATE_VIDEO_CALL, "POST"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(AppointmentDetailActivity.this, "Please check internet!", false);
        }
    }


    public void getChatConfigurations() {


        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put("UserID", mStrDocId);

        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();

            inputParams.put("UserName", accInfo.getString("FirstName") + accInfo.getString("LastName"));
            inputParams.put("UserType", "DOC");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        SoapAPIManager apiManager = new SoapAPIManager(this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(context, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(context, "Please check again!", false);
                            }
                            return;

                        }
//                        loadAppointments(responseAry);
                        if (commonDataInfo.has("RespText")) {
                            JSONObject jsonObject = new JSONObject(commonDataInfo.getString("RespText"));
                            if (jsonObject.has("TokenId") && !jsonObject.getString("TokenId").isEmpty() &&
                                    jsonObject.has("Sessionid") && !jsonObject.getString("Sessionid").isEmpty()) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("Option", "0");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("doctorName", mStrDocName);

                                OpenTokConfigConstants.SESSION_ID = jsonObject.getString("Sessionid");
                                OpenTokConfigConstants.TOKEN = jsonObject.getString("TokenId");

                                Log.e("OpenTok", "responseCallback: "+jsonObject.toString() );
                                context.startActivity(intent);
                                Utilities.pushAnimation((Activity) context);

                            }
                        }



                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services5, Config.INITIATE_CHAT, "POST"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        }else{
            Utilities.showAlert(AppointmentDetailActivity.this, "Please check internet!", false);

        }
    }

}
