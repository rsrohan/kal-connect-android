package com.kal.connect.modules.dashboard.tabs.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.customLibs.appCustomization.CustomActivity;

import com.kal.connect.models.DoctorModel;

import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutDoctorActivity extends CustomActivity {

    private static final String TAG = "AboutDoctorAct";
    TextView docName, location, desc;

    GlobValues g = GlobValues.getInstance();

    @BindView(R.id.profileImage)
    ImageView profileImage;

    @BindView(R.id.consult_now)
    Button consultNowBtn;
    String specialistID = "";

    @OnClick(R.id.consult_now)
    void consultNow() {

        g.addAppointmentInputParams("AppointmentDate", Utilities.getCurrentDate("MM/dd/yyyy"));
        g.addAppointmentInputParams("AppointmentTime", Utilities.getCurrentTime("hh:mm"));

        GlobValues.getInstance().addAppointmentInputParams("ConsultNow", 1);

        Intent mIntent = new Intent(this, AppointmentSummaryActivity.class);
        mIntent.putExtra("specialistID",specialistID);
        Log.e(TAG, "consultNow: "+specialistID );
        startActivity(mIntent);


    }

    @OnClick(R.id.consult_later)
    void consultLater() {
        GlobValues.getInstance().addAppointmentInputParams("ConsultNow", 2);
        startActivity(new Intent(this, ScheduleAppointment.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_about_doctor);
        ButterKnife.bind(this);
        buildUI();

    }

    public void buildUI() {
        setHeaderView(R.id.headerView, AboutDoctorActivity.this, AboutDoctorActivity.this.getResources().getString(R.string.about_doctor));
        headerView.showBackOption();
        docName = (TextView) findViewById(R.id.doc_name);
        location = (TextView) findViewById(R.id.location);
        desc = (TextView) findViewById(R.id.desc);

        DoctorModel d = GlobValues.getInstance().getAboutDoctor();
        docName.setText(d.getName());
        location.setText(d.getAddress() + " " + d.getZipcode());

        Bundle mBundle = getIntent().getExtras();
        if (mBundle.containsKey("SpecialistID") && mBundle.getString("SpecialistID") != null) {
            specialistID = mBundle.getString("SpecialistID");
        }
        if (!d.getIsLoggedIn()) {
            consultNowBtn.setVisibility(View.GONE);
        }
        try {
            Utilities.loadImageWithPlaceHoler(profileImage, d.getDoctorImage(), d.getName());
            desc.setText(d.getBreifnote());

        } catch (Exception e) {

        }

    }

}
