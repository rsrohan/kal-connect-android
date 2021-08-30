package com.kal.connect.modules.dashboard.BookAppointment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;

import com.kal.connect.R;
import com.kal.connect.adapters.SelectedIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.Maps.Manager.CustomMapActivity;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.models.LocationModel;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F1Questions;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity;
import com.kal.connect.modules.hospitals.AboutHospitalActivity;
import com.kal.connect.modules.hospitals.HospitalsListActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;

public class IssueDescriptorMapActivityS3 extends CustomMapActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private SelectedIssueAdapter selectedIssueAdapter;
    RecyclerView selectedRecyclerView;
    LinearLayout selectTime, consultNow, tecContainerLayout;
    private ArrayList<IssuesModel> selectedIssuesModelList = new ArrayList<>();
    private ArrayList<LocationModel> locationsList = new ArrayList<>();
    DatePickerDialog datepickerdialog;
    TimePickerDialog timepickerdialog;
    CheckBox techRequired;

    String selectedDateToSend;

    Boolean isUSPatient = true;

    Boolean technicianRequiredEnableManuall = false;

    int isInstant = 0;
    Calendar now;


    LovelyTextInputDialog l;

    RelativeLayout nextBtn;

    TextView appointmentTime, addressTxtVw;
    EditText descTxt;
    ImageView communicationImgVw;
    SwitchMultiButton loginOptionBtn;

    int currentlySelected = -1;

    String selectedDate = "", selectedTime = "";

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    GlobValues g = GlobValues.getInstance();

    @BindView(R.id.systolicsys)
    EditText systolic;
    @BindView(R.id.diastolic)
    EditText diastolic;
    @BindView(R.id.pulse_rate)
    EditText pulse;
    @BindView(R.id.bodyTemperature)
    EditText bodyTemperature;
    @BindView(R.id.spo)
    EditText spo2;

    @BindView(R.id.covidRG)
    RadioGroup covidRG;

    @BindView(R.id.vitals_weight)
    EditText weight;

    @BindView(R.id.vitals_height)
    EditText height;
    Boolean isMap = false;
    String addressInfo = "";
    private HashMap<String, Object> appointmentinputParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_descriptor);
        buildUI();
    }

    public void buildUI() {
        TextView tv_proceed = findViewById(R.id.tv_proceed);
        tv_proceed.setText(tv_proceed.getText()+" (3/3)");
        isMap = false;
        ButterKnife.bind(this);
        setHeaderView(R.id.headerView, IssueDescriptorMapActivityS3.this, IssueDescriptorMapActivityS3.this.getResources().getString(R.string.issue_descriptor_title));
        headerView.showBackOption();

        selectedRecyclerView = (RecyclerView) findViewById(R.id.issuesSelectedRecyclerVW);
        selectTime = (LinearLayout) findViewById(R.id.header_select_time);
        consultNow = (LinearLayout) findViewById(R.id.consult_now_layout);
        tecContainerLayout = (LinearLayout) findViewById(R.id.tec_container_layout);
        addressTxtVw = (TextView) findViewById(R.id.addressTxtVw);
        descTxt = (EditText) findViewById(R.id.description_edit_txt);
        communicationImgVw = (ImageView) findViewById(R.id.imgCommunication);
        loginOptionBtn = (SwitchMultiButton) findViewById(R.id.signin_options);


        selectedIssuesModelList = (ArrayList<IssuesModel>) getIntent().getSerializableExtra("SelectedIssues");

        selectedIssueAdapter = new SelectedIssueAdapter(selectedIssuesModelList, IssueDescriptorMapActivityS3.this, null);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(IssueDescriptorMapActivityS3.this, LinearLayoutManager.HORIZONTAL, false);
        selectedRecyclerView.setLayoutManager(horizontalLayoutManager);
        selectedRecyclerView.setAdapter(selectedIssueAdapter);
        appointmentTime = (TextView) findViewById(R.id.appointmentTime);
        nextBtn = findViewById(R.id.next_btn);
        techRequired = (CheckBox) findViewById(R.id.technician_req_chk);


        l = new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorAccent)
                .setTitle(R.string.address_for_medical_assistant)
                .setMessage(R.string.address_option_msg)
                .setIcon(R.drawable.ok_icon_white)
                .setInputFilter(R.string.error_input_address, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.length() >= 8 ? true : false;
                    }
                })
                .setConfirmButton(R.string.btn_ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        isMap = false;
                        try {

                            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
                            if (userInfo.has("Lattitude") && userInfo.get("Lattitude") != null && !userInfo.getString("Lattitude").isEmpty() && !userInfo.getString("Lattitude").equals("null")) {
                                g.addAppointmentInputParams("Lattitude", "" + userInfo.get("Lattitude"));
                            }
                            if (userInfo.has("Longitude") && userInfo.get("Longitude") != null && !userInfo.getString("Longitude").isEmpty() && !userInfo.getString("Longitude").equals("null")) {
                                g.addAppointmentInputParams("Longitude", "" + userInfo.get("Longitude"));
                            }
                        } catch (Exception e) {

                        }

                        addressTxtVw.setVisibility(View.VISIBLE);
                        if (!isMap) {
                            addressTxtVw.setText(text);
                        }
                        g.addAppointmentInputParams("TechAddress", text);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                technicianRequiredEnableManuall = true;
                                techRequired.setChecked(true);
                            }
                        }, 300);


                    }
                }).setNegativeButton(getResources().getString(R.string.map), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Launch Location Picker
                        l.dismiss();
                        launchPlacePicker();

                    }
                });
        ;


        createLocations();

        selectTime.setOnClickListener(this);
        consultNow.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        techRequired.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (technicianRequiredEnableManuall) {
                        technicianRequiredEnableManuall = false;
                        return;
                    }

                    String address = "";
                    try {
                        JSONObject accInfo = AppPreferences.getInstance().getUserInfo();
                        address = accInfo.getString("LocationAddress");

                        address = accInfo.getString("Addressline1") + ", " + accInfo.getString("Addressline2") + ", " +
                                accInfo.getString("cityname") + ", " + accInfo.getString("statename") + ", " +
                                accInfo.getString("Zipcode");

                        weight.setText(accInfo.getString(""));
                        height.setText(accInfo.getString(""));


                    } catch (Exception e) {

                    }


                    if (!isMap) {
                        l.setInitialInput(address);
                    } else {
                        l.setInitialInput(addressInfo);
                    }

                    l.setHint("Enter Address");
                    techRequired.setChecked(false);
                    l.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            techRequired.setChecked(false);
                        }
                    }, 300);


                } else {
                    addressTxtVw.setText("");
                    addressTxtVw.setVisibility(View.GONE);
                }
            }
        });

        loginOptionBtn.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {


                if (position == 0) {
                    communicationImgVw.setImageResource(R.drawable.icon_video_call);
                } else {
                    communicationImgVw.setImageResource(R.drawable.icon_center);
                }

            }
        });


        now = Calendar.getInstance();
        now.add(Calendar.HOUR, 12);
        datepickerdialog = DatePickerDialog.newInstance(
                IssueDescriptorMapActivityS3.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(false); //choose year first?
        datepickerdialog.setAccentColor(Color.parseColor("#008ba3")); // custom accent color
        datepickerdialog.setTitle("Please select a date"); //dialog title


        datepickerdialog.setMinDate(now);


        timepickerdialog = TimePickerDialog.newInstance(IssueDescriptorMapActivityS3.this,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);


        timepickerdialog.setThemeDark(false); //Dark Theme?
        timepickerdialog.vibrate(false); //vibrate on choosing time?
        timepickerdialog.dismissOnPause(false); //dismiss the dialog onPause() called?
        timepickerdialog.enableSeconds(false); //show seconds?
//        timepickerdialog.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),0);


        //Handling cancel event
        timepickerdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

//        consultNow.callOnClick();

    }


    public void createLocations() {
        locationsList.add(new LocationModel("1", "Bangalore", false));
        locationsList.add(new LocationModel("1", "Patna", false));
        locationsList.add(new LocationModel("1", "Bihar", false));
        locationsList.add(new LocationModel("1", "Chennai", false));
        locationsList.add(new LocationModel("1", "Gujarat", false));

    }

    public boolean validate() {
//        if(!Utilities.validate(IssueDescriptor.this,descTxt,"Please enter valid description Also",false,5,700))
//            return false;
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Utilities.showAlert(IssueDescriptorMapActivityS3.this, "Please select when you want to consult", false);
            return false;
        }

        return true;
    }

    void setupData() {
        g.addAppointmentInputParams("PatLoc", addressTxtVw.getText().toString());

        if (techRequired.isChecked()) {
            g.addAppointmentInputParams("isTechnician", "1");
        } else {
            g.addAppointmentInputParams("Lattitude", "");
            g.addAppointmentInputParams("Longitude", "");
            g.addAppointmentInputParams("isTechnician", "0");
        }

        g.addAppointmentInputParams("BloodPressure", "" + systolic.getText().toString() + "/" + diastolic.getText().toString());
        g.addAppointmentInputParams("PulseRate", pulse.getText().toString());
        g.addAppointmentInputParams("Oxypercentage", spo2.getText().toString());
        g.addAppointmentInputParams("Temperature", bodyTemperature.getText().toString());
        g.addAppointmentInputParams("Weight", weight.getText().toString());
        g.addAppointmentInputParams("Height", height.getText().toString());
        String covid = "";

        if (covidRG.getCheckedRadioButtonId() != -1) {
            switch (covidRG.getCheckedRadioButtonId()) {
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
        g.addAppointmentInputParams("COVID", covid);

        if(!AppPreferences.getInstance().getCountryCode().toString().equals("+1"))
        {
            GlobValues g = GlobValues.getInstance();
            appointmentinputParams = g.getAddAppointmentParams();
            Log.e(F1Questions.TAG, "onCreate: "+appointmentinputParams.toString() );
            generateComplaintID(appointmentinputParams);

        }else{
            startActivity(new Intent(IssueDescriptorMapActivityS3.this, HospitalsListActivity.class));
        }
        Utilities.pushAnimation(IssueDescriptorMapActivityS3.this);
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.addressTxtVw) {

        }
        if (v.getId() == R.id.header_select_time) {

            datepickerdialog.show(getFragmentManager(), "Datepickerdialog"); //show dialog
            tecContainerLayout.setVisibility(View.GONE);


        }
        if (v.getId() == R.id.consult_now_layout) {

            consultNow.setBackgroundColor(getResources().getColor(R.color.background_grey));
            selectTime.setBackgroundColor(getResources().getColor(R.color.white));

            selectedTime = Utilities.getCurrentTime("hh:mm");
//            selectedDateToSend = Utilities.getCurrentDate("MM/dd/yyyy");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            selectedDateToSend = dateFormat.format(calendar.getTime());

            selectedDate = selectedDateToSend;
            appointmentTime.setText("Appointment Time");

            tecContainerLayout.setVisibility(View.GONE);
            isInstant = 1;
            nextBtn.callOnClick();
        }
        if (v.getId() == R.id.next_btn) {
            setupData();




        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (now.get(Calendar.DATE) == dayOfMonth && now.get(Calendar.YEAR) == year && now.get(Calendar.MONTH) == monthOfYear) {
            Timepoint minTimePoint = new Timepoint(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            timepickerdialog.setMinTime(minTimePoint);
        } else {
            timepickerdialog.setMinTime(0, 0, 0);
        }


        selectedDateToSend = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
        selectedDate = dayOfMonth + " , " + MONTHS[monthOfYear] + ", " + year;

        timepickerdialog.setTitle("Appointment can be book after 12 hrs");
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog"); //show time picker dialog

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
//        selectedTime = hourOfDay + " : "+ minute;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        selectedTime = formatter.format(cal.getTime());

        appointmentTime.setText(selectedDate + " " + selectedTime);
        isInstant = 0;
        appointmentTime.setTextColor(ContextCompat.getColor(IssueDescriptorMapActivityS3.this, R.color.colorAccent));
        tecContainerLayout.setVisibility(View.VISIBLE);
        selectTime.setBackgroundColor(getResources().getColor(R.color.background_grey));
        consultNow.setBackgroundColor(getResources().getColor(R.color.white));

    }


    // MARK : Choose your location via location picker
    private void launchPlacePicker() {

        showPlacePickerUpgrade(new PlacePickCallbackUpdate() {
            @Override
            public void receiveSelectedPlace(Boolean status, com.google.android.libraries.places.api.model.Place selectedPlaceUpdate) {
                if (status) {

                    if (selectedPlaceUpdate != null) {

                        // Name
                        addressInfo = (selectedPlaceUpdate.getName() != null && selectedPlaceUpdate.getName().length() > 0) ? selectedPlaceUpdate.getName().toString() : addressInfo;

                        // Address
                        addressInfo = (selectedPlaceUpdate.getAddress() != null && selectedPlaceUpdate.getAddress().length() > 0) ? addressInfo + ", " + selectedPlaceUpdate.getAddress().toString() : addressInfo;

                        // Phone
                        addressInfo = (selectedPlaceUpdate.getPhoneNumber() != null && selectedPlaceUpdate.getPhoneNumber().length() > 0) ? addressInfo + ", Phone: " + selectedPlaceUpdate.getPhoneNumber().toString() : addressInfo;

                        // Location
//                        addressInfo = (selectedPlace.getLatLng() != null) ? addressInfo + ", " + selectedPlace.getLatLng() : addressInfo;
                        try {
                            isMap = true;
                            addressInfo = new Geocoder(getApplicationContext(), Locale.getDefault()).getFromLocation(selectedPlaceUpdate.getLatLng().latitude, selectedPlaceUpdate.getLatLng().longitude, 1).get(0).getAddressLine(0);
                        } catch (IOException e) {
                            isMap = false;
                            e.printStackTrace();
                        }

                        g.addAppointmentInputParams("Lattitude", "" + selectedPlaceUpdate.getLatLng().latitude);
                        g.addAppointmentInputParams("Longitude", "" + selectedPlaceUpdate.getLatLng().longitude);


                    }

                }

                // Set the address details
                if (addressInfo.length() > 0) {
                    l.setInitialInput(addressInfo);
                    addressTxtVw.setVisibility(View.VISIBLE);
                    addressTxtVw.setText(addressInfo);
                    techRequired.setChecked(true);

                }
            }
        });

    }

    private void generateComplaintID(HashMap<String, Object> appointmentinputParams) {

        SoapAPIManager apiManager = new SoapAPIManager(IssueDescriptorMapActivityS3.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(F1Questions.TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {

                                complaintID = Integer.parseInt(commonDataInfo.get("ComplaintID").toString());
                                oldComplaintID = Integer.parseInt(commonDataInfo.get("OldComplaintID").toString());
                                Intent i = new Intent(IssueDescriptorMapActivityS3.this, HealthSeekerActivity.class);
                                i.putExtra("issues", selectedIssuesModelList);
                                i.putExtra("PastIssues", getIntent().getSerializableExtra("PastIssues"));
                                startActivity(i);

                        } else {
                            Utilities.showAlert(IssueDescriptorMapActivityS3.this, "Something went wrong!", false);
                        }


                    }
                } catch (Exception e) {
                    Utilities.showAlert(IssueDescriptorMapActivityS3.this, "Something went wrong...", false);

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GENERATE_COMPLAINT_ID, "POST"};

        if (Utilities.isNetworkAvailable(IssueDescriptorMapActivityS3.this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(IssueDescriptorMapActivityS3.this, "No Internet", false);

        }
    }

}
