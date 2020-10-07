package com.kal.connect.modules.hospitals;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.HospitalListAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HospitalsList extends CustomActivity {

    @BindView(R.id.hospitals_list)
    RecyclerView hospitalsList;

    HospitalListAdapter dataAdapter;
    ArrayList<HospitalModel> dateItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitals_list);
        ButterKnife.bind(this);
        buildUI();
    }

    public void buildUI(){
        setHeaderView(R.id.headerView, HospitalsList.this, HospitalsList.this.getResources().getString(R.string.hospitals_list));
        headerView.showBackOption();



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        hospitalsList.setNestedScrollingEnabled(false);
        hospitalsList.setLayoutManager(mLayoutManager);
        hospitalsList.setItemAnimator(new DefaultItemAnimator());

        dataAdapter = new HospitalListAdapter(this,dateItems);
        hospitalsList.setAdapter(dataAdapter);
//        AppComponents.reloadRecyclerViewCustomDataWithEmptyHint(hospitalsList, dataAdapter, dateItems, this.getResources().getString(R.string.no_hospitals_found));
        getHospitalsList();
    }

    void createHospitalsList(String response){

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type hospitalListType = new TypeToken<ArrayList<HospitalModel>>(){}.getType();
        dateItems = gson.fromJson(response, hospitalListType);
        dataAdapter = new HospitalListAdapter(this,dateItems);
        hospitalsList.setAdapter(dataAdapter);
//        AppComponents.reloadRecyclerViewCustomDataWithEmptyHint(hospitalsList, dataAdapter, dateItems, this.getResources().getString(R.string.no_hospitals_found));




//        HospitalModel[] hospitalsList  = gson.fromJson(response, HospitalModel[].class);

    }

    public void getHospitalsList(){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();



        SoapAPIManager apiManager = new SoapAPIManager(HospitalsList.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(HospitalsList.this,commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(HospitalsList.this,"Please check again!",false);
                            }
                            return;

                        }
                        createHospitalsList(response);
                    }
                }catch (Exception e){

                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.GET_HOSPITALS_LIST,"POST"};

        if (Utilities.isNetworkAvailable(HospitalsList.this)) {
            apiManager.execute(url);
        }else{

        }
    }

}
