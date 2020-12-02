package com.kal.connect.modules.communicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kal.connect.R;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.Config;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;

public class ChatActivity extends CustomActivity implements Session.SessionListener,
        Session.SignalListener, WebServiceCoordinator.Listener {

    private static final String LOG_TAG = ChatActivity.class.getSimpleName();
    public static final String SIGNAL_TYPE = "text-signal";
    EditText mEdtMessage;
    private Session mSession;
    private WebServiceCoordinator mWebServiceCoordinator;
    private SignalMessageAdapter mMessageHistory;
    private ListView mMessageHistoryListView;
    Button mBtnSend;
    private FirebaseCrashlytics crashlytics;
    private ProgressBar progressBar;
    //        public static String SESSION_ID = "1_MX40NTQ2NzMwMn5-MTYwMTU1MDExMTQ5OH5vbVZXUmtzaG82UmtxM0syYmlmWVREdUN-fg";
//    public static String TOKEN = "T1==cGFydG5lcl9pZD00NTQ2NzMwMiZzaWc9N2FlMDc5YjY3YjI1ZGUxZTgyZTBkZmU2ODc0YzI1N2I3ZjVlMjczODpzZXNzaW9uX2lkPTFfTVg0ME5UUTJOek13TW41LU1UWXdNVFUxTURFeE1UUTVPSDV2YlZaWFVtdHphRzgyVW10eE0wc3lZbWxtV1ZSRWRVTi1mZyZjcmVhdGVfdGltZT0xNjAxNTUwMTI4Jm5vbmNlPTAuMzM0NzY5Njg1OTg2MjAzOTQmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYwNDE0MjEyNSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    public static final String CHAT_SERVER_URL = null;
    public static final String SESSION_INFO_ENDPOINT = CHAT_SERVER_URL + "/session";
    String mStrDocName = "",Option="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle mBundle = getIntent().getExtras();

        if (mBundle.containsKey("doctorName") && mBundle.getString("doctorName") != null) {
            mStrDocName = mBundle.getString("doctorName");
        }

        setHeaderView(R.id.headerView, ChatActivity.this, mStrDocName);
        headerView.showBackOption();



        mMessageHistoryListView = (ListView) findViewById(R.id.message_history_list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mEdtMessage = (EditText) findViewById(R.id.inputMsg);
        mBtnSend = (Button) findViewById(R.id.btnSend);
        mMessageHistory = new SignalMessageAdapter(this);
        mMessageHistoryListView.setAdapter(mMessageHistory);

        mMessageHistoryListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mEdtMessage.setEnabled(false);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtMessage.getText().toString().trim().length()>0) {
                    sendMessage();
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter message",Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (CHAT_SERVER_URL == null) {
            initializeSession(OpenTokConfigConstants.API_KEY, OpenTokConfigConstants.SESSION_ID, OpenTokConfigConstants.TOKEN);
//            initializeSession(OpenTokConfig.API_KEY, SESSION_ID, TOKEN);
        } else {
//            mWebServiceCoordinator = new WebServiceCoordinator(this, this);
//            mWebServiceCoordinator.fetchSessionConnectionData(SESSION_INFO_ENDPOINT);
        }

        Config.isChat = true;
    }

    private void sendMessage() {

        SignalMessage signal = new SignalMessage(mEdtMessage.getText().toString());
        mSession.sendSignal(SIGNAL_TYPE, signal.getMessageText());

        mEdtMessage.setText("");


    }


    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mMessageHistoryListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mEdtMessage.setEnabled(true);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }

    @Override
    public void onSignalReceived(Session session, String type, String data, Connection connection) {
        boolean remote = !connection.equals(mSession.getConnection());
        if (type != null && type.equals(SIGNAL_TYPE)) {
            showMessage(data, remote, "Rajvel");
        }
    }


    private void logOpenTokError(OpentokError opentokError) {

        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSession != null) {
            mSession.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
        mSession.setSignalListener(this);
    }

    private void showMessage(String messageData, boolean remote, String name) {

        SignalMessage message = new SignalMessage(messageData, remote, name);
        mMessageHistory.add(message);
    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        showConfigError("Web Service error", error.getMessage());
    }

    private void showConfigError(String alertTitle, final String errorMessage) {

        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ChatActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Config.isChat = false;
        if (Config.isBack) {
            Intent mIntent = new Intent(getApplicationContext(), DashboardMapActivity.class);
            startActivity(mIntent);
            finish();
        } else {
            finish();
        }
    }
}
