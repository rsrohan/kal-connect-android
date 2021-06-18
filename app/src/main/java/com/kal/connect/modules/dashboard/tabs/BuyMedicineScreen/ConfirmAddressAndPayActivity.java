package com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kal.connect.appconstants.APIWebServiceConstants.isTesting;

public class ConfirmAddressAndPayActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "PayANDAddress";
    ArrayList<HashMap<String, Object>> sentParams;
    ImageView iv_back;
    EditText et_name, et_phone, et_mainAddress1, et_mainAddress2, et_mainAddress3, et_city, et_state, et_pincode;
    TextView tv_proceed;
    private String pAddress = "";
    private String pName = "";
    private String pPhone = "";
    private Checkout checkout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address_and_pay);
        setUpViews();
        intent = getIntent();
        Bundle args = intent.getBundleExtra("data");

        sentParams = (ArrayList<HashMap<String, Object>>) args.getSerializable("MedicineData");
        Log.e(TAG, "onCreate: " + sentParams.toString());

        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsAreValid()) {

                    pName = et_name.getText().toString();
                    pPhone = et_phone.getText().toString();
                    pAddress = et_mainAddress1.getText().toString()
                            + ", " + et_mainAddress2.getText().toString()
                            + ", " + et_mainAddress3.getText().toString()
                            + ", " + et_city.getText().toString()
                            + ", " + et_state.getText().toString()
                            + ", " + et_pincode.getText().toString();

                    String amount = intent.getStringExtra("amount");
                    amount = amount.replace(".", "");
//                    int amountInInt = Integer.parseInt(amount);
//                    amountInInt = amountInInt*100;
                    initialiseRazorPayForPayment(amount);

                } else {
                    Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Please check the details...", false);
                }
            }
        });
    }

    private boolean allFieldsAreValid() {
        if (et_name.getText().toString().isEmpty() ||
                et_phone.getText().toString().isEmpty() ||
                et_mainAddress1.getText().toString().isEmpty() ||
                et_mainAddress2.getText().toString().isEmpty() ||
                et_city.getText().toString().isEmpty() ||
                et_state.getText().toString().isEmpty() ||
                et_pincode.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void setUpViews() {
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_mainAddress1 = findViewById(R.id.et_mainAddress1);
        et_mainAddress2 = findViewById(R.id.et_mainAddress2);
        et_mainAddress3 = findViewById(R.id.et_mainAddress3);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_pincode = findViewById(R.id.et_pincode);
        iv_back = findViewById(R.id.iv_back);
        tv_proceed = findViewById(R.id.tv_confirm_add);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();
            String fullName = "";
            fullName = ((accInfo.getString("FirstName") != null) && accInfo.getString("FirstName").length() > 0) ? accInfo.getString("FirstName") : "";
            fullName = ((accInfo.getString("LastName") != null) && accInfo.getString("LastName").length() > 0) ? fullName + " " + accInfo.getString("LastName") : "";

            if (fullName.length() > 0)
                et_name.setText(fullName);

            String mobile = "";
            mobile = ((accInfo.getString("ContactNo") != null) && accInfo.getString("ContactNo").length() > 0) ? accInfo.getString("ContactNo") : "";

            if (mobile.length() > 0)
                et_phone.setText(mobile);

            if (accInfo.getString("Addressline1") != null && accInfo.getString("Addressline1").length() > 0) {
                et_mainAddress1.setText(accInfo.getString("Addressline1"));
            }

            if (accInfo.getString("Addressline2") != null && accInfo.getString("Addressline2").length() > 0) {
                et_mainAddress2.setText(accInfo.getString("Addressline2"));
            }

            if (accInfo.has("cityname") && !accInfo.getString("cityname").isEmpty()) {
                et_city.setText(accInfo.getString("cityname"));
            }

            if (accInfo.getString("statename") != null && accInfo.getString("statename").length() > 0) {
                et_state.setText(accInfo.getString("statename"));
            }

            if (accInfo.getString("Zipcode") != null && accInfo.getString("Zipcode").length() > 0) {
                et_pincode.setText(accInfo.getString("Zipcode"));
            }


        } catch (Exception e) {
            Log.e(TAG, "setUpViews: " + e);
        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        placeOrder(sentParams, pName, pPhone, pAddress, s);
    }

    @Override
    public void onPaymentError(int i, String s) {
//        if (isTesting){
//            showAlert("You are testing...");
//        }else{
        Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Payment Failed! If money deducted, it will be refunded.", false);
        // }
    }

    void placeOrder(ArrayList<HashMap<String, Object>> sentParams, String pName, String pPhone, String pAddress, String payId) {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();

        if (sentParams.size() > 0) {
            inputParams.put("objMedicineList", new JSONArray(sentParams));
        }
        inputParams.put("PatientName", pName);
        inputParams.put("PatientPhone", pPhone);
        inputParams.put("PatientAddress", pAddress);
        inputParams.put("PaymentId", payId);

        Log.e(TAG, "placeOrder: " + inputParams.toString());
        SoapAPIManager apiManager = new SoapAPIManager(ConfirmAddressAndPayActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText")) {
//                                Utilities.showAlert(mContext, commonDataInfo.getString("RespText"), false);
                                showAlert(commonDataInfo.getString("RespText"));
                            } else {
                                Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Please check again!", false);
                            }
                        } else {
                            Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Error Occurred!", false);
                        }

                    } else {
                        Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Error Occurred!", false);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "responseCallback: " + e);
                    e.getMessage();
                    Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Error Occurred!", false);

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.EMAIL_MEDICINE_TO_PHARMACY, "POST"};

        if (Utilities.isNetworkAvailable(getApplicationContext())) {
            Log.e(TAG, "placeOrder: " + url);
            apiManager.execute(url);
        } else {
            Utilities.showAlert(ConfirmAddressAndPayActivity.this, "Please check internet!", false);

        }


    }

    void showAlert(String message) {
        Intent i = new Intent(getApplicationContext(), OrderSuccessfulActivity.class);
        i.putExtra("message", message);
        startActivity(i);

    }

    private void initialiseRazorPayForPayment(String consultAmount) {
        checkout = new Checkout();
        checkout.setImage(R.drawable.kerala);
        checkout.setKeyID("rzp_live_Zg6HCW1SoWec80");
//        checkout.setKeyID("rzp_test_FluUs9dnT8NZLB");

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Amount to be paid...");
            options.put("image", "");
            //options.put("order_id", orderId);
            options.put("currency", "INR");
            JSONObject preFill = new JSONObject();
//            preFill.put("email", AppPreferences.loadPreferences(getApplicationContext(), "email"));
//            preFill.put("contact", user.getPhoneNumber());

            options.put("prefill", preFill);
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", consultAmount + "");

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }


    }
}