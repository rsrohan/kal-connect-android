package com.kal.connect.modules.dashboard.BuyMedicine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.modules.dashboard.DashboardMapActivity;

public class OrderSuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successful);
        TextView tv_message = findViewById(R.id.tv_message);
        String s = "";
        s = getIntent().getStringExtra("message");
        tv_message.setText(s);
        TextView tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderSuccessfulActivity.this, DashboardMapActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}