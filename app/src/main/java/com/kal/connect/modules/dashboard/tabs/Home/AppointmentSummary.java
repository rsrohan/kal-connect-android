package com.kal.connect.modules.dashboard.tabs.Home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.adapters.SelectedIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.DoctorModel;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.models.Notes;
import com.kal.connect.models.RazerPayOrder;
import com.kal.connect.models.Transfer;
import com.kal.connect.modules.communicate.OpenTokConfig;
import com.kal.connect.modules.communicate.VideoConference;
import com.kal.connect.modules.dashboard.Dashboard;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSummary extends CustomActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = "AppointmentSummary";
    RecyclerView selectedRecyclerView;
    LinearLayout tecLayout;
    SelectedIssueAdapter selectedIssueAdapter;
    private static HashMap<String, Object> appointmentinputParams;
    TextView description, appointmentTime, techAddress, docCategory, docDegere, docName, docLocation, consultCharge,
            technicianCharge;
    Button paymentBtn;
    Boolean isPay = false;
    ProgressDialog pd;

    int cosultChargeAmount = 100;

    String orderID, specialistID = "";

    @BindView(R.id.hospital_name)
    TextView hospitalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_summary);
        ButterKnife.bind(this);
        buildUI();
    }

    public void buildUI() {

        setHeaderView(R.id.headerView, AppointmentSummary.this, AppointmentSummary.this.getResources().getString(R.string.appointment_summary_title));
        headerView.showBackOption();

        selectedRecyclerView = (RecyclerView) findViewById(R.id.issuesSelectedRecyclerVW);
        tecLayout = (LinearLayout) findViewById(R.id.tec_layout);

        selectedIssueAdapter = new SelectedIssueAdapter(GlobValues.getInstance().getSelectedIssuesList(), AppointmentSummary.this, null);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AppointmentSummary.this, LinearLayoutManager.HORIZONTAL, false);
        selectedRecyclerView.setLayoutManager(horizontalLayoutManager);
        selectedRecyclerView.setAdapter(selectedIssueAdapter);
        description = (TextView) findViewById(R.id.description_edit_txt);

        techAddress = (TextView) findViewById(R.id.address);
        appointmentTime = (TextView) findViewById(R.id.appointmentTime);
        docCategory = (TextView) findViewById(R.id.doctor_category);
        description = (TextView) findViewById(R.id.description_edit_txt);
        docDegere = (TextView) findViewById(R.id.doctor_qualification);
        docLocation = (TextView) findViewById(R.id.location_txt_vw);
        docName = (TextView) findViewById(R.id.doctor_name);
        consultCharge = (TextView) findViewById(R.id.consult_charge);
        technicianCharge = (TextView) findViewById(R.id.tech_charge);

        paymentBtn = (Button) findViewById(R.id.next_btn);
        paymentBtn.setOnClickListener(this);

        setupDetails();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey("isPay")) {
                isPay = mBundle.getBoolean("isPay");
            }
            if (mBundle.containsKey("specialistID")) {
                specialistID = mBundle.getString("specialistID");
            }
        }

    }

    void setupDetails() {
        GlobValues g = GlobValues.getInstance();

        appointmentinputParams = g.getAddAppointmentParams();


        if (appointmentinputParams != null) {
            description.setText(appointmentinputParams.get("ComplaintDescp").toString());
        }
        if (appointmentinputParams != null && appointmentinputParams.get("ComplaintDescp").toString().isEmpty()) {
            description.setVisibility(View.GONE);
        }

        DoctorModel selectedDoctor = null;
        HospitalModel selectedHospital = null;
        if (appointmentinputParams != null) {
            String appointmentDateStr = Utilities.changeStringFormat(appointmentinputParams.get("AppointmentDate").toString(), "mm/dd/yyyy", "dd/mm/yyyy");

            appointmentTime.setText(appointmentDateStr + " " + appointmentinputParams.get("AppointmentTime").toString());
            selectedDoctor = g.getDoctor();

            selectedHospital = Utilities.selectedHospitalModel;

            if (Integer.parseInt(appointmentinputParams.get("isTechnician").toString()) == 1) {
                tecLayout.setVisibility(View.VISIBLE);
                //            techAddress.setText(appointmentinputParams.get("").toString());
                technicianCharge.setText(selectedDoctor.getTechnicianCharge() != null && selectedDoctor.getTechnicianCharge().isEmpty() ? "Rs 0" : "Rs " + selectedDoctor.getDocCharge());

            } else {
                tecLayout.setVisibility(View.GONE);
            }

            if (Integer.parseInt(appointmentinputParams.get("ConsultNow").toString()) == 1) {
                paymentBtn.setText(R.string.consult_now);
            } else {
                paymentBtn.setText(R.string.schedule_appointment);
            }
        }
        docCategory.setText(selectedDoctor.getSpecializationName());
        docDegere.setText(selectedDoctor.getQualification());
        docName.setText(selectedDoctor.getName());
        docLocation.setText(selectedDoctor.getLocation());

        hospitalName.setText(selectedHospital.getHospitalName());

        consultCharge.setText(selectedDoctor.getDocCharge().isEmpty() ? "" : "Rs " + selectedDoctor.getVCCharge().toString());


        if (selectedDoctor.getVCCharge() != null) {
            cosultChargeAmount = (int) (Double.parseDouble(selectedDoctor.getVCCharge().toString()) * 100);
        }



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_btn) {

            createRazorPayOrder();
        }

    }


    void bookAppointment(String paymentId, String amount) {

        appointmentinputParams.put("paymentId", paymentId);
        appointmentinputParams.put("amount", amount);

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummary.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("Message") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummary.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummary.this, "Please check again!", false);
                            }
                            return;

                        }
                        startActivity(new Intent(AppointmentSummary.this, Dashboard.class));

                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.BOOK_APPOINTMENT, "POST"};

        if (Utilities.isNetworkAvailable(AppointmentSummary.this)) {
            apiManager.execute(url);
        } else {

        }
    }

    public void getVideoCallConfigurations(String paymentId, String amount) {

        appointmentinputParams.put("paymentId", paymentId);
        appointmentinputParams.put("amount", amount);

        Log.e(TAG, appointmentinputParams.toString());

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummary.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummary.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummary.this, "Please check again!", false);
                            }
                            return;

                        }
                        if (commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()) {
                            String TOKEN = commonDataInfo.getString("VCToekn");
                            String SESSION = commonDataInfo.getString("VSSessionID");

                            Intent intent = new Intent(AppointmentSummary.this, VideoConference.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            OpenTokConfig.SESSION_ID = SESSION;
                            OpenTokConfig.TOKEN = TOKEN;

                            intent.putExtra("CALER_NAME", docName.getText().toString());
                            intent.putExtra("CALL_TYPE", 2);
                            startActivity(intent);

                            Utilities.pushAnimation(AppointmentSummary.this);

                        }


                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.INITIATE_VIDEO_CALL, "POST"};

        if (Utilities.isNetworkAvailable(AppointmentSummary.this)) {
            apiManager.execute(url);
        }
    }


    void showOrderErrorMessage() {
        Utilities.showAlert(this, "Issue while creating order, Please try again!", false);

    }

    void createRazorPayOrder() {

        try {


            RazerPayOrder razorPayOrder = new RazerPayOrder();
            razorPayOrder.setAmount(cosultChargeAmount);
            razorPayOrder.setCurrency("INR");
            razorPayOrder.setPaymentCapture(1);


            Notes transferNotes = new Notes();
            transferNotes.setAppointmentDate(appointmentinputParams.get("AppointmentDate").toString());
            transferNotes.setAppointmentTime(appointmentinputParams.get("AppointmentTime").toString());
            transferNotes.setConsultationMode(appointmentinputParams.get("ConsultationMode").toString());
            transferNotes.setSpecialistName(appointmentinputParams.get("SpecialistName").toString());
            transferNotes.setSpecialistID((Integer) appointmentinputParams.get("SpecialistID"));
            transferNotes.setComplaintID(appointmentinputParams.get("ComplaintID").toString());
            transferNotes.setPatientID(appointmentinputParams.get("patientID").toString());
            transferNotes.setIsTechnician(appointmentinputParams.get("isTechnician").toString());
            transferNotes.setPatientName(appointmentinputParams.get("PatientName").toString());

            Transfer hospitalTranfer = new Transfer();


            hospitalTranfer.setAccount("acc_FO4DrKlapsbqas");

            hospitalTranfer.setAmount(100);
            hospitalTranfer.setCurrency("INR");
            hospitalTranfer.setNotes(transferNotes);
            hospitalTranfer.setOnHold(0);

            Transfer medi360Transfer = new Transfer();

            medi360Transfer.setAccount("acc_FO4I5Y8usyFNt8");
            medi360Transfer.setAmount(100);
            medi360Transfer.setCurrency("INR");
            medi360Transfer.setNotes(transferNotes);
            medi360Transfer.setOnHold(0);

            List<Transfer> transfers = new ArrayList<>();
            transfers.add(hospitalTranfer);
            transfers.add(medi360Transfer);


            Gson gson = new Gson();
            String inpurtRazorPayOrder = gson.toJson(razorPayOrder);

            pd = Utilities.showLoading(this);

            ///

            //request a json object response
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Config.CREATE_ORDER, new JSONObject(inpurtRazorPayOrder), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.hide();

                    //now handle the response
                    try {
                        if (response.has("id") && response.get("id") != null) {
                            orderID = response.get("id").toString();


                            getCheckingPaymentStatus();


                            return;
                        }

                    } catch (Exception e) {

                    }


                    showOrderErrorMessage();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.hide();
                    showOrderErrorMessage();
                }
            }) {    //this is the part, that adds the header to the request
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();


                    String encoded =
                            Base64Utils.encode(
                                    String.format("%s:%s", "rzp_live_Zg6HCW1SoWec80", "OlKwuWIvFPOHs8hJEIt4H8YH")
                                            .getBytes()
                            );
                    params.put("Authorization", "Basic " + encoded);

                    params.put("content-type", "application/json");
                    return params;
                }
            };

            // Add the request to the queue
            Volley.newRequestQueue(this).add(jsonRequest);


        } catch (Exception e) {
            pd.hide();
            e.printStackTrace();
        }


    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_live_Zg6HCW1SoWec80");
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", Utilities.selectedHospitalModel.getHospitalName());

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Payment for Ayurvedha consultation #" + orderID);
//            options.put("image", "https://www.medi360.in/images/logo.png");
//            options.put("image", "https://www.ayurvedaacademy.com/KAA_Logo_Horizontal_200_no_bg_green_white_text.png");

            options.put("order_id", orderID);
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", cosultChargeAmount);

            co.open(activity, options);
        } catch (Exception e) {
            Utilities.showAlert(AppointmentSummary.this, getString(R.string.payment_failure), false);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {


        if ((int) GlobValues.getAddAppointmentParams().get("ConsultNow") == 2) {
            bookAppointment(s, String.valueOf(cosultChargeAmount));
        } else {
            getVideoCallConfigurations(s, String.valueOf(cosultChargeAmount));
        }

        Log.v("onPaymentSuccess : ", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Utilities.showAlert(AppointmentSummary.this, getString(R.string.payment_failure), false);
    }


    void getCheckingPaymentStatus() {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        HashMap<String, Object> appointmentParms = GlobValues.getInstance().getAddAppointmentParams();



        inputParams.put("AppointmentDate", appointmentParms.get("AppointmentDate").toString());
        if (specialistID != null && !specialistID.equalsIgnoreCase("")) {
            inputParams.put("SpecialistID", specialistID);
        } else {
            inputParams.put("SpecialistID", appointmentParms.get("SpecialistID"));
        }

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummary.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummary.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummary.this, "Please check again!", false);
                            }
                            return;
                        }
                        if (commonDataInfo.getString("isPay").equalsIgnoreCase("true")) {
                            startPayment();
                        } else {
                            if ((int) GlobValues.getAddAppointmentParams().get("ConsultNow") == 2) {
                                bookAppointment("","");
                            } else {
                                getVideoCallConfigurations("", "");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_CHECKING_PAYMENT_STATUS, "POST"};

        if (Utilities.isNetworkAvailable(AppointmentSummary.this)) {
            apiManager.execute(url);
        } else {

        }
    }
}
