package com.kal.connect.modules.communicate;

//import androidx.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;

import com.opentok.android.SubscriberKit;

        import com.kal.connect.customLibs.appCustomization.CustomActivity;

        import com.opentok.android.Session;
import com.opentok.android.Stream;
        import com.opentok.android.PublisherKit;
        import com.opentok.android.OpentokError;

//import androidx.annotation.NonNull;
        import android.view.View;

        import androidx.annotation.NonNull;

        import pub.devrel.easypermissions.EasyPermissions;

        import java.util.List;

public class VideoCaller extends CustomActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener, WebServiceCoordinator.Listener,Session.SessionListener{



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

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
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

/*
    TextView callerNameTxtVw,countDownTxtVw;
    ImageButton connectBtn,disconnectBtn,activeCallDisconnectBtn,outGoingCalldisconnectBtn,switchCameraBtn;
    CountDownTimer countDownTimer;
    RelativeLayout callerDesignLayout;
    FrameLayout communicationLayout;



    public static final String CHAT_SERVER_URL = null;
    public static final String SESSION_INFO_ENDPOINT = CHAT_SERVER_URL + "/session";

//    private static String API_KEY = "";
//    private static String SESSION_ID = "";
//    private static String TOKEN = "";

    int callType;

    String API_KEY = "45467302";
//    // Replace with a generated Session ID
//    public static final String SESSION_ID = "2_MX40NjEzODc2Mn5-MTUyOTAzMDkyNTk4OX5TSWNHQXdma3J5NU5BSU1KQ25MblJVVE9-fg";
//    // Replace with a generated token (from the dashboard or using an OpenTok server SDK)
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NjEzODc2MiZzaWc9YzA0YzA3ZjU1MDlkNWMzN2FiNDQ3ZWFkODhiYzdiZjhiYmU2Nzg5NjpzZXNzaW9uX2lkPTJfTVg0ME5qRXpPRGMyTW41LU1UVXlPVEF6TURreU5UazRPWDVUU1dOSFFYZG1hM0o1TlU1QlNVMUtRMjVNYmxKVlZFOS1mZyZjcmVhdGVfdGltZT0xNTI5MDMwOTUyJm5vbmNlPTAuMDc2MDY3OTk0MTE0NTQ2NTcmcm9sZT1tb2RlcmF0b3ImZXhwaXJlX3RpbWU9MTUzMTYyMjk1MSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";


    private static final String LOG_TAG = VideoCaller.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;


    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private String sessionId,token,callerName;
    Ringtone currentRingtone;
    boolean videoCallConnectionClicked = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout


            // initialize and connect to the session

//            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
//            mSession.setSessionListener(this);
//            mSession.connect(TOKEN);
            initializeSession(API_KEY, sessionId, token);


//            mWebServiceCoordinator = new WebServiceCoordinator(this, this);
//            mWebServiceCoordinator.fetchSessionConnectionData(SESSION_INFO_ENDPOINT);


        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_call);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        buildUI();

    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }
    @Override
    public void onDestroy() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    public void buildUI(){
        callerNameTxtVw = (TextView)findViewById(R.id.callerNameTxtVw);
        countDownTxtVw = (TextView)findViewById(R.id.countdownTxtVw);
        connectBtn = (ImageButton)findViewById(R.id.connectBtn);
        disconnectBtn = (ImageButton)findViewById(R.id.disconnectBtn);
        switchCameraBtn = (ImageButton)findViewById(R.id.switchCameraBtn);

        callerDesignLayout = (RelativeLayout)findViewById(R.id.callerUI);
        communicationLayout = (FrameLayout)findViewById(R.id.communicationVw) ;
        activeCallDisconnectBtn = (ImageButton) findViewById(R.id.activeCallDisconnectBtn) ;
        outGoingCalldisconnectBtn = (ImageButton) findViewById(R.id.outGoingCalldisconnectBtn) ;

        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);


        activeCallDisconnectBtn.setOnClickListener(this);
        outGoingCalldisconnectBtn.setOnClickListener(this);
        connectBtn.setOnClickListener(this);
        disconnectBtn.setOnClickListener(this);
        switchCameraBtn.setOnClickListener(this);

        callType = getIntent().getIntExtra("CALL_TYPE",0);
        sessionId = getIntent().getStringExtra("SESSION_ID");
        token = getIntent().getStringExtra("TOKEN");
        callerName = getIntent().getStringExtra("CALER_NAME");

        callerNameTxtVw.setText(callerName);
        if(callType==1){
            callerDesignLayout.setVisibility(View.VISIBLE);
            communicationLayout.setVisibility(View.GONE);
            outGoingCalldisconnectBtn.setVisibility(View.GONE);
            Uri currentRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this
                    .getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
            currentRingtone = RingtoneManager.getRingtone(this, currentRintoneUri);
            currentRingtone.play();

        }
        if(callType==2){
            callerDesignLayout.setVisibility(View.VISIBLE);
            communicationLayout.setVisibility(View.GONE);
            outGoingCalldisconnectBtn.setVisibility(View.VISIBLE);
            connectBtn.setVisibility(View.GONE);
            disconnectBtn.setVisibility(View.GONE);

            requestPermissions();



        }
//
//        mSession.setReconnectionListener(new Session.ReconnectionListener() {
//            @Override
//            public void onReconnecting(Session session) {
//
//
//            }
//
//            @Override
//            public void onReconnected(Session session) {
//
//            }
//        });






        Log.v("Data",sessionId+ " "+ token+ "   "+ callerName);






        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
//                countDownTxtVw.setText("" + millisUntilFinished / 1000);
                countDownTxtVw.setText(String.format("00:%1$tS", millisUntilFinished,millisUntilFinished));
            }

            public void onFinish() {


                Log.v("","onFinish.......");
                if(callType==2){
                    if(!videoCallConnectionClicked){
                        mSession.disconnect();
                        callDashboard();
                    }


                }
                if(currentRingtone!=null && currentRingtone.isPlaying())
                    currentRingtone.stop();
//                mTextField.setText("done!");
                if(!videoCallConnectionClicked){
                    callDashboard();
                }




            }
        }.start();





    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.connectBtn){
            videoCallConnectionClicked = true;
            countDownTimer.onFinish();
            callerDesignLayout.setVisibility(View.GONE);
            communicationLayout.setVisibility(View.VISIBLE);
            requestPermissions();
        }
        if(view.getId()==R.id.disconnectBtn){

            countDownTimer.onFinish();


            finish();
        }
        if(view.getId()==R.id.activeCallDisconnectBtn){


            mSession.disconnect();
        }
        if(view.getId()==R.id.outGoingCalldisconnectBtn){
            mSession.disconnect();
        }

        if(view.getId()==R.id.switchCameraBtn){
            mPublisher.cycleCamera();
        }

    }

    public void callDashboard(){
        startActivity(new Intent(VideoCaller.this,Dashboard.class));
    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    // PublisherListener methods

    /* Session Listener methods

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: "+session.getSessionId());

        // initialize Publisher and set this object to listen to Publisher events
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

//        protected Publisher(android.content.Context context,
//                java.lang.String name,
//        boolean audioTrack,
//        int maxAudioBitrate,
//        boolean videoTrack,
//        BaseVideoCapturer capturer,
//        Publisher.CameraCaptureResolution resolution,
//        Publisher.CameraCaptureFrameRate frameRate,
//        BaseVideoRenderer renderer)
//        protected PublisherKit(Context context, String name, boolean audioTrack, int maxAudioBitRate, boolean videoTrack, BaseVideoCapturer capturer, BaseVideoRenderer renderer) {



//        new Publisher(this,"CallerUI",12,true,new PublisherKit().getCapturer(),

//        callerDesignLayout.setVisibility(View.GONE);
//        communicationLayout.setVisibility(View.VISIBLE);
//
//        outGoingCalldisconnectBtn.setVisibility(View.GONE);


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

        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: "+session.getSessionId());
        callDashboard();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received "+stream.getStreamId() + " in session: "+session.getSessionId());

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            //Hide callerUI

            videoCallConnectionClicked = true;
            callerDesignLayout.setVisibility(View.GONE);
            communicationLayout.setVisibility(View.VISIBLE);

            outGoingCalldisconnectBtn.setVisibility(View.GONE);

            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: "+stream.getStreamId() +" in session: "+session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
        mSession.disconnect();

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: "+ opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - "+opentokError.getMessage() + " in session: "+ session.getSessionId());

        showOpenTokError(opentokError);
    }

    //Publisher Listener methods

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream "+stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream "+stream.getStreamId());
//        if (mSession != null) {
//            mSession.onResume();
//        }
//        mSession.connect(token);

//        mSession.re
        callDashboard();
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "PublisherKit onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }



    private void showOpenTokError(OpentokError opentokError) {


        callDashboard();
    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VideoCaller.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "001 onConnected");

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "000 onSessionConnectionDataReady");

//        mSession.disconnect();
        callDashboard();
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e(LOG_TAG, " SubscriberKit onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initializeSession(API_KEY, sessionId, token);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(LOG_TAG, "onPermissionsDenied");
        callDashboard();

    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
//        initializeSession(apiKey, sessionId, token);
        Log.d(LOG_TAG, "onSessionConnectionDataReady");


    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        Log.d(LOG_TAG, "onWebServiceCoordinatorError");

    }

    */
}
