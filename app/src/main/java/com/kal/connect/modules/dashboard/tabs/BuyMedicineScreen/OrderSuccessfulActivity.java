package com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.internal.$Gson$Preconditions;
import com.kal.connect.R;

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
                finish();
            }
        });
    }
}