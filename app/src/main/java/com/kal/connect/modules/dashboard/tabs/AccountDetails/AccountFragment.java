package com.kal.connect.modules.dashboard.tabs.AccountDetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kal.connect.R;
import com.kal.connect.adapters.AccountPersonalDetailsAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountFragment extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();

    LinearLayout optionEditProfile, optionLogout, changePassword, updateLocation;
    TextView lblFullName, lblMobile;
    RecyclerView vwProfileList;
    TextView textViewVersionInfo;

    AccountPersonalDetailsAdapter dataAdapter = null;
    CustomDialogClass customDialog;


    @OnClick(R.id.language)
    public void changeLanguage(){
        Utilities.openLanguageSelection(getActivity());
    }

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.account, container, false);
        buildUI();

        ButterKnife.bind(this,view);
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {
        lblFullName = (TextView) view.findViewById(R.id.fullName);
        lblMobile = (TextView) view.findViewById(R.id.mobile);
        optionEditProfile = (LinearLayout) view.findViewById(R.id.optionEditProfile);
        optionLogout = (LinearLayout) view.findViewById(R.id.optionLogout);
        changePassword = (LinearLayout) view.findViewById(R.id.changePassword);
        updateLocation = (LinearLayout) view.findViewById(R.id.locationUpdate);
        vwProfileList = (RecyclerView) view.findViewById(R.id.profileInfoList);
        textViewVersionInfo = (TextView) view.findViewById(R.id.textview_version_info);

        customDialog = new CustomDialogClass((Activity)getContext());

        optionEditProfile.setOnClickListener(this);
        optionLogout.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        updateLocation.setOnClickListener(this);

        buildListView();
        loadAccountInfo();
//        getVersionInfo();

    }

    private void buildListView() {

        dataAdapter = new AccountPersonalDetailsAdapter(dataItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwProfileList.setNestedScrollingEnabled(false);
        vwProfileList.setLayoutManager(mLayoutManager);
        vwProfileList.setItemAnimator(new DefaultItemAnimator());
        vwProfileList.setAdapter(dataAdapter);

        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwProfileList, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));

        dataAdapter.setOnItemClickListener(new AccountPersonalDetailsAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {

            }

        });

    }

    void loadAccountInfo() {

        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();

            // Name
            String fullName = "";
            fullName = ((accInfo.getString("FirstName") != null) && accInfo.getString("FirstName").length() > 0) ? accInfo.getString("FirstName") : "";
            fullName = ((accInfo.getString("LastName") != null) && accInfo.getString("LastName").length() > 0) ? fullName + " " + accInfo.getString("LastName") : "";

            if (fullName.length() > 0)
                lblFullName.setText(fullName);

            // Mobile
            String mobile = "";
            mobile = ((accInfo.getString("ContactNo") != null) && accInfo.getString("ContactNo").length() > 0) ? accInfo.getString("ContactNo") : "";

            if (mobile.length() > 0)
                lblMobile.setText(mobile);


            dataItems.clear();

            // Email
            if (accInfo.getString("Email") != null && accInfo.getString("Email").length() > 0) {

                String email = accInfo.getString("Email");
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("key", getResources().getString(R.string.email));
                item.put("value", email);
                dataItems.add(item);

            }

            // DOB
            if (Utilities.dateRT(accInfo.getString("DOB")) != null && Utilities.dateRT(accInfo.getString("DOB")).length() > 0) {

                String dob = Utilities.dateRT(accInfo.getString("DOB"));
                dob = Utilities.changeStringFormat(dob,"yyyy-mm-dd","dd/mm/yyyy");
                HashMap<String, Object> item = new HashMap<String, Object>();
//                item.put("key", "Date of birth");
                item.put("key", getResources().getString(R.string.dob));
                item.put("value", dob);
                dataItems.add(item);

            }

            // Password
//            HashMap<String, Object> password = new HashMap<String, Object>();
////            password.put("key", "Password");
//            password.put("key", getResources().getString(R.string.sign_in_password));
//            password.put("value", "******");
            //dataItems.add(password);

            // Gender
            if (accInfo.getString("Sex").trim() != null && accInfo.getString("Sex").trim().length() > 0) {

                int gender = Integer.parseInt(accInfo.getString("Sex").trim());
                HashMap<String, Object> item = new HashMap<String, Object>();
//                item.put("key", "Gender");
                item.put("key", getResources().getString(R.string.gender));

                if(gender == 1){
                    item.put("value", getResources().getString(R.string.male));
                }
                else if(gender == 2){
                    item.put("value", getResources().getString(R.string.female));
                }
                else if(gender == 0){
                    item.put("value", getResources().getString(R.string.others));
                }
                dataItems.add(item);

            }

            // Address
            String addressValue = "";

//            // Address
//            if (accInfo.has("LocationAddress") && accInfo.getString("LocationAddress") != null && accInfo.getString("LocationAddress").length() > 0) {
//                HashMap<String, Object> item = new HashMap<String, Object>();
//                item.put("key", "Address");
//                item.put("key", getResources().getString(R.string.address));
//                item.put("value", accInfo.getString("LocationAddress"));
//                dataItems.add(item);
//            }
            // Address 1
            if (accInfo.getString("Addressline1") != null && accInfo.getString("Addressline1").length() > 0) {
                addressValue = accInfo.getString("Addressline1");
            }

            // Address 2
            if (accInfo.getString("Addressline2") != null && accInfo.getString("Addressline2").length() > 0) {
                addressValue = addressValue + ", " + accInfo.getString("Addressline2");
            }

            // City
            if (accInfo.has("cityname") && !accInfo.getString("cityname").isEmpty()) {
                addressValue = addressValue + ", " + accInfo.getString("cityname");
            }

            // State
            if (accInfo.getString("statename") != null && accInfo.getString("statename").length() > 0) {
                addressValue = addressValue + ", " + accInfo.getString("statename");
            }
            // State
            if (accInfo.getString("Zipcode") != null && accInfo.getString("Zipcode").length() > 0) {
                addressValue = addressValue + ", Pincode: " + accInfo.getString("Zipcode");
            }

            // Address
            if (addressValue.length() > 0) {

                // Email
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("key", "Address");
                item.put("value", addressValue);
                dataItems.add(item);

            }

            AppComponents.reloadDataWithEmptyHint(vwProfileList, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.optionEditProfile:

                Intent detailsScreen = new Intent(getActivity(), EditAccountActivity.class);
                startActivity(detailsScreen);
                Utilities.pushAnimation(getActivity());
                break;

            case R.id.optionLogout:

                Utilities.showAlertDialogWithOptions(getActivity(),  getResources().getString(R.string.alert_logout), new String[]{getResources().getString(R.string.btn_no), getResources().getString(R.string.btn_yes)}, new UtilitiesInterfaces.AlertCallback() {
                    @Override
                    public void onOptionClick(DialogInterface dialog, int buttonIndex) {

                        if(buttonIndex == 1){
//                            AppPreferences.getInstance().logout(getContext());
                            logout();
                        }

                    }
                });

                break;
            case R.id.locationUpdate:
                DashboardMapActivity d = (DashboardMapActivity)getContext();
                d.launchPlacePicker();
                break;
            case R.id.changePassword:
                customDialog.show();
                break;

        }

    }

//    private void getVersionInfo() {
//        String versionName = "";
//        long versionCode = -1;
//        try {
//            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
//            versionName = packageInfo.versionName;
//            versionCode = packageInfo.getLongVersionCode();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        textViewVersionInfo.setText("V "+ versionName+ " ("+versionCode+")");
//    }

    public void logout(){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        String webMethodName = Config.LOGOUT;

        try{

            inputParams.put("Username", inputParams.get("PatPhone").toString());

        }catch (Exception e){

        }

        inputParams.put("iDeviceType", "1");


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);

                    if(responseAry.length()>0){
                        JSONObject userInfo = responseAry.getJSONObject(0);
                        if(userInfo.has("APIStatus") && Integer.parseInt(userInfo.getString("APIStatus")) == -1){
                            if(userInfo.has("Message") && !userInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(getContext(),userInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(getContext(),"Logout",false);
                            }
                            return;

                        }

                        if(userInfo.has("Message") && !userInfo.getString("Message").isEmpty())
                            Utilities.showAlert(getContext(),userInfo.getString("Message"),false);


                    }

                }catch (Exception e){

                }

                //TODO TOKEN ISSUE
                //new MyFirebaseInstanceIDService().onNewToken();
                AppPreferences.getInstance().logout(getContext());


            }
        },true);
        String[] url = {Config.WEB_Services1,webMethodName,"POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        }else{

        }
    }


    public void changePassword(String password){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
        String webMethodName = Config.CHANGE_PASSWORD;

        try{

            inputParams.put("Username", inputParams.get("PatPhone").toString());
            inputParams.put("Password", password);
        }catch (Exception e){

        }

        inputParams.put("iDeviceType", "1");


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject userInfo = responseAry.getJSONObject(0);
                        if(userInfo.has("APIStatus") && Integer.parseInt(userInfo.getString("APIStatus")) == -1){
                            if(userInfo.has("Message") && !userInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(getContext(),userInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(getContext(),getResources().getString(R.string.please_check_again),false);
                            }
                            return;

                        }

                        if(userInfo.has("Message") && !userInfo.getString("Message").isEmpty())
                            Utilities.showAlert(getContext(),userInfo.getString("Message"),false);


                    }
                }catch (Exception e){

                }


            }
        },true);
        String[] url = {Config.WEB_Services1,webMethodName,"POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        }else{

        }
    }


    public class CustomDialogClass extends Dialog {

        public Activity activity;
        public Dialog dialog;
        public Button yes, no;

        public EditText password, confirmPassword;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.activity = a;
        }



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.password_change);

            yes = (Button) findViewById(R.id.btn_yes);
            no = (Button) findViewById(R.id.btn_no);
            password = (EditText) findViewById(R.id.password);
            confirmPassword = (EditText) findViewById(R.id.confirm_password);

            yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    if(password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty())
                    {
                        Utilities.showAlert(activity,getResources().getString(R.string.password_empty),true);
                    }else if(password.getText().toString().equals(confirmPassword.getText().toString()))
                    {
                        changePassword(password.getText().toString());

                    }else{
                        Utilities.showAlert(activity,getResources().getString(R.string.password_mismatch),true);
                    }
                    password.setText("");
                    confirmPassword.setText("");
                    dismiss();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    password.setText("");
                    confirmPassword.setText("");
                    dismiss();
                }
            });

        }

    }
}