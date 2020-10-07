package com.kal.connect.modules.dashboard.tabs.Medicine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.utilities.Utilities;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {


    // Step 1: Initialize By receiving the data via constructor
    Context mContext;
    ArrayList<HashMap<String, Object>> items;
    HashMap<String, Object> mHashMapMedcine;
    Activity mActivity;
    Button mBtnAddToCard, mBtnGoToCatalog;
    ArrayList<HashMap<String, Object>> sentParams;

    public MedicineAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context, Button mBtnAddToCard, Button mBtnGoToCatalog) {
        this.items = partnerItems;
        this.mContext = context;
        this.mBtnAddToCard = mBtnAddToCard;
        this.mBtnGoToCatalog = mBtnGoToCatalog;
        mActivity = (Activity) context;
        sentParams = new ArrayList<>();
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblName, lblTimeStamp;
        NumberPicker numberPicker;

        public ViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.lblName);
            lblTimeStamp = (TextView) view.findViewById(R.id.lblTimeStamp);
            numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        }


    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list_item, parent, false);
        return new MedicineAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MedicineAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);


        String prescriptionDate = (item.get("PriscriptionDate") != null) ? item.get("PriscriptionDate").toString() : "";
        if (!prescriptionDate.isEmpty()) {
            prescriptionDate = Utilities.changeStringFormat(Utilities.dateRT(item.get("PriscriptionDate").toString()), "yyyy-mm-dd", "dd/mm/yyyy");
            prescriptionDate = prescriptionDate + " ";
        }


        String ReportComment = (item.get("ReportComment") != null) ? item.get("ReportComment").toString() : "";

        holder.lblTimeStamp.setText(ReportComment);

        String medicineName = (item.get("Medicinename") != null) ? item.get("Medicinename").toString() : "";
        holder.lblName.setText(medicineName);

        holder.numberPicker.setMax(15);
        holder.numberPicker.setMin(0);
        holder.numberPicker.setUnit(1);
        holder.numberPicker.setValue(0);

        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {

                HashMap<String, Object> item = items.get(position);
                if (value > 0) {
                    item.put("isEnabled", true);
                    item.put("count", value);
                } else {
                    item.put("isEnabled", false);
                }
                items.set(position, item);
            }
        });


        mBtnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentParams.clear();

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).containsKey("isEnabled") && items.get(i).get("isEnabled").toString().equalsIgnoreCase("true")) {
                        Iterator entries = items.get(i).entrySet().iterator();
                        mHashMapMedcine = new HashMap<>();
                        while (entries.hasNext()) {
                            Map.Entry entry = (Map.Entry) entries.next();
                            mHashMapMedcine.put(entry.getKey().toString(), entry.getValue());
                        }
                        sentParams.add(mHashMapMedcine);
                    }
                }

                System.out.println(sentParams.size() + "");

//                https://www.keralaayurveda.store/cart/

                if (sentParams.size() > 0) {
                    gotoBrowser("https://www.keralaayurveda.store/cart/");
                }else{
                    Utilities.showToast(mContext,"Please add medcine");
                }
            }
        });


        mBtnGoToCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                https://www.keralaayurveda.biz/
                gotoBrowser("https://www.keralaayurveda.biz/product");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    void gotoBrowser(String Weburl) {
        String url = "";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Weburl));
        mContext.startActivity(i);
    }
}

