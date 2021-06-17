package com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.adapters.OrderSummaryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderSummaryActivity extends AppCompatActivity {

    private static final String TAG = "SummaryOrder";
    RecyclerView rv_order_summary;
    ArrayList<HashMap<String, Object>> sentParams;
    ImageView iv_back;
    TextView tv_total_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        rv_order_summary = findViewById(R.id.rv_order_summary);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_total_amt = findViewById(R.id.tv_total_amt);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("data");

        sentParams = (ArrayList<HashMap<String, Object>>) args.getSerializable("MedicineData");
        Log.e(TAG, "onCreate: " + sentParams.toString());

        OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(sentParams, getApplicationContext());

        rv_order_summary.setAdapter(orderSummaryAdapter);

        double totalAmt = 0;
        for (int i = 0; i < sentParams.size(); i++) {
            if (sentParams.get(i).containsKey("amount")) {
                totalAmt = totalAmt + (Double.parseDouble(String.valueOf(sentParams.get(i).get("amount")))
                        *
                        Double.parseDouble(String.valueOf(sentParams.get(i).get("MedicineCount"))));
            }
        }
        tv_total_amt.setText("Total: Rs " + totalAmt);


    }




    /*void placeOrder(ArrayList<HashMap<String, Object>> sentParams) {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();

        if (sentParams.size() > 0) {
            inputParams.put("objMedicineList", new JSONArray(sentParams));
        }
        if (uploadedFilesArrayList.size() > 0) {
            inputParams.put("Uploadprescription", new JSONArray(uploadedFilesArrayList));
        }
        if (sentParams.size() <= 0 && uploadedFilesArrayList.size() <= 0) {
            Utilities.showAlert(MedicineActivity.this, "No Medicine or Uploaded Prescription found.", false);
        } else {
            Log.e(TAG, "placeOrder: " + inputParams.toString());
            SoapAPIManager apiManager = new SoapAPIManager(MedicineActivity.this, inputParams, new APICallback() {
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
                                    Utilities.showAlert(MedicineActivity.this, "Please check again!", false);
                                }
                            } else {
                                Utilities.showAlert(MedicineActivity.this, "Error Occurred!", false);
                            }

                        } else {
                            Utilities.showAlert(MedicineActivity.this, "Error Occurred!", false);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "responseCallback: " + e);
                        e.getMessage();
                        Utilities.showAlert(MedicineActivity.this, "Error Occurred!", false);

                    }
                }
            }, true);
            String[] url = {Config.WEB_Services1, Config.EMAIL_MEDICINE_TO_PHARMACY, "POST"};

            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                Log.e(TAG, "placeOrder: " + url);
                apiManager.execute(url);
            } else {
                Utilities.showAlert(MedicineActivity.this, "Please check internet!", false);

            }
        }


    }*/
}