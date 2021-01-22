package com.kal.connect.modules.communicate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.modules.dashboard.DashboardMapActivity;

public class CallEndedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_ended);
        TextView tv_message = findViewById(R.id.tv_message);
        String s = "";
        s = getIntent().getStringExtra("message");
        tv_message.setText(s);
        TextView tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeScreen = new Intent(getApplicationContext(), DashboardMapActivity.class);
                homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeScreen);
                finish();
            }
        });
    }
}