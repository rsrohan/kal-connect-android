package com.kal.connect.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.AppointmentDetailActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {


    // Step 1: Initialize By receiving the data via constructor
    Context mContext;
    static String TAG = "AppointmentsAdapter";
    ArrayList<HashMap<String, Object>> items;
    private static AppointmentsAdapter.ItemClickListener itemClickListener;
    HashMap<String, Object> appointmentinputParams;
    Activity mActivity;

    public AppointmentsAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
        this.items = partnerItems;
        this.mContext = context;
        mActivity = (Activity) context;
        appointmentinputParams = new HashMap<>();
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgStatus, mImgDelete;
        public TextView lblName, lblDegree, lblTimeStamp, lblStatus;
        public TagGroup symptomsTags;
        public LinearLayout videoLayout, statusLayout, mLlRoot; //directionLayout, , tecStatusContainer;
//        public FrameLayout techStatusLayout;

        public ViewHolder(View view) {
            super(view);

            mImgDelete = (ImageView) view.findViewById(R.id.img_delete);
            imgStatus = (ImageView) view.findViewById(R.id.imgOnline);
            symptomsTags = (TagGroup) view.findViewById(R.id.symptoms);
            imgStatus = (ImageView) view.findViewById(R.id.imgStatus);

            lblName = (TextView) view.findViewById(R.id.lblName);
            lblDegree = (TextView) view.findViewById(R.id.lblDegree);
            lblTimeStamp = (TextView) view.findViewById(R.id.lblTimeStamp);

//            techStatusLayout = (FrameLayout) view.findViewById(R.id.techStatus);
            videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
//            directionLayout = (LinearLayout) view.findViewById(R.id.directionLayout);
            statusLayout = (LinearLayout) view.findViewById(R.id.statusLayout);
            mLlRoot = (LinearLayout) view.findViewById(R.id.vwContainer);

//            tecStatusContainer = (LinearLayout) view.findViewById(R.id.techStatusContainer);

            lblStatus = (TextView) view.findViewById(R.id.lblStatus);


        }


    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_item, parent, false);
        return new AppointmentsAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AppointmentsAdapter.ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);

        Log.e(TAG, "onBindViewHolder: " + item.toString());

        String doctorName = (item.get("doctorName") != null) ? item.get("doctorName").toString() : "";
        holder.lblName.setText(doctorName);

        String qualification = (item.get("qualification") != null) ? item.get("qualification").toString() : "";
        holder.lblDegree.setText(qualification);

        holder.lblTimeStamp.setText(item.get("appointmentDate").toString());

        String appointmentStatus = (item.get("status") != null) ? item.get("status").toString() : "Status";
        holder.lblStatus.setText(Utilities.getStatus(mContext, appointmentStatus));

        if (item.get("cancelstatus") != null && !item.get("cancelstatus").equals("false")) {
            holder.mImgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.mImgDelete.setVisibility(View.GONE);
        }


        holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to Cancel it");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        cancelAppointment(position, item.get("time").toString(), item.get("appointmentDate").toString(), item.get("appointmentId").toString());

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ArrayList<HashMap<String, Object>> symptoms = (ArrayList<HashMap<String, Object>>) item.get("symptoms");

        List<String> tags = new ArrayList<String>();
        for (HashMap<String, Object> tag : symptoms) {
            tags.add(tag.get("title") + "");
        }
        holder.symptomsTags.setTags(tags);


        holder.mLlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> selectedItem = items.get(position);
                if (selectedItem.get("appointmentId") != null) {
                    GlobValues.getInstance().setSelectedAppointment(selectedItem.get("appointmentId").toString());
                    GlobValues.getInstance().setSelectedAppointmentData(selectedItem);
                    getAppointmentsDetails(doctorName, item.get("doctorId").toString(), qualification,item.get("appointmentDate").toString(), Utilities.getStatus(mContext, appointmentStatus));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Step 4: Create Interface and it's methods to Receive Click event
    public interface ItemClickListener {
        public void onItemClick(int position, View v);
    }

    // Method to receive Click event
    public void setOnItemClickListener(AppointmentsAdapter.ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    void getAppointmentsDetails(String doctorName, String docId, String docQual, String appointmentTime, String appointmentStatus) {


        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        HashMap<String, Object> selectedAppointmentData = GlobValues.getSelectedAppointmentData();
        inputParams.put("appointmentID", selectedAppointmentData.get("appointmentId").toString());
        inputParams.put("ComplaintID", selectedAppointmentData.get("ComplaintID").toString());

        SoapAPIManager apiManager = new SoapAPIManager(mContext, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus")) {
                            if (commonDataInfo.has("Message")) {
                                Utilities.showAlert(mContext, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(mContext, "Please check again!", false);
                            }
                            return;
                        }

                        GlobValues.getInstance().setAppointmentCompleteDetails(responseAry.getJSONObject(0));
                        Intent detailsScreen = new Intent(mContext, AppointmentDetailActivity.class);
                        detailsScreen.putExtra("doctorName", doctorName);
                        detailsScreen.putExtra("docId", docId);
                        detailsScreen.putExtra("docQual",docQual);
                        detailsScreen.putExtra("appointmentTime",appointmentTime);
                        detailsScreen.putExtra("appointmentStatus",appointmentStatus);
                        mContext.startActivity(detailsScreen);

                        Utilities.pushAnimation(mActivity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_APPOINTMENTS_DETAILS, "POST"};

        if (Utilities.isNetworkAvailable(mContext)) {
            apiManager.execute(url);
        } else {

        }
    }


    void cancelAppointment(int position, String time, String date, String appointmentId) {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
        String appointmentDate = Utilities.changeStringFormat(date.split(" ")[0], "dd/mm/yyyy", "dd/mm/yyyy");

        appointmentinputParams.put("appointmentID", Integer.parseInt(appointmentId));
        appointmentinputParams.put("Offset", 300);
        appointmentinputParams.put("AppointmentTime", time);
        appointmentinputParams.put("PatientID", Integer.parseInt(inputParams.get("patientID").toString()));
        appointmentinputParams.put("AppointmentDate", appointmentDate);


        SoapAPIManager apiManager = new SoapAPIManager(mContext, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    Utilities.showAlert(mContext, "Appointment cancelled successfully", false);
                    appointmentinputParams.remove(position);
                    notifyItemRemoved(position);
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.CANCEL_APPOINTMENT, "POST"};

        if (Utilities.isNetworkAvailable(mContext)) {
            apiManager.execute(url);
        } else {

        }
    }


}

