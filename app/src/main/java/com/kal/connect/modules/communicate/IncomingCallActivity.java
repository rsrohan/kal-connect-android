package com.kal.connect.modules.communicate;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
//import androidx.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;


import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IncomingCallActivity extends CustomActivity {


    @BindView(R.id.caller)
    TextView callerName;

    @BindView(R.id.timer)
    TextView tv_timer;

    @BindView(R.id.callState)
    TextView callType;
    String SpecialistID = "";
    private CountDownTimer countDownTimer;

    @OnClick(R.id.declineButton)
    void disconnectCall() {
        if (ringtone != null) {
            ringtone.stop();
        }
        if (Config.isDisconnect) {
            moveToHome();
        } else {
            getEndCall();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if (ringtone.isPlaying()){
                ringtone.stop();
            }
        }catch (Exception e){}

    }

    @OnClick(R.id.answerButton)
    void connectCall() {
        ringtone.stop();
        finish();

        Intent videoIntent = new Intent(IncomingCallActivity.this, VideoConferenceActivity.class);

        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (isTaskRoot()) {
            videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        videoIntent.putExtra("CALL_TYPE", 1);
        startActivity(videoIntent);
    }

    Ringtone ringtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if(keyguardManager!=null)
                keyguardManager.requestDismissKeyguard(this, null);
        }
        else
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        setContentView(R.layout.activity_incoming_video_call);
        Config.mActivity = this;
        buildUI();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    void playRingTone() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
        }
        ringtone.play();
    }

    public void moveToHome() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (isTaskRoot()) {
            //// This is last activity
            Intent homeScreen = new Intent(getApplicationContext(), DashboardMapActivity.class);
            homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeScreen);
        } else {
            //// There are more activities in stack
            finish();
        }
    }

    public void buildUI() {
        ButterKnife.bind(this);
        try {
            if (getIntent().getExtras().containsKey("CALER_NAME") && getIntent().getExtras().getString("CALER_NAME") != null && !getIntent().getExtras().getString("CALER_NAME").isEmpty()) {
                callerName.setText(getIntent().getExtras().getString("CALER_NAME"));
            }
            if (getIntent().getExtras().containsKey("SpecialistID") && getIntent().getExtras().getString("SpecialistID") != null) {
                SpecialistID = getIntent().getExtras().getString("SpecialistID");
            }
        } catch (Exception e) {
        }
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                

            }
        };
        countDownTimer.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ringtone.isPlaying())
                    ringtone.stop();
                moveToHome();

            }
        }, 60000);

        playRingTone();

    }

    public void getEndCall() {

        String docId = "";
        Bundle mBundle = null;
        if (getIntent().getExtras() != null) {
            mBundle = getIntent().getExtras();
        }
        if (mBundle != null && mBundle.containsKey("DocterId")) {
            docId = mBundle.getString("DocterId");
        } else {
            docId = SpecialistID;
        }
        HashMap<String, Object> endCallParams = new HashMap<>();
        endCallParams.put("User_id", docId);
        endCallParams.put("SpecialistID", docId);

        JSONObject accInfo = AppPreferences.getInstance().getUserInfo();

        try {
            endCallParams.put("PatientID", accInfo.getString("PatientID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SoapAPIManager apiManager = new SoapAPIManager(IncomingCallActivity.this, endCallParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {

                JSONArray mJsonArray = new JSONArray(response);
                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                if (mJsonObject.has("APIStatus")) {
                    if (mJsonObject.getString("APIStatus").equalsIgnoreCase("1")) {
                        moveToHome();
                    }
                }

            }
        }, true);
        String[] url = {Config.WEB_Services4, Config.END_CALL, "POST"};

        if (Utilities.isNetworkAvailable(IncomingCallActivity.this)) {
            apiManager.execute(url);
        } else {

        }
    }
}
