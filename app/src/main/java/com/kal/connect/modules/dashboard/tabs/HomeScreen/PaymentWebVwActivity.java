package com.kal.connect.modules.dashboard.tabs.HomeScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.appconstants.WebService;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.DoctorModel;
import com.kal.connect.modules.communicate.VideoCaller;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentWebVwActivity extends CustomActivity implements View.OnClickListener{

    // MARK : Properties
    WebView paymentWebVw;
    final HashMap<String, Object> appointmentinputParams  = GlobValues.getInstance().getAddAppointmentParams();

    // MARK : Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_webvw);
        buildUI();

    }

    // MARK : Instance Methods
    private void buildUI() {

        setHeaderView(R.id.headerView, PaymentWebVwActivity.this, PaymentWebVwActivity.this.getResources().getString(R.string.appointment_detail_title));
        headerView.showBackOption();

        //Header for details

        paymentWebVw = (WebView) findViewById(R.id.paymentWebVw);
        paymentWebVw.getSettings().setJavaScriptEnabled(true);
        paymentWebVw.getSettings().setLoadWithOverviewMode(true);
        paymentWebVw.getSettings().setUseWideViewPort(true);
        paymentWebVw.addJavascriptInterface(new WebAppInterface(this), "Android");
//        WebView.setWebContentsDebuggingEnabled(true);

        paymentWebVw.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PaymentWebVwActivity.this);
                builder.setMessage("It's may not a valid SSL certificate url , Please copnfirm!");
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        GlobValues g = GlobValues.getInstance();
        DoctorModel d = g.getDoctor();
        HashMap<String, Object> ap = g.getAddAppointmentParams();
        int totalAmount = Integer.parseInt(d.getDocCharge());
        if(Integer.parseInt(ap.get("isTechnician").toString()) == 1){
            totalAmount = totalAmount + Integer.parseInt(d.getDocCharge());

        }




        String url = WebService.BASE_URL + "RazorPayAPI/RazorPaymentNew.aspx?paymentAmount="+totalAmount+"&PatName="+ap.get("PatientName").toString()+"&PatEmail="+ap.get("PatEmail").toString()+"&PatPhone="+ap.get("PatPhone").toString();
//        paymentWebVw.loadUrl("https://ec2-35-154-223-253.ap-south-1.compute.amazonaws.com/RazorPayAPI/RazorPaymentNew.aspx?paymentAmount=99.99&PatName=gajanan bhat&PatEmail=gajananpb@gmail.com&PatPhone=984501223");
//        paymentWebVw.loadUrl("https://www.google.com/");
        paymentWebVw.loadUrl(url);
    }

    @Override
    public void onClick(View v) {

    }

    public void getVideoCallConfigurations(){
//        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();


//        inputParams.put("ComplaintID",GlobValues.getInstance().getSelectedAppointment());
//        GlobValues g = GlobValues.getInstance();
//
//        final HashMap<String, Object> appointmentinputParams  = g.getAddAppointmentParams();

        SoapAPIManager apiManager = new SoapAPIManager(PaymentWebVwActivity.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(PaymentWebVwActivity.this,commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(PaymentWebVwActivity.this,"Please check again!",false);
                            }
                            return;

                        }
//                        loadAppointments(responseAry);
                        if(commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()){
                            String TOKEN = commonDataInfo.getString("VCToekn");
                            String SESSION = commonDataInfo.getString("VSSessionID");

                            Intent intent = new Intent(PaymentWebVwActivity.this, VideoCaller.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                            intent.putExtra("SESSION_ID",SESSION);
                            intent.putExtra("TOKEN",TOKEN);

                            intent.putExtra("CALER_NAME",appointmentinputParams.get("SpecialistName").toString());
                            intent.putExtra("CALL_TYPE",2);
                            startActivity(intent);

                            Utilities.pushAnimation(PaymentWebVwActivity.this);

                        }

                    }
                }catch (Exception e){
                    Utilities.showAlert(PaymentWebVwActivity.this,"Please contact support!",false);

                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.INITIATE_VIDEO_CALL,"POST"};

        if (Utilities.isNetworkAvailable(PaymentWebVwActivity.this)) {
            apiManager.execute(url);
        }else{

        }
    }

    void bookAppointment(){
//        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

//        final HashMap<String, Object> appointmentinputParams  = GlobValues.getInstance().getAddAppointmentParams();
        SoapAPIManager apiManager = new SoapAPIManager(PaymentWebVwActivity.this, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("Message") && commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(PaymentWebVwActivity.this,commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(PaymentWebVwActivity.this,"Please check again!",false);
                            }
                            return;

                        }
                        startActivity(new Intent(PaymentWebVwActivity.this, DashboardMapActivity.class));

                    }
                }catch (Exception e){
                    Utilities.showAlert(PaymentWebVwActivity.this,"Please contact support!",false);

                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.BOOK_APPOINTMENT,"POST"};

        if (Utilities.isNetworkAvailable(PaymentWebVwActivity.this)) {
            apiManager.execute(url);
        }else{

        }
    }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Response messages from the web page */
        @JavascriptInterface
        public void paymentFailure(String msg) {

            finish();
        }
        @JavascriptInterface
        public void paymentSuccess(String msg) {

            //Should call here video call or appointment success

            if(Integer.parseInt(appointmentinputParams.get("ConsultNow").toString())==1)
                getVideoCallConfigurations();
            else
                bookAppointment();
        }


    }
}