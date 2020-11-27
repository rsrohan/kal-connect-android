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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.communicate.ChatActivity;
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
    String mStrDocName = "", mStrDocId;

    // MARK : Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail);

        buildUI();

    }

    // MARK : Instance Methods
    private void buildUI() {

        Bundle mBundle = getIntent().getExtras();
        if (mBundle.containsKey("doctorName") && mBundle.getString("doctorName") != null) {
            mStrDocName = mBundle.getString("doctorName");
            mStrDocId = mBundle.getString("docId");
        }

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
        floatingMenu.setVisibility(View.VISIBLE);
        btnTechnician.setVisibility(View.VISIBLE);

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

        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_present_compliant_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.personal_history)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_vitals_title)));

        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_detail_records_title)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.patient_examination)));
        tabContainer.addTab(tabContainer.newTab().setText(res.getString(R.string.appointment_prescription_title)));



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

    void setAppointmentParams() {
        try {
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

            g.addAppointmentInputParams("isInstant", "" + "1");
            g.addAppointmentInputParams("ConsultNow", "1");
            g.addAppointmentInputParams("ComplaintDescp", "");
            g.addAppointmentInputParams("ClientID", AppPreferences.getInstance().getUserInfo().getString("ClientID"));

            g.addAppointmentInputParams("PresentComplaint", selectedAppointmentData.get("complaints").toString());
            g.addAppointmentInputParams("SelectedComplaintId", selectedAppointmentData.get("ComplaintID").toString());
            g.addAppointmentInputParams("SelectedAppointmentId", selectedAppointmentData.get("appointmentId").toString());
            g.addAppointmentInputParams("AppointmentID", selectedAppointmentData.get("appointmentId").toString());

            g.addAppointmentInputParams("SpecialistID", selectedAppointmentData.get("doctorId").toString());
            g.addAppointmentInputParams("SpecialistName", selectedAppointmentData.get("doctorName").toString());

            GlobValues.getInstance().addAppointmentInputParams("ComplaintID", selectedAppointmentData.get("ComplaintID").toString());

        } catch (Exception e) {
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
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(context, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(context, "Please check again!", false);
                            }
                            return;

                        }

                        if (commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()) {


                            Intent intent = new Intent(context, VideoConference.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("DocterId", selectedAppointmentData.get("doctorId").toString());


                            OpenTokConfig.SESSION_ID = commonDataInfo.getString("VSSessionID");
                            OpenTokConfig.TOKEN = commonDataInfo.getString("VCToekn");
                            intent.putExtra("CALER_NAME", doctorName.getText().toString());
                            intent.putExtra("CALL_TYPE", 2);
                            Log.e("OpenTok", "responseCallback: "+commonDataInfo.toString() );

                            context.startActivity(intent);
                            Utilities.pushAnimation((Activity) context);

                        }


                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.INITIATE_VIDEO_CALL, "POST"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        } else {

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

                                OpenTokConfig.SESSION_ID = jsonObject.getString("Sessionid");
                                OpenTokConfig.TOKEN = jsonObject.getString("TokenId");

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
        }
    }

}
