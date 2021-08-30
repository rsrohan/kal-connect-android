package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kal.connect.R;
import com.kal.connect.adapters.TabsAdapter;
import com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.modules.communicate.VideoConferenceActivity;
import com.kal.connect.modules.dashboard.AppointmentsDetails.AppointmentDetailActivity;
import com.kal.connect.modules.dashboard.AppointmentsDetails.AppointmentsFragment;
import com.kal.connect.modules.dashboard.BookAppointment.AppointmentSummaryActivity;
import com.kal.connect.modules.dashboard.BookAppointment.IssueDescriptorMapActivityS3;
import com.kal.connect.modules.hospitals.HospitalsListActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f0Profile;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f10Symptoms;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f11FamilyHistory;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f12Consent;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f1Questions;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f2DescribeIssue;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f3PastIssuesMedicalCondition;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f4AboutMedications;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f5PastDiseaseSurgeryHospitalization;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f6ScaleQuestions;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f7ScaleQuestions;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f8Habits;
import static com.kal.connect.adapters.healthseeker.HealthSeekerTabsAdapter.f9PersonalPreference;

public class HealthSeekerActivity extends AppCompatActivity {
    private static final String TAG = "HealthSeekerForm";
    ViewPager tabPager;
    HealthSeekerTabsAdapter tabAdapter;
    TabLayout tabContainer;
    TextView tvNext, tvPrev;
    public static ArrayList<IssuesModel> issuesModels = new ArrayList<>();
    public static ArrayList<String> pastIssues = new ArrayList<>();
    public static int complaintID = 0;
    public static int oldComplaintID = 0;
    public static String patientID = AppPreferences.getInstance().getPatientID();
    public static final Calendar myCalendar = Calendar.getInstance();
    public static HashMap<String, Object> inputParamsGET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_seeker);
        tabPager = (ViewPager) findViewById(R.id.container);
        tabContainer = (TabLayout) findViewById(R.id.tabs);
        tvNext = findViewById(R.id.tv_next);
        tvPrev = findViewById(R.id.tv_prev);
        issuesModels = (ArrayList<IssuesModel>) getIntent().getSerializableExtra("issues");
        pastIssues = (ArrayList<String>) getIntent().getSerializableExtra("PastIssues");

        inputParamsGET = new HashMap<>();
        inputParamsGET.put("PatientID", patientID);
        inputParamsGET.put("ComplaintID", oldComplaintID);
        inputParamsGET.put("IsFilling", true);
        Log.e(TAG, "input parameters for GET apis: "+inputParamsGET.toString());
        setTabs();




    }



    private void setTabs() {
        tabContainer.addTab(tabContainer.newTab().setText(""));//0
        tabContainer.addTab(tabContainer.newTab().setText(""));//1
        tabContainer.addTab(tabContainer.newTab().setText(""));//2
        tabContainer.addTab(tabContainer.newTab().setText(""));//3
        tabContainer.addTab(tabContainer.newTab().setText(""));//4
        tabContainer.addTab(tabContainer.newTab().setText(""));//5
        tabContainer.addTab(tabContainer.newTab().setText(""));//6
        tabContainer.addTab(tabContainer.newTab().setText(""));//7
        tabContainer.addTab(tabContainer.newTab().setText(""));//8
        tabContainer.addTab(tabContainer.newTab().setText(""));//9
        tabContainer.addTab(tabContainer.newTab().setText(""));//10
        tabContainer.addTab(tabContainer.newTab().setText(""));//11
        tabContainer.addTab(tabContainer.newTab().setText(""));//12

        tabAdapter = new HealthSeekerTabsAdapter(getSupportFragmentManager(), tabContainer.getTabCount());
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

        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabContainer.getSelectedTabPosition() == 0) {
                    finish();
                } else if (tabContainer.getSelectedTabPosition() != 0) {
                    tabPager.setCurrentItem(tabContainer.getSelectedTabPosition() - 1);
                }


            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabContainer.getSelectedTabPosition() == 0) {
                    f0Profile.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 1) {
                    f1Questions.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 2) {
                    f2DescribeIssue.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 3) {
                    f3PastIssuesMedicalCondition.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 4) {
                    f4AboutMedications.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 5) {
                    f5PastDiseaseSurgeryHospitalization.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 6) {
                    f6ScaleQuestions.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 7) {
                    f7ScaleQuestions.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 8) {
                    f8Habits.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 9) {
                    f9PersonalPreference.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 10) {
                    f10Symptoms.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 11) {
                    f11FamilyHistory.saveData();
                } else if (tabContainer.getSelectedTabPosition() == 12) {
                    f12Consent.saveData();
                }

                if (tabContainer.getSelectedTabPosition() != tabContainer.getTabCount() - 1) {
                    tabPager.setCurrentItem(tabContainer.getSelectedTabPosition() + 1);
                } else if (tabContainer.getSelectedTabPosition() == tabContainer.getTabCount() - 1) {
                    startActivity(new Intent(HealthSeekerActivity.this, HospitalsListActivity.class));
                    Utilities.pushAnimation(HealthSeekerActivity.this);

                }
            }
        });

    }


}