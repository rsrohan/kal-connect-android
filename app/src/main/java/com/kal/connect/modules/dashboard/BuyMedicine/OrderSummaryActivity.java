package com.kal.connect.modules.dashboard.BuyMedicine;

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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderSummaryActivity extends AppCompatActivity {

    private static final String TAG = "SummaryOrder";
    RecyclerView rv_order_summary;
    ArrayList<HashMap<String, Object>> sentParams;
    ImageView iv_back;
    TextView tv_total_amt, tv_confirm_add, tv_del_charges;
    private double totalAmt;
    public static double shippingCharges = 49;
    boolean orderBelow500 = false;

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
        tv_confirm_add = findViewById(R.id.tv_confirm_add);
        tv_del_charges = findViewById(R.id.txt_del_charge);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("data");

        sentParams = (ArrayList<HashMap<String, Object>>) args.getSerializable("MedicineData");
        Log.e(TAG, "onCreate: " + sentParams.toString());

        OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(sentParams, getApplicationContext());

        rv_order_summary.setAdapter(orderSummaryAdapter);
        DecimalFormat precision = new DecimalFormat("0.00");

        totalAmt = 0;
        for (int i = 0; i < sentParams.size(); i++) {
            if (sentParams.get(i).containsKey("amount")) {
                totalAmt = totalAmt + (Double.parseDouble(String.valueOf(sentParams.get(i).get("amount")))
                        *
                        Double.parseDouble(String.valueOf(sentParams.get(i).get("MedicineCount"))));
            }
        }
        if (totalAmt<500){
            tv_total_amt.setText("Total: Rs " + precision.format(totalAmt+shippingCharges));
            orderBelow500 = true;
            tv_del_charges.setText("Delivery charges of Rs 49.00 is applicable for the orders below value of Rs 500.00");

        }else{
            tv_total_amt.setText("Total: Rs " + precision.format(totalAmt));
            orderBelow500 = false;
            tv_del_charges.setText("Free Delivery!!!");

        }
// dblVariable is a number variable and not a String in this case
        //totalAmt = (precision.format(totalAmt));

        tv_confirm_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryActivity.this, ConfirmAddressAndPayActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("MedicineData", (Serializable) sentParams);
                i.putExtra("data", args);
                i.putExtra("amount", precision.format(totalAmt));
                i.putExtra("delCharge", orderBelow500);
                startActivity(i);
            }
        });


    }




    /**/
}