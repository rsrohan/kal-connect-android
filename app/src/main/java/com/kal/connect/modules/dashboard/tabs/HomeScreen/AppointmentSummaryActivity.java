package com.kal.connect.modules.dashboard.tabs.HomeScreen;

import android.app.Activity;
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
import com.kal.connect.customdialogbox.FlipProgressDialog;
import com.kal.connect.models.DoctorModel;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.models.NotesModel;
import com.kal.connect.models.RazerPayOrderModel;
import com.kal.connect.models.TransferModel;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.modules.communicate.VideoConferenceActivity;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
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

import static com.kal.connect.appconstants.APIWebServiceConstants.isTesting;

public class AppointmentSummaryActivity extends CustomActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = "AppointmentSummary";
    RecyclerView selectedRecyclerView;
    LinearLayout tecLayout;
    SelectedIssueAdapter selectedIssueAdapter;
    private static HashMap<String, Object> appointmentinputParams;
    TextView description, appointmentTime, techAddress, docCategory, docDegere, docName, docLocation, consultCharge,
            technicianCharge;
    Button paymentBtn;
    Boolean isPay = false;
    FlipProgressDialog pd;

    int cosultChargeAmount = 100;

    String orderID, specialistID = "";

    @BindView(R.id.hospital_name)
    TextView hospitalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_summary);
        ButterKnife.bind(this);
        buildUI();
    }

    public void buildUI() {

        setHeaderView(R.id.headerView, AppointmentSummaryActivity.this, AppointmentSummaryActivity.this.getResources().getString(R.string.appointment_summary_title));
        headerView.showBackOption();

        selectedRecyclerView = (RecyclerView) findViewById(R.id.issuesSelectedRecyclerVW);
        tecLayout = (LinearLayout) findViewById(R.id.tec_layout);

        selectedIssueAdapter = new SelectedIssueAdapter(GlobValues.getInstance().getSelectedIssuesModelList(), AppointmentSummaryActivity.this, null);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AppointmentSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false);
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
        if(AppPreferences.getInstance().getCountryCode().toString().equals("+91")){
            consultCharge.setText(selectedDoctor.getDocCharge().isEmpty() ? "" : "Rs " + selectedDoctor.getVCCharge().toString());

        }else{
            consultCharge.setText(selectedDoctor.getDocIntCharge().isEmpty() ? selectedDoctor.getDocCharge().isEmpty() ? "" : "Rs " + selectedDoctor.getVCCharge().toString() : "Rs " + selectedDoctor.getDocIntCharge().toString());

        }


        if(AppPreferences.getInstance().getCountryCode().toString().equals("+91")) {
            if (!selectedDoctor.getVCCharge().equals("")) {
                cosultChargeAmount = (int) (Double.parseDouble(selectedDoctor.getVCCharge().toString()) * 100);
                Log.e(TAG, "setupDetails: "+cosultChargeAmount );
            }
        }else{
            if (!selectedDoctor.getDocIntCharge().equals("")) {
                cosultChargeAmount = (int) (Double.parseDouble(selectedDoctor.getDocIntCharge().toString()) * 100);
            }else{
                if (!selectedDoctor.getVCCharge().equals("")) {
                    cosultChargeAmount = (int) (Double.parseDouble(selectedDoctor.getVCCharge().toString()) * 100);
                }
            }
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

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummaryActivity.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("Message") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummaryActivity.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummaryActivity.this, "Please check again!", false);
                            }
                            return;

                        }
                        startActivity(new Intent(AppointmentSummaryActivity.this, DashboardMapActivity.class));

                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.BOOK_APPOINTMENT, "POST"};

        if (Utilities.isNetworkAvailable(AppointmentSummaryActivity.this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(AppointmentSummaryActivity.this, "No Internet", false);

        }
    }

    public void getVideoCallConfigurations(String paymentId, String amount) {

        appointmentinputParams.put("paymentId", paymentId);
        appointmentinputParams.put("amount", amount);

        Log.e(TAG, appointmentinputParams.toString());

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummaryActivity.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummaryActivity.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummaryActivity.this, "Please check again!", false);
                            }
                            return;

                        }
                        if (commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()) {
                            String TOKEN = commonDataInfo.getString("VCToekn");
                            String SESSION = commonDataInfo.getString("VSSessionID");

                            Intent intent = new Intent(AppointmentSummaryActivity.this, VideoConferenceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            OpenTokConfigConstants.SESSION_ID = SESSION;
                            OpenTokConfigConstants.TOKEN = TOKEN;

                            intent.putExtra("CALER_NAME", docName.getText().toString());
                            intent.putExtra("CALL_TYPE", 2);
                            intent.putExtra("docId", specialistID);
                            Log.e(TAG, "responseCallback: "+specialistID );
                            startActivity(intent);

                            Utilities.pushAnimation(AppointmentSummaryActivity.this);

                        }


                    }
                } catch (Exception e) {
                    Utilities.showAlert(AppointmentSummaryActivity.this, "Something went wrong...", false);

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.INITIATE_VIDEO_CALL, "POST"};

        if (Utilities.isNetworkAvailable(AppointmentSummaryActivity.this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(AppointmentSummaryActivity.this, "No Internet", false);

        }
    }


    void showOrderErrorMessage() {
        Utilities.showAlert(this, "Issue while creating order, Please try again!", false);

    }

    void createRazorPayOrder() {

        try {


            RazerPayOrderModel razorPayOrder = new RazerPayOrderModel();
            razorPayOrder.setAmount(cosultChargeAmount);
            razorPayOrder.setCurrency("INR");
            razorPayOrder.setPaymentCapture(1);


            NotesModel transferNotesModel = new NotesModel();
            transferNotesModel.setAppointmentDate(appointmentinputParams.get("AppointmentDate").toString());
            transferNotesModel.setAppointmentTime(appointmentinputParams.get("AppointmentTime").toString());
            transferNotesModel.setConsultationMode(appointmentinputParams.get("ConsultationMode").toString());
            transferNotesModel.setSpecialistName(appointmentinputParams.get("SpecialistName").toString());
            transferNotesModel.setSpecialistID((Integer) appointmentinputParams.get("SpecialistID"));
            transferNotesModel.setComplaintID(appointmentinputParams.get("ComplaintID").toString());
            transferNotesModel.setPatientID(appointmentinputParams.get("patientID").toString());
            transferNotesModel.setIsTechnician(appointmentinputParams.get("isTechnician").toString());
            transferNotesModel.setPatientName(appointmentinputParams.get("PatientName").toString());

            TransferModel hospitalTranfer = new TransferModel();


            hospitalTranfer.setAccount("acc_FO4DrKlapsbqas");

            hospitalTranfer.setAmount(100);
            hospitalTranfer.setCurrency("INR");
            hospitalTranfer.setNotesModel(transferNotesModel);
            hospitalTranfer.setOnHold(0);

            TransferModel medi360TransferModel = new TransferModel();

            medi360TransferModel.setAccount("acc_FO4I5Y8usyFNt8");
            medi360TransferModel.setAmount(100);
            medi360TransferModel.setCurrency("INR");
            medi360TransferModel.setNotesModel(transferNotesModel);
            medi360TransferModel.setOnHold(0);

            List<TransferModel> transferModels = new ArrayList<>();
            transferModels.add(hospitalTranfer);
            transferModels.add(medi360TransferModel);


            Gson gson = new Gson();
            String inpurtRazorPayOrder = gson.toJson(razorPayOrder);

            pd = Utilities.showLoading(this);

            ///

            //request a json object response
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Config.CREATE_ORDER, new JSONObject(inpurtRazorPayOrder), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();

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
                    pd.dismiss();
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
            pd.dismiss();
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
            Log.e(TAG, "startPayment: "+cosultChargeAmount );

            co.open(activity, options);
        } catch (Exception e) {
            Utilities.showAlert(AppointmentSummaryActivity.this, getString(R.string.payment_failure), false);
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
        Utilities.showAlert(AppointmentSummaryActivity.this, getString(R.string.payment_failure), false);
        if (isTesting){
            getVideoCallConfigurations(s, String.valueOf(cosultChargeAmount));
        }
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

        SoapAPIManager apiManager = new SoapAPIManager(AppointmentSummaryActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(AppointmentSummaryActivity.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(AppointmentSummaryActivity.this, "Please check again!", false);
                            }
                            return;
                        }
                        if (commonDataInfo.getString("isPay").equalsIgnoreCase("true")) {
                            startPayment();
                        } else {
                            if ((int) GlobValues.getAddAppointmentParams().get("ConsultNow") == 2) {
                                bookAppointment("", "");
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

        if (Utilities.isNetworkAvailable(AppointmentSummaryActivity.this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(AppointmentSummaryActivity.this, "No Internet", false);
        }
    }
}
