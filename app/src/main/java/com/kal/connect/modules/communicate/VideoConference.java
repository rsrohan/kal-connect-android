package com.kal.connect.modules.communicate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout ;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Splash;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.kal.connect.modules.dashboard.Dashboard;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoConference extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        Publisher.PublisherListener,
        SubscriberKit.SubscriberListener,
        Session.SessionListener, SubscriberKit.VideoListener {

    private static final String TAG = "VideoConference";
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private Publisher mPublisher;

    Boolean isEndCall = false;

    private ArrayList<Subscriber> mSubscribers = new ArrayList<Subscriber>();
    private HashMap<Stream, Subscriber> mSubscriberStreams = new HashMap<Stream, Subscriber>();

    private ConstraintLayout mContainer;
    CountDownTimer countDownTimer;

    @BindView(R.id.initial)
    TextView initialText;

    @BindView(R.id.doctorName)
    TextView doctorName;

    @BindView(R.id.timer)
    TextView timer;

    @BindView(R.id.dialerVw)
    RelativeLayout dialerView;

    @BindView(R.id.videoCallLayout)
    RelativeLayout videoCallLayout;

    @BindView(R.id.temp_publisher_id)
    FrameLayout mPublisherViewContainer;

    @BindView(R.id.temp_subscriber_id)
    FrameLayout mSubscriberViewContainer;

    @BindView(R.id.temp_subscriber_id_additional)
    FrameLayout mSubscriberViewContainerAdditional;

    @BindView(R.id.video_disabled_video)
    RelativeLayout videoDisabledView;

    @BindView(R.id.video_disabled_additional)
    RelativeLayout videoDisabledViewAdditional;

    @BindView(R.id.ll_temp_subscriper)
    LinearLayout mLlVideoSubscriberRoot;

    @BindView(R.id.ll_subscriber_id_additional)
    LinearLayout mLlVideoSubscriberRootAdditional;

//    private FrameLayout mPublisherViewContainer;
//    private FrameLayout mSubscriberViewContainer;
//    private String sessionId,token,callerName;
//    String APIKEY = "45467302";

    private Subscriber mSubscriber, mSubscriberAdditional;

    boolean isMovingToHome = false;


    @OnClick(R.id.disconnect)
    void disconnectCall() {
        if (Config.isDisconnect) {
            moveToHome();
        } else {
            getEndCall();
        }
    }

    @OnClick(R.id.switch_camera)
    void switchCamera() {
        try {
            if (mPublisher != null)
                mPublisher.cycleCamera();
        } catch (Exception e) {
        }

    }

    @OnClick(R.id.dialer_disconnect)
    void dialerDisconnectCall() {
        if (Config.isDisconnect) {
//            disconnectSession();
            moveToHome();
        } else {
            getEndCall();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call_communication);
        Config.mActivity = this;
        ButterKnife.bind(this);

        mLlVideoSubscriberRootAdditional.setVisibility(View.GONE);


        if (getIntent().getExtras().getInt("CALL_TYPE") == 2) {
            dialHandler(true);

        } else {
            dialHandler(false);
        }


        requestPermissions();
    }

    void dialHandler(boolean shouldShow) {
        if (shouldShow) {
            dialerView.setVisibility(View.VISIBLE);
            videoCallLayout.setVisibility(View.GONE);
            try {
                if (!getIntent().getExtras().getString("CALER_NAME").isEmpty()) {
                    String docName = getIntent().getExtras().getString("CALER_NAME");

                    doctorName.setText(docName);
                    initialText.setText(docName.charAt(0));
                }
            } catch (Exception e) {
            }

            countDownTimer = new CountDownTimer(2 * 60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText("" + String.format("%d : %d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    if (mSession == null) {
                        dialerDisconnectCall();
                    }

                }
            };
            countDownTimer.start();
        } else {
            if (countDownTimer != null)
                countDownTimer.cancel();
            dialerView.setVisibility(View.GONE);
            videoCallLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");

        super.onResume();

        if (mSession == null) {
            return;
        }
        mSession.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");

        super.onPause();

        if (mSession == null) {
            return;
        }
        mSession.onPause();

        if (isFinishing()) {
            disconnectSession();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");

        disconnectSession();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.settings))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(this, perms)) {

            Log.e(TAG, OpenTokConfig.API_KEY+"\n\n"+OpenTokConfig.SESSION_ID+"\n\n"+OpenTokConfig.TOKEN);

            mSession = new Session.Builder(this, OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID).sessionOptions(new Session.SessionOptions() {
                @Override
                public boolean useTextureViews() {
                    return true;
                }
            }).build();
            mSession.setSessionListener(this);
            mSession.connect(OpenTokConfig.TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onConnected(Session session) {

        Log.e(TAG, "onConnected: Connected to session " + session.getSessionId());
        // initialize Publisher and set this object to listen to Publisher events

        if (getIntent().getExtras().getInt("CALL_TYPE") == 1) {
            setPublisherView();
        }
    }

    public void setPublisherView() {
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }


        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.e(TAG, "onDisconnected: disconnected from session " + session.getSessionId());

//        mSession = null;
        moveToHome();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(TAG, "onError: Error (" + opentokError.getMessage() + ") in session " + session.getSessionId());

        Toast.makeText(this, "Session error. See the logcat please." + opentokError.getMessage(), Toast.LENGTH_LONG).show();

        moveToHome();
    }


    private int getResIdForSubscriberIndex(int index) {
        TypedArray arr = getResources().obtainTypedArray(R.array.subscriber_view_ids);
        int subId = arr.getResourceId(index, 0);
        arr.recycle();
        return subId;
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Config.isDisconnect = true;
        Log.e(TAG, "onStreamReceived: New stream " + stream.getStreamId() + " in session " + session.getSessionId());


        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
            mSubscriber.setVideoListener(this);

            if (getIntent().getExtras().getInt("CALL_TYPE") == 2) {
                setPublisherView();
            }

            dialHandler(false);
            return;
        }

        if (mSubscriberAdditional == null) {
            mSubscriberAdditional = new Subscriber.Builder(this, stream).build();
            mSubscriberAdditional.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriberAdditional.setSubscriberListener(this);
            mSession.subscribe(mSubscriberAdditional);
            mSubscriberViewContainerAdditional.addView(mSubscriberAdditional.getView());
            mSubscriberAdditional.setVideoListener(this);
            mLlVideoSubscriberRootAdditional.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.e(TAG, "onStreamDropped: Stream " + stream.getStreamId() + " dropped from session " + session.getSessionId());
        Config.isDisconnect = false;



        if (mSubscriberAdditional != null && mSubscriberAdditional.getStream() != null && mSubscriberAdditional.getStream().getStreamId().equalsIgnoreCase(stream.getStreamId())) {
            mSubscriberAdditional = null;
            mLlVideoSubscriberRootAdditional.setVisibility(View.GONE);
        }

        if (mSubscriber != null && mSubscriber.getStream() != null && mSubscriber.getStream().getStreamId().equalsIgnoreCase(stream.getStreamId())) {
            mSubscriber = null;
            mLlVideoSubscriberRoot.setVisibility(View.GONE);
        }

        if (mSubscriber == null && mSubscriberAdditional == null) {
            moveToHome();
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.e(TAG, "onStreamCreated: Own stream " + stream.getStreamId() + " created");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.e(TAG, "onStreamDestroyed: Own stream " + stream.getStreamId() + " destroyed");
        Config.isDisconnect = false;
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(TAG, "onError: Error (" + opentokError.getMessage() + ") in publisher");
        Config.isDisconnect = false;
        //Toast.makeText(this, "Session error. See the logcat please.", Toast.LENGTH_LONG).show();
        moveToHome();
    }

    public void moveToHome() {
        if (GlobValues.getAddAppointmentParams() != null)
            GlobValues.getAddAppointmentParams().clear();

        if (isMovingToHome == true) {
            return;
        }

        isMovingToHome = true;

        Utilities.showAlertDialogWithOptions(this, false, "Thank You for using our service. You can check details in Appointment Section. Stay Healthy!!", new String[]{"Done"}, new UtilitiesInterfaces.AlertCallback() {
            @Override
            public void onOptionClick(DialogInterface dialog, int buttonIndex) {
                Intent homeScreen = new Intent(getApplicationContext(), Dashboard.class);
                homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeScreen);
            }
        });


    }



    private void disconnectSession() {

        try {

            if (mSubscriber != null) {
                mSession.unsubscribe(mSubscriber);
                mSubscriber.destroy();
            }

            if (mSubscriberAdditional != null) {
                mSession.unsubscribe(mSubscriberAdditional);
                mSubscriberAdditional.destroy();
            }

            if (mPublisher != null) {
                mSession.unpublish(mPublisher);
                mPublisher.destroy();
                mPublisher = null;
            }

            if (mSession != null) {
                mSession.disconnect();

            }

        } catch (Exception e) {

        }


    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriberKit) {

    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriberKit, String s) {
        Log.e(TAG, "&&&&&&&&&&  onVideoDisabled  &&&&&&&&&");
        if (subscriberKit != null && mSubscriber != null && subscriberKit.getStream() != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriber.getStream().getStreamId())) {
            videoDisabledView.setVisibility(View.VISIBLE);
            mSubscriberViewContainer.setVisibility(View.GONE);
            mLlVideoSubscriberRoot.setVisibility(View.VISIBLE);
        }

        if (subscriberKit != null && mSubscriberAdditional != null && subscriberKit.getStream() != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriberAdditional.getStream().getStreamId())) {
            mLlVideoSubscriberRootAdditional.setVisibility(View.VISIBLE);
            mSubscriberViewContainerAdditional.setVisibility(View.GONE);
            videoDisabledViewAdditional.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriberKit, String s) {
        Log.e(TAG, "&&&&&&&&&&  onVideoEnabled  &&&&&&&&&");

        if (subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriber.getStream().getStreamId())) {
            videoDisabledView.setVisibility(View.GONE);
            mSubscriberViewContainer.setVisibility(View.VISIBLE);
            mLlVideoSubscriberRoot.setVisibility(View.VISIBLE);
        }
        if (subscriberKit != null && mSubscriberAdditional != null && subscriberKit.getStream() != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriberAdditional.getStream().getStreamId())) {
            mLlVideoSubscriberRootAdditional.setVisibility(View.VISIBLE);
            mSubscriberViewContainerAdditional.setVisibility(View.VISIBLE);
            videoDisabledViewAdditional.setVisibility(View.GONE);

        }

    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriberKit) {
        Log.e(TAG, "&&&&&&&&&&  onVideoDisableWarning  &&&&&&&&&");

    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriberKit) {
        Log.e(TAG, "&&&&&&&&&&  onVideoDisableWarningLifted  &&&&&&&&&");

    }


    public void getEndCall() {

        String docId = "";
        Bundle mBundle = getIntent().getExtras();
        if (mBundle.containsKey("DocterId")) {
            docId = mBundle.getString("DocterId");
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
        SoapAPIManager apiManager = new SoapAPIManager(VideoConference.this, endCallParams, new APICallback() {
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

        if (Utilities.isNetworkAvailable(VideoConference.this)) {
            apiManager.execute(url);
        } else {

        }
    }

}
