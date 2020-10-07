package com.kal.connect.modules.communicate;

import android.Manifest;
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


import com.kal.connect.R;
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

    private static final String TAG = VideoConference.class.getSimpleName();
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
        disconnectSession();
        moveToHome();
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
        disconnectSession();
        moveToHome();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call_communication);

        ButterKnife.bind(this);

//        mSubscriberViewContainerAdditional.setVisibility(View.GONE);
        mLlVideoSubscriberRootAdditional.setVisibility(View.GONE);

//        sessionId = getIntent().getStringExtra("SESSION_ID");
//        token = getIntent().getStringExtra("TOKEN");

//        mContainer = (ConstraintLayout) findViewById(R.id.main_container);
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
        Log.d(TAG, "onResume");

        super.onResume();

        if (mSession == null) {
            return;
        }
        mSession.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

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
        Log.d(TAG, "onDestroy");

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
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

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

    private void startPublisherPreview() {
        mPublisher = new Publisher.Builder(this).name("publisher").build();

        mPublisher.setPublisherListener(this);
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisher.startPreview();
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
//            OpenTokConfig.SESSION_ID = "1_MX40NTQ2NzMwMn5-MTU5OTYyNzQ0NTg1Nn5BMkR1clpRYUtjNEhvVU5hSkdmTnk2QjF-fg";
//            OpenTokConfig.TOKEN = "T1==cGFydG5lcl9pZD00NTQ2NzMwMiZzaWc9NmMzYWNjNzU2N2I1NGU1YzNlYWFkZjQ5ZjFlMzBkOTFkNGY4ODg4ZDpzZXNzaW9uX2lkPTFfTVg0ME5UUTJOek13TW41LU1UVTVPVFl5TnpRME5UZzFObjVCTWtSMWNscFJZVXRqTkVodlZVNWhTa2RtVG5rMlFqRi1mZyZjcmVhdGVfdGltZT0xNTk5NjI3NDcyJm5vbmNlPTAuOTQ0NzI5Mzk0NzIwMzk0NyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjAwMjMyMjcwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";

            mSession = new Session.Builder(this, OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID).sessionOptions(new Session.SessionOptions() {
                @Override
                public boolean useTextureViews() {
                    return true;
                }
            }).build();
            mSession.setSessionListener(this);
            mSession.connect(OpenTokConfig.TOKEN);

//            startPublisherPreview();
//            mPublisher.getView().setId(R.id.publisher_view_id);
//            mContainer.addView(mPublisher.getView());
//            calculateLayout();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onConnected(Session session) {

        Log.d(TAG, "onConnected: Connected to session " + session.getSessionId());
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
        Log.d(TAG, "onDisconnected: disconnected from session " + session.getSessionId());

//        mSession = null;
        moveToHome();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.d(TAG, "onError: Error (" + opentokError.getMessage() + ") in session " + session.getSessionId());

//        Toast.makeText(this, "Session error. See the logcat please." + opentokError.getMessage(), Toast.LENGTH_LONG).show();

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
        Log.d(TAG, "onStreamReceived: New stream " + stream.getStreamId() + " in session " + session.getSessionId());

//        final Subscriber subscriber = new Subscriber.Builder(VideoConference.this, stream).build();
//        mSession.subscribe(subscriber);
//        mSubscribers.add(subscriber);
//        mSubscriberStreams.put(stream, subscriber);
//
//        int subId = getResIdForSubscriberIndex(mSubscribers.size() - 1);
//        subscriber.getView().setId(subId);
//        mContainer.addView(subscriber.getView());


//        calculateLayout();

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

//            setPublisherView();
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
//            mSubscriberViewContainerAdditional.setVisibility(View.VISIBLE);
            mLlVideoSubscriberRootAdditional.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.d(TAG, "onStreamDropped: Stream " + stream.getStreamId() + " dropped from session " + session.getSessionId());

//        moveToHome();

//        Subscriber subscriber = mSubscriberStreams.get(stream);
//        if (subscriber == null) {
//            return;
//        }


        if (mSubscriberAdditional != null && mSubscriberAdditional.getStream() != null && mSubscriberAdditional.getStream().getStreamId().equalsIgnoreCase(stream.getStreamId())) {
            mSubscriberAdditional = null;
//            mSubscriberViewContainerAdditional.setVisibility(View.GONE);
            mLlVideoSubscriberRootAdditional.setVisibility(View.GONE);
        }

        if (mSubscriber != null && mSubscriber.getStream() != null && mSubscriber.getStream().getStreamId().equalsIgnoreCase(stream.getStreamId())) {
            mSubscriber = null;
//            mSubscriberViewContainer.setVisibility(View.GONE);
            mLlVideoSubscriberRoot.setVisibility(View.GONE);
        }

        if (mSubscriber == null && mSubscriberAdditional == null) {
            moveToHome();
        }

//
//        mSubscribers.remove(subscriber);
//        mSubscriberStreams.remove(stream);
//        mContainer.removeView(subscriber.getView());
//
//        if(mSubscribers.size() < 1){
//            mSession.disconnect();
//            moveToHome();
//        }
//
//        // Recalculate view Ids
//        for (int i = 0; i < mSubscribers.size(); i++) {
//            mSubscribers.get(i).getView().setId(getResIdForSubscriberIndex(i));
//        }
//        calculateLayout();
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.d(TAG, "onStreamCreated: Own stream " + stream.getStreamId() + " created");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.d(TAG, "onStreamDestroyed: Own stream " + stream.getStreamId() + " destroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.d(TAG, "onError: Error (" + opentokError.getMessage() + ") in publisher");

//        Toast.makeText(this, "Session error. See the logcat please.", Toast.LENGTH_LONG).show();
        moveToHome();
    }

    void moveToHome() {
        if (GlobValues.getAddAppointmentParams() != null)
            GlobValues.getAddAppointmentParams().clear();

        if (isMovingToHome == true) {
            return;
        }

        isMovingToHome = true;

        Utilities.showAlertDialogWithOptions(this, false, "Thanks for using our service, You can check this call details from your Appointment details, Take care!", new String[]{"Done"}, new UtilitiesInterfaces.AlertCallback() {
            @Override
            public void onOptionClick(DialogInterface dialog, int buttonIndex) {
                Intent homeScreen = new Intent(getApplicationContext(), Dashboard.class);
                homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeScreen);
//                finish();
            }
        });


    }

    private void calculateLayout() {
        ConstraintSetHelper set = new ConstraintSetHelper(R.id.main_container);

        int size = mSubscribers.size();
        if (size == 0) {
            // Publisher full screen
            set.layoutViewFullScreen(R.id.publisher_view_id);
        } else if (size == 1) {
            // Publisher
            // Subscriber
            set.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(0));
            set.layoutViewWithTopBound(R.id.publisher_view_id, R.id.main_container);
            set.layoutViewWithBottomBound(getResIdForSubscriberIndex(0), R.id.main_container);
            set.layoutViewAllContainerWide(R.id.publisher_view_id, R.id.main_container);
            set.layoutViewAllContainerWide(getResIdForSubscriberIndex(0), R.id.main_container);

        } else if (size > 1 && size % 2 == 0) {
            //  Publisher
            // Sub1 | Sub2
            // Sub3 | Sub4
            //    .....
            set.layoutViewWithTopBound(R.id.publisher_view_id, R.id.main_container);
            set.layoutViewAllContainerWide(R.id.publisher_view_id, R.id.main_container);

            for (int i = 0; i < size; i += 2) {
                if (i == 0) {
                    set.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i));
                    set.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i + 1));
                } else {
                    set.layoutViewAboveView(getResIdForSubscriberIndex(i - 2), getResIdForSubscriberIndex(i));
                    set.layoutViewAboveView(getResIdForSubscriberIndex(i - 1), getResIdForSubscriberIndex(i + 1));
                }

                set.layoutTwoViewsOccupyingAllRow(getResIdForSubscriberIndex(i), getResIdForSubscriberIndex(i + 1));
            }

            set.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 2), R.id.main_container);
            set.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 1), R.id.main_container);
        } else if (size > 1) {
            // Pub  | Sub1
            // Sub2 | Sub3
            // Sub3 | Sub4
            //    .....

            set.layoutViewWithTopBound(R.id.publisher_view_id, R.id.main_container);
            set.layoutViewWithTopBound(getResIdForSubscriberIndex(0), R.id.main_container);
            set.layoutTwoViewsOccupyingAllRow(R.id.publisher_view_id, getResIdForSubscriberIndex(0));

            for (int i = 1; i < size; i += 2) {
                if (i == 1) {
                    set.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i));
                    set.layoutViewAboveView(getResIdForSubscriberIndex(0), getResIdForSubscriberIndex(i + 1));
                } else {
                    set.layoutViewAboveView(getResIdForSubscriberIndex(i - 2), getResIdForSubscriberIndex(i));
                    set.layoutViewAboveView(getResIdForSubscriberIndex(i - 1), getResIdForSubscriberIndex(i + 1));
                }
                set.layoutTwoViewsOccupyingAllRow(getResIdForSubscriberIndex(i), getResIdForSubscriberIndex(i + 1));
            }

            set.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 2), R.id.main_container);
            set.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 1), R.id.main_container);
        }

        set.applyToLayout(mContainer, true);
    }

    private void disconnectSession() {

        try {
            //        if (mSubscribers.size() > 0) {
//            for (Subscriber subscriber : mSubscribers) {
//                if (subscriber != null) {
//                    mSession.unsubscribe(subscriber);
//                    subscriber.destroy();
//                }
//            }
//        }

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
//            mContainer.removeView(mPublisher.getView());
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
//            mSubscriberViewContainer.setVisibility(View.GONE);
            mLlVideoSubscriberRoot.setVisibility(View.VISIBLE);
        }

        if (subscriberKit != null && mSubscriberAdditional != null && subscriberKit.getStream() != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriberAdditional.getStream().getStreamId())) {
//            videoDisabledView.setVisibility(View.VISIBLE);
//            mSubscriberViewContainer.setVisibility(View.GONE);
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
//            mSubscriberViewContainer.setVisibility(View.GONE);
            mLlVideoSubscriberRoot.setVisibility(View.VISIBLE);
        }
        if (subscriberKit != null && mSubscriberAdditional != null && subscriberKit.getStream() != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriberAdditional.getStream().getStreamId())) {
//            videoDisabledView.setVisibility(View.VISIBLE);
//            mSubscriberViewContainer.setVisibility(View.GONE);
            mLlVideoSubscriberRootAdditional.setVisibility(View.VISIBLE);
            mSubscriberViewContainerAdditional.setVisibility(View.VISIBLE);
            videoDisabledViewAdditional.setVisibility(View.GONE);

        }


//        if (subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriber.getStream().getStreamId())) {
//            videoDisabledView.setVisibility(View.VISIBLE);
//            mLlVideoSubscriberRoot.setVisibility(View.GONE);
//            mSubscriberViewContainer.setVisibility(View.GONE);
//        }
//        if (subscriberKit != null && mSubscriberAdditional != null && subscriberKit.getStream().getStreamId().equalsIgnoreCase(mSubscriberAdditional.getStream().getStreamId())) {
//            videoDisabledView.setVisibility(View.VISIBLE);
//            mSubscriberViewContainer.setVisibility(View.GONE);
//        }

//        videoDisabledView.setVisibility(View.GONE);
//        mSubscriberViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriberKit) {
        Log.e(TAG, "&&&&&&&&&&  onVideoDisableWarning  &&&&&&&&&");

    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriberKit) {
        Log.e(TAG, "&&&&&&&&&&  onVideoDisableWarningLifted  &&&&&&&&&");

    }



}
