package com.kal.connect.modules.communicate;

import android.app.ActivityManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import androidx.annotation.Nullable;
import android.widget.TextView;


import com.kal.connect.R;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.Dashboard;

import androidx.annotation.Nullable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IncommingCall extends CustomActivity {


    @BindView(R.id.caller)
    TextView callerName;

    @BindView(R.id.callState)
    TextView callType;

    @OnClick(R.id.declineButton)
    void disconnectCall()
    {
        if(ringtone != null)
        {
            ringtone.stop();
        }
        moveToHome();
    }

    @OnClick(R.id.answerButton)
    void connectCall()
    {
        ringtone.stop();
        finish();

        Intent videoIntent = new Intent(IncommingCall.this, VideoConference.class);

        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(isTaskRoot()){
            videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        videoIntent.putExtra("CALL_TYPE",1);
        startActivity(videoIntent);
    }

    Ringtone ringtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_video_call);
        buildUI();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    void playRingTone()
    {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this,uri);
        ringtone.play();
    }

    void moveToHome(){
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(isTaskRoot()){
            //// This is last activity
            Intent homeScreen = new Intent(getApplicationContext(), Dashboard.class);
            homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeScreen);
        } else{
            //// There are more activities in stack
            finish();
        }
    }
    public void buildUI()
    {
        ButterKnife.bind(this);
        try{
            if(!getIntent().getExtras().getString("CALER_NAME").isEmpty())
            {
                callerName.setText(getIntent().getExtras().getString("CALER_NAME"));
            }
        }catch (Exception e){}

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ringtone.isPlaying())
                    ringtone.stop();
                moveToHome();

            }
        }, 45000);

        playRingTone();

    }
}
