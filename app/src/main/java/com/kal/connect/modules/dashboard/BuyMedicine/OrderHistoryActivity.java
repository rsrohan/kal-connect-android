package com.kal.connect.modules.dashboard.BuyMedicine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kal.connect.R;
import com.kal.connect.adapters.OrderHistoryAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String TAG = "OrderHistory";
    ImageView iv_back, not_found;
    String specialistID="";
    SharedPreferences pref;
    RecyclerView rvOrderHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        iv_back = findViewById(R.id.iv_back);
        not_found = findViewById(R.id.not_found);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("noWayHome", false)){
                    Intent i = new Intent(OrderHistoryActivity.this, DashboardMapActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else{
                    finish();
                }
            }
        });

        getOrderHistory(specialistID);
    }

    private void getOrderHistory(String userID) {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();

        inputParams.put("UserID", inputParams.get("Patientid").toString());
        inputParams.put("UserType", "4");

        Log.e(TAG, "orderHistory: " + inputParams.toString());
        SoapAPIManager apiManager = new SoapAPIManager(OrderHistoryActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {

                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("TotalOrders") && Integer.parseInt(commonDataInfo.getString("TotalOrders")) > 0){

                                ArrayList<HashMap<String, Object>> items = new ArrayList<>();
                                JSONArray orders = new JSONArray(commonDataInfo.get("OrderHistory").toString());
                                for (int i=0;i<orders.length();i++) {
                                    JSONObject data = orders.getJSONObject(i);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("OrderID", data.get("OrderID").toString());
                                    hashMap.put("UserID", data.get("UserID").toString());
                                    hashMap.put("OrderStatus", data.get("OrderStatus").toString());
                                    hashMap.put("OrderStatusString", data.get("OrderStatusString").toString());
                                    hashMap.put("OrderDescription", data.get("OrderDescription").toString());
                                    hashMap.put("OrderTotalPrice", data.get("OrderTotalPrice").toString());
                                    hashMap.put("OrderActualDate", data.get("OrderActualDate").toString());
                                    hashMap.put("PaymentMode", data.get("PaymentMode").toString());
                                    hashMap.put("OrderDate", data.get("OrderDate").toString());
                                    hashMap.put("OrderCompleteID", data.get("OrderCompleteID").toString());
                                    items.add(hashMap);
                                }

                                OrderHistoryAdapter earningsAdapter = new OrderHistoryAdapter(getApplicationContext(), items);
                                rvOrderHistory.setAdapter(earningsAdapter);

                            }else{
                                not_found.setVisibility(View.VISIBLE);
                            }
                        } else {
                            not_found.setVisibility(View.VISIBLE);
                        }

                    } else {
                        not_found.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "responseCallback: " + e);
                    not_found.setVisibility(View.VISIBLE);
                    //Utilities.showAlert(OrderHistoryActivity.this, "Error Occurred!", false);

                }
            }
        }, true);
        String[] url = {Config.WEB_Services5, Config.GET_ORDER_HISTORY, "POST"};

        if (Utilities.isNetworkAvailable(getApplicationContext())) {
            Log.e(TAG, "orderHistory: " + url);
            apiManager.execute(url);
        } else {
            Utilities.showAlert(OrderHistoryActivity.this, "Please check internet!", false);

        }

    }

    @Override
    public void onBackPressed() {
        iv_back.performClick();
    }

}