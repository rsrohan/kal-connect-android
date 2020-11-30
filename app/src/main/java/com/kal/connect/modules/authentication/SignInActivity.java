package com.kal.connect.modules.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import com.google.android.material.textfield.TextInputLayout;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.cardio.patient.R;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.Dashboard;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import lib.kingja.switchbutton.SwitchMultiButton;

public class SignInActivity extends CustomActivity implements View.OnClickListener {
    LovelyTextInputDialog l;
    EditText mobile, password;
    Button getOTP;
    // TextInputLayout passoedTxtLayout;
    int authedicateType = 1; // 0->Login with password, 1->Get Otp, 2-> ProcessOtp

    SwitchMultiButton loginTypeSwich;

    Button signIN;

    @BindView(R.id.country_code)
    CountryCodePicker countryCode;


    @OnClick(R.id.language)
    public void languageShow() {

        Utilities.openLanguageSelection(SignInActivity.this);
    }

    @OnTextChanged(R.id.mobile)
    public void mobileNumberChanged() {
        password.setVisibility(View.GONE);
        signIN.setVisibility(View.GONE);
        getOTP.setAlpha(1f);
        getOTP.setText(R.string.get_otp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        buildUI();


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
//        Utilities.setAppLocale(this);

    }

    public void buildUI() {

        ButterKnife.bind(this);

        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        getOTP = (Button) findViewById(R.id.get_otp_btn);

//        password.setText("02241591");
        // passoedTxtLayout = (TextInputLayout) findViewById(R.id.passoedTxtLayout);
        signIN = (Button) findViewById(R.id.sign_in_btn);

//        mobile.setText("12345");
        signIN.setOnClickListener(this);
        getOTP.setOnClickListener(this);

        signIN.setVisibility(View.GONE);
        password.setVisibility(View.GONE);

        l = new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorAccent)
                .setTitle(R.string.signin_title)
                .setMessage(R.string.signin_option_msg)
                .setIcon(R.drawable.ok_icon_white)
                .setInputFilter(R.string.error_input, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^[7-9][0-9]{9}$");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {

                        mobile.setText(text);
                        authedicateType = 1;
                        signIN();
                    }
                }).setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginTypeSwich.setSelectedTab(0);
                    }
                }).setCancelable(false);


        loginTypeSwich = (SwitchMultiButton) findViewById(R.id.signin_options);
        loginTypeSwich.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {


                password.setHint("");
                if (position == 1) {
                    password.setHint("OTP");
                    l.setInitialInput(mobile.getText().toString());
                    l.setInputType(InputType.TYPE_CLASS_PHONE);
                    l.show();
                    authedicateType = 2;
                } else {
                    password.setHint("Password");
                    authedicateType = 0;
                }

            }
        });

    }

    public void signIN() {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        String webMethodName = Config.LOGIN_VIA_PASSWORD;

        if (authedicateType == 2)
            webMethodName = Config.VERIFYOTP;

        inputParams.put("username", mobile.getText().toString());
        inputParams.put("Password", authedicateType == 0 ? password.getText().toString() : "");

        if (AppPreferences.getInstance().getDeviceToken() != null && !AppPreferences.getInstance().getDeviceToken().equalsIgnoreCase("")) {
            inputParams.put("DeviceId", AppPreferences.getInstance().getDeviceToken());
        } else {
            Toast.makeText(getApplicationContext(), "DeviceId Not Register", Toast.LENGTH_LONG).show();
        }
        inputParams.put("iDeviceType", "1");

        inputParams.put("countryCode", countryCode.getSelectedCountryCodeWithPlus());

        inputParams.put("isOTP", authedicateType == 1 ? "1" : "0");
        inputParams.put("OTP", authedicateType == 2 ? password.getText().toString() : "");


        SoapAPIManager apiManager = new SoapAPIManager(SignInActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject userInfo = responseAry.getJSONObject(0);
                        if (userInfo.has("APIStatus") && Integer.parseInt(userInfo.getString("APIStatus")) == -1) {
                            if (userInfo.has("Message") && !userInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(SignInActivity.this, userInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(SignInActivity.this, "Please check again!", false);
                            }
                            return;

                        }
                        if (authedicateType == 1) {
                            signIN.setVisibility(View.VISIBLE);
                            password.setVisibility(View.VISIBLE);
                            getOTP.setClickable(false);
                            getOTP.setAlpha(.4f);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getOTP.setText(R.string.resend_otp);
                                    getOTP.setClickable(true);
                                    getOTP.setAlpha(1f);
                                }
                            }, 5000);
                        } else {
                            AppPreferences.getInstance().setLoginInfo(userInfo.toString());
                            startActivity(new Intent(SignInActivity.this, Dashboard.class));
                            finish();
                        }
//                        finish();

                    }
                } catch (Exception e) {

                }


            }
        }, true);
        String[] url = {Config.WEB_Services1, webMethodName, "POST"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        } else {

        }
    }

    public boolean validate() {
        if (countryCode.getSelectedCountryCodeAsInt() == 91) {
            return Utilities.validate(SignInActivity.this, mobile, getResources().getString(R.string.sign_in_mobile), false, 10, 10);
        }
        return Utilities.validate(SignInActivity.this, mobile, getResources().getString(R.string.sign_in_mobile), false, 3, 20);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_btn) {
            if (validate()) {
//                authedicateType = loginTypeSwich.getSelectedTab()==0?0:2;
                authedicateType = 2;
                signIN();
            }
//            startActivity(new Intent(SignIn.this,Dashboard.class));
        }
        if (v.getId() == R.id.get_otp_btn) {

            if (validate()) {
                authedicateType = 1;
                signIN();
            }

//            signIN.setVisibility(View.VISIBLE);
//            password.setVisibility(View.VISIBLE);
//            getOTP.setClickable(false);
//            getOTP.setAlpha(.4f);
//            mobile.setEnabled(false);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getOTP.setText(R.string.resend_otp);
//                    getOTP.setClickable(true);
//                    getOTP.setAlpha(1f);
//                    mobile.setEnabled(true);
//                }},5000);


        }

    }


}
