package com.kal.connect.modules.dashboard.tabs.Medicine;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Medicine extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwAppointments;
    MedicineAdapter dataAdapter = null;
    Button mBtnAddToCard, mBtnGoToCatalog;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.medicine, container, false);
        ButterKnife.bind(this, view);
        buildUI();
        return view;

    }

    private void buildUI() {

        mBtnAddToCard = (Button) view.findViewById(R.id.add_to_card);
        mBtnGoToCatalog = (Button) view.findViewById(R.id.goto_product);
        vwAppointments = (RecyclerView) view.findViewById(R.id.appointmentsView);
        buildListView();
        getMedicineList();

        mSwipeRefreshLayout.setColorSchemeColors(Utilities.getColor(getContext(), R.color.colorPrimary), Utilities.getColor(getContext(), R.color.colorPrimaryDark), Utilities.getColor(getContext(), R.color.colorPrimary));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getMedicineList();
            }
        });

    }


    private void buildListView() {

        dataAdapter = new MedicineAdapter(dataItems, getActivity(), mBtnAddToCard, mBtnGoToCatalog);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwAppointments.setNestedScrollingEnabled(false);
        vwAppointments.setLayoutManager(mLayoutManager);
        vwAppointments.setItemAnimator(new DefaultItemAnimator());
        vwAppointments.setAdapter(dataAdapter);


        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_appointments_found));

//        dataAdapter.setOnItemClickListener(new AppointmentsAdapter.ItemClickListener() {
//
//            @Override
//            public void onItemClick(int position, View v) {
//
//                HashMap<String, Object> selectedItem = dataItems.get(position);
//                if (selectedItem.get("appointmentId") != null) {
//                    GlobValues.getInstance().setSelectedAppointment(selectedItem.get("appointmentId").toString());
//                    GlobValues.getInstance().setSelectedAppointmentData(selectedItem);
//                    getAppointmentsDetails();
//                }
//
//            }
//
//        });

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

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
                item.put("isEnabled", false);
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
                    AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    void getMedicineList() {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("Message") && commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(getContext(), commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(getContext(), "Please check again!", false);
                            }
                            return;

                        }
                        loadAppointments(responseAry);

                    }
                } catch (Exception e) {

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_BUY_MEDICINE, "POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        } else {

        }
    }
}
