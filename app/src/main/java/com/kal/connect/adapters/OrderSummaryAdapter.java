package com.kal.connect.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.MedicineActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {


    private static final String TAG = "AdapterSummary";
    Context mContext;
    ArrayList<HashMap<String, Object>> items;

    public OrderSummaryAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
        this.items = partnerItems;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblName, mTxtAmount, mTxtQuantity;

        public ViewHolder(View view) {
            super(view);
            mTxtAmount = (TextView) view.findViewById(R.id.txt_medicine_offer_amt);
            lblName = (TextView) view.findViewById(R.id.lblName);
            mTxtQuantity = (TextView) view.findViewById(R.id.tv_quantity);

        }


    }

    @Override
    public OrderSummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list_summary_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(OrderSummaryAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);


        String medicineName = (item.get("Medicinename") != null) ? item.get("Medicinename").toString() : "";
        holder.lblName.setText(medicineName);


        //String medicineCount = (item.get("MedicineCount") != null) ? item.get("MedicineCount").toString() : "";

        if (item.get("MedicineCount") != null) {
            String count = item.get("MedicineCount").toString();
            try {
                holder.mTxtQuantity.setText("Quantity: " + Integer.parseInt(count));
            } catch (Exception e) {
                Log.e(TAG, "onBindViewHolder: " + e);
                holder.mTxtQuantity.setText("Quantity: NA");
            }
            if (item.get("amount") != null && !item.get("amount").toString().equalsIgnoreCase("")) {
                holder.mTxtAmount.setText(Double.parseDouble(item.get("amount").toString()) * Integer.parseInt(count) + "");
            } else {
                holder.mTxtAmount.setText("NA");
            }

        } else {
            holder.mTxtQuantity.setText("Quantity: NA");
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

