package com.kal.connect.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.MedicineActivity;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.PrescriptionActivity;
import com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.ProductHomeActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ImageView mImgAddProduct, mImgOrder, mImgUplod;
    ArrayList<HashMap<String, Object>> sentParams;
    MedicineActivity mMedicineActivity;
    TextView mTxtPlaceOrder, mTxtAddProduct, mTxtUpload;

    public MedicineAdapter(MedicineActivity mMedicineActivity, ArrayList<HashMap<String, Object>> partnerItems, Context context, ImageView mImgAddProduct, ImageView mImgOrder, ImageView mImgUplod, TextView mTxtAddProduct, TextView mTxtPlaceOrder, TextView mTxtUpload) {
        this.items = partnerItems;
        this.mContext = context;
        this.mMedicineActivity = mMedicineActivity;
        this.mImgAddProduct = mImgAddProduct;
        this.mImgOrder = mImgOrder;
        this.mImgUplod = mImgUplod;

        this.mTxtPlaceOrder = mTxtPlaceOrder;
        this.mTxtAddProduct = mTxtAddProduct;
        this.mTxtUpload = mTxtUpload;
        mActivity = (Activity) context;
        sentParams = new ArrayList<>();
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblName, lblTimeStamp, mTxtAmount;
        NumberPicker numberPicker;
        ImageView mImgRubees;

        public ViewHolder(View view) {
            super(view);
            mTxtAmount = (TextView) view.findViewById(R.id.txt_medicine_offer_amt);
            lblName = (TextView) view.findViewById(R.id.lblName);
            lblTimeStamp = (TextView) view.findViewById(R.id.lblTimeStamp);
            numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
            mImgRubees = (ImageView) view.findViewById(R.id.img_amt);
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

        if (item.containsKey("amount") && item.get("amount") != null) {
            holder.mTxtAmount.setText(item.get("amount").toString());
            holder.mImgRubees.setVisibility(View.VISIBLE);
            holder.numberPicker.setMax(15);
            holder.numberPicker.setMin(0);
            holder.numberPicker.setUnit(1);
            holder.numberPicker.setValue(0);
        } else {
            holder.mImgRubees.setVisibility(View.GONE);
            holder.mTxtAmount.setText("");
            holder.numberPicker.setMax(15);
            holder.numberPicker.setMin(0);
            holder.numberPicker.setUnit(1);
            holder.numberPicker.setValue(0);
        }


        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {

                holder.mImgRubees.setVisibility(View.GONE);

                HashMap<String, Object> item = items.get(position);
                if (value > 0) {
                    item.put("isEnabled", true);
                    item.put("MedicineCount", value);
                    if (item.get("amount") != null && !item.get("amount").toString().equalsIgnoreCase("")) {
                        holder.mTxtAmount.setText(Double.parseDouble(item.get("amount").toString()) * value + "");
                        holder.mImgRubees.setVisibility(View.VISIBLE);
                    }
                    items.set(position, item);
                } else {
                    item.put("isEnabled", false);
                    if (item.get("amount") != null && !item.get("amount").toString().equalsIgnoreCase("")) {
                        int newPosition = holder.getAdapterPosition();
                        items.remove(newPosition);
                        notifyItemRemoved(newPosition);
                        notifyItemRangeChanged(newPosition, items.size());
                    }
                }

            }
        });


        mTxtAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeColor(mImgAddProduct, mTxtAddProduct);

                Intent mIntent = new Intent(mActivity, ProductHomeActivity.class);
                mActivity.startActivityForResult(mIntent, 101);

            }
        });


        mTxtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeColor(mImgOrder, mTxtPlaceOrder);
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

                if (sentParams.size() > 0) {
                    //placeOrder(holder);
                } else {
                    Utilities.showAlert(mContext, "Please add medicine!", false);
                }


            }
        });

        mTxtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeColor(mImgUplod, mTxtUpload);
                Intent mIntent = new Intent(mContext, PrescriptionActivity.class);
                mContext.startActivity(mIntent);
            }
        });

    }

    public void changeColor(ImageView mImgSelect, TextView mTxtSelect) {
        mImgAddProduct.setColorFilter(ContextCompat.getColor(mActivity, R.color.black));
        mImgOrder.setColorFilter(ContextCompat.getColor(mActivity, R.color.black));
        mImgUplod.setColorFilter(ContextCompat.getColor(mActivity, R.color.black));

        mTxtAddProduct.setTextColor(mContext.getResources().getColor(R.color.black));
        mTxtPlaceOrder.setTextColor(mContext.getResources().getColor(R.color.black));
        mTxtUpload.setTextColor(mContext.getResources().getColor(R.color.black));

        mImgSelect.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        mTxtSelect.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
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

    void placeOrder(ViewHolder holder) {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();
        inputParams.put("objMedicineList", new JSONArray(sentParams));

        SoapAPIManager apiManager = new SoapAPIManager(mContext, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText")) {
//                                Utilities.showAlert(mContext, commonDataInfo.getString("RespText"), false);
                                showAlert(commonDataInfo.getString("RespText"));
                            } else {
                                Utilities.showAlert(mContext, "Please check again!", false);
                            }
                            return;
                        }

                    }
                } catch (Exception e) {
                    e.getMessage();
                } finally {
                    mMedicineActivity.getMedicineList();
                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.EMAIL_MEDICINE_TO_PHARMACY, "POST"};

        if (Utilities.isNetworkAvailable(mContext)) {
            apiManager.execute(url);
        } else {

        }
    }

    void showAlert(String message) {
        System.out.println("RESPONSE HI");
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.playstore_update_view);

        TextView text = (TextView) dialog.findViewById(R.id.lblMessage);
        text.setText(message);

        Button mBtnOk = (Button) dialog.findViewById(R.id.playstore_update);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 15000);
        }

        dialog.show();


    }

}

