package com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kal.connect.R;
import com.kal.connect.adapters.MedicineAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.models.ProductModel;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineActivity extends CustomActivity implements View.OnClickListener {

    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwAppointments;
    public MedicineAdapter dataAdapter = null;
    ImageView mImgAddProduct, mImgOrder, mImgUplod;
    TextView mTxtPlaceOrder, mTxtAddProduct, mTxtUpload;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        ButterKnife.bind(this);
        buildUI();

    }

    private void buildUI() {

        mTxtAddProduct = (TextView) findViewById(R.id.txt_add_product);
        mTxtPlaceOrder = (TextView) findViewById(R.id.txt_place_order);
        mTxtUpload = (TextView) findViewById(R.id.txt_upload);

        mImgAddProduct = (ImageView) findViewById(R.id.img_product);
        mImgOrder = (ImageView) findViewById(R.id.img_order);
        mImgUplod = (ImageView) findViewById(R.id.img_upload_descr);

        setHeaderView(R.id.headerView, MedicineActivity.this, MedicineActivity.this.getResources().getString(R.string.tab_medicine));
        headerView.showBackOption();


        vwAppointments = (RecyclerView) findViewById(R.id.appointmentsView);
        buildListView();
        getMedicineList();

        mSwipeRefreshLayout.setColorSchemeColors(Utilities.getColor(getApplicationContext(), R.color.colorPrimary), Utilities.getColor(getApplicationContext(), R.color.colorPrimaryDark), Utilities.getColor(MedicineActivity.this, R.color.colorPrimary));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getMedicineList();
            }
        });

        mTxtAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeColor(mImgAddProduct, mTxtAddProduct);

                Intent mIntent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivityForResult(mIntent, 101);

            }
        });


//        mTxtPlaceOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //changeColor(mImgOrder, mTxtPlaceOrder);
//                sentParams.clear();
//
//                for (int i = 0; i < items.size(); i++) {
//                    if (items.get(i).containsKey("isEnabled") && items.get(i).get("isEnabled").toString().equalsIgnoreCase("true")) {
//                        Iterator entries = items.get(i).entrySet().iterator();
//                        mHashMapMedcine = new HashMap<>();
//                        while (entries.hasNext()) {
//                            Map.Entry entry = (Map.Entry) entries.next();
//                            mHashMapMedcine.put(entry.getKey().toString(), entry.getValue());
//                        }
//                        sentParams.add(mHashMapMedcine);
//                    }
//                }
//
//                if (sentParams.size() > 0) {
//                    placeOrder(holder);
//                } else {
//                    Utilities.showAlert(mContext, "Please add medicine!", false);
//                }
//
//
//            }
//        });

        mTxtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeColor(mImgUplod, mTxtUpload);
                Intent mIntent = new Intent(getApplicationContext(), PrescriptionUploadActivity.class);
                startActivity(mIntent);
            }
        });

    }


    private void buildListView() {

        dataAdapter = new MedicineAdapter(MedicineActivity.this, dataItems, MedicineActivity.this, mTxtPlaceOrder);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MedicineActivity.this, LinearLayoutManager.VERTICAL, false);

        vwAppointments.setNestedScrollingEnabled(false);
        vwAppointments.setLayoutManager(mLayoutManager);
        vwAppointments.setItemAnimator(new DefaultItemAnimator());
        vwAppointments.setAdapter(dataAdapter);


        //#todo WHY THIS ???
        AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, MedicineActivity.this.getResources().getString(R.string.no_medicines_found));

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            ProductModel mProductModel = (ProductModel) data.getSerializableExtra("Model");
            HashMap<String, Object> mHashMapAddToCard = new HashMap<>();
            mHashMapAddToCard.put("Medicinename", mProductModel.getMedicineName());
            mHashMapAddToCard.put("amount", mProductModel.getDiscountedprice());
            mHashMapAddToCard.put("ReportComment", mProductModel.getMeddiscription());
            mHashMapAddToCard.put("isEnabled", true);
            if (dataItems.size()>0)
            {
                if (dataItems.contains(mHashMapAddToCard)){

                    Utilities.showAlert(MedicineActivity.this, "Already Exists", false);


                }else{
                    dataItems.add(mHashMapAddToCard);
                }

            }else{
                dataItems.add(mHashMapAddToCard);
            }
            dataAdapter.notifyDataSetChanged();
            vwAppointments.setAdapter(dataAdapter);

        }

    }

    // MARK : API
    private void loadAppointments(JSONArray medicineNameArray) {

        // Show loading only at first time
        Boolean showLoading = dataItems.size() == 0;
        dataItems.clear();

        // Make API Call here!
        for (int loop = 0; loop < medicineNameArray.length(); loop++) {
            try {
                JSONObject singleObj = medicineNameArray.getJSONObject(loop);
                HashMap<String, Object> item = new HashMap<String, Object>();

                String PriscriptionDate = (singleObj.getString("PriscriptionDate") != null) ? singleObj.getString("PriscriptionDate") : "";
                if (!PriscriptionDate.isEmpty()) {
                    PriscriptionDate = Utilities.changeStringFormat(Utilities.dateRT(singleObj.getString("PriscriptionDate")), "yyyy-mm-dd", "dd/mm/yyyy");
                    PriscriptionDate = PriscriptionDate + " " + singleObj.getString("PriscriptionDate");
                }

                item.put("PriscriptionDate", singleObj.getString("PriscriptionDate"));
                item.put("Medicinename", singleObj.getString("Medicinename"));
                item.put("isEnabled", true);
                item.put("ReportComment", singleObj.getString("ReportComment"));

                dataItems.add(item);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, MedicineActivity.this.getResources().getString(R.string.no_data_found));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void getMedicineList() {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();


        SoapAPIManager apiManager = new SoapAPIManager(MedicineActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("Message") && commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(MedicineActivity.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(MedicineActivity.this, "Please check again!", false);
                            }
                            return;

                        }
                        loadAppointments(responseAry);
                    }
                } catch (Exception e) {

                } finally {
                    mSwipeRefreshLayout.setRefreshing(false);
                    dataAdapter.notifyDataSetChanged();
                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_BUY_MEDICINE, "POST"};

        if (Utilities.isNetworkAvailable(MedicineActivity.this)) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(MedicineActivity.this, "Please check internet!", false);

        }
    }
}
