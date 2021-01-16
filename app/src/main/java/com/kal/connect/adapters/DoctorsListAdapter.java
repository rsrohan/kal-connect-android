package com.kal.connect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
//import androidx.annotation.NonNull;
//import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
//import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.DoctorModel;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.modules.communicate.VideoConferenceActivity;
import com.kal.connect.modules.dashboard.tabs.HomeScreen.AboutDoctorActivity;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.MyViewHolder> {

    Context context;
    ArrayList<DoctorModel> doctorslist;// = new ArrayList<>();

//    String SESSION = "2_MX40NTQ2NzMwMn5-MTU4NDE4OTc0NDAzNX51LzBoQXNPcWQyOE9yZFRKRE1qNHluQWl-fg";
//    String TOKEN = "T1==cGFydG5lcl9pZD00NTQ2NzMwMiZzaWc9NzhkM2ZjMTNjMzBkYjcxOWIxNTNjNDMwM2YxMmM1ZGRiMDEyNzcyOTpzZXNzaW9uX2lkPTJfTVg0ME5UUTJOek13TW41LU1UVTROREU0T1RjME5EQXpOWDUxTHpCb1FYTlBjV1F5T0U5eVpGUktSRTFxTkhsdVFXbC1mZyZjcmVhdGVfdGltZT0xNTg0MTg5Nzc5Jm5vbmNlPTAuMjkwNjQwMDE0MjA2ODE0ODUmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU4Njc4MTc3NiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";

    public DoctorsListAdapter(Context context, ArrayList<DoctorModel> doctorslist) {
        this.context = context;
        this.doctorslist = doctorslist;
    }

    @NonNull
    @Override
    public DoctorsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_doctors_list, viewGroup, false);
        itemView.setBackgroundColor(Color.WHITE);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        DoctorModel doctor = doctorslist.get(i);

        if (holder.category != null)
            holder.category.setText(doctor.getSpecializationName());
        if (holder.qualification != null)
            holder.qualification.setText(doctor.getQualification());
        if (holder.docName != null)
            holder.docName.setText(doctor.getName());
        if (holder.location != null)
            holder.location.setText(doctor.getAddress() + " " + doctor.getZipcode());
        if (doctor.getIsLoggedIn())
            holder.onlineStatus.setImageResource(R.drawable.icon_online);
        else
            holder.onlineStatus.setImageResource(R.drawable.icon_offline);

        if(AppPreferences.getInstance().getCountryCode().toString().equals("+91"))
        {
            if (!doctor.getVCCharge().isEmpty())
                holder.doctorCharge.setText("Consultation Charge : Rs " + doctor.getVCCharge());
        }else{
            if (!doctor.getDocIntCharge().isEmpty()){
                holder.doctorCharge.setText("Consultation Charge : Rs " + doctor.getDocIntCharge());
            }else{
                if (!doctor.getVCCharge().isEmpty())
                    holder.doctorCharge.setText("Consultation Charge : Rs " + doctor.getVCCharge());
            }

        }



//        if(doctor.getId().equals("17956")){
//            holder.doctorsList.setText("Dr Dinesh Raj \nDr Amit Raj \nDr Keyur Jatakiya \nDr Nihar Sojitra");
//            holder.doctorsList.setVisibility(View.VISIBLE);
//        }else{
//            holder.doctorsList.setVisibility(View.GONE);
//        }

        holder.bookBtn.setTag(doctor);
        holder.aboutBtn.setTag(doctor);

        Utilities.loadImageWithPlaceHoler(holder.profileImg, doctor.getDoctorImage(), doctor.getName());


    }

    @Override
    public int getItemCount() {
        return this.doctorslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView category, qualification, docName, location, doctorsList, doctorCharge;
        public ImageView profileImg, onlineStatus;

        public CircularRevealCardView profileCircleImgVw;
        LinearLayout bookBtn, aboutBtn;

        public MyViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.doctor_category);
            qualification = (TextView) view.findViewById(R.id.doctor_qualification);

            docName = (TextView) view.findViewById(R.id.doctor_name);
            bookBtn = (LinearLayout) view.findViewById(R.id.book_btn);
            aboutBtn = (LinearLayout) view.findViewById(R.id.about_btn);
            profileImg = (ImageView) view.findViewById(R.id.profileImage);
            profileCircleImgVw = (CircularRevealCardView) view.findViewById(R.id.profileCircleImage);
            location = (TextView) view.findViewById(R.id.location);
            onlineStatus = (ImageView) view.findViewById(R.id.online_status);
            doctorCharge = (TextView) view.findViewById(R.id.doctor_charge);

            aboutBtn.setOnClickListener(this);
            bookBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.about_btn) {

//                DoctorModel doctor = (DoctorModel) v.getTag();
//                GlobValues.getInstance().setAboutDoctor(doctor);
//                context.startActivity(new Intent(context, AboutDoctor.class));


                DoctorModel doctor = (DoctorModel) v.getTag();
                GlobValues g = GlobValues.getInstance();
                GlobValues.getInstance().setAboutDoctor(doctor);
                g.addAppointmentInputParams("SpecialistID", doctor.getSpecialistID());
                g.addAppointmentInputParams("SpecialistName", doctor.getName());
                g.setDoctor(doctor);

                Intent mIntent = new Intent(context, AboutDoctorActivity.class);
                mIntent.putExtra("SpecialistID", doctor.getSpecialistID().toString());
                context.startActivity(mIntent);

                Utilities.pushAnimation((Activity) context);
            }

            if (v.getId() == R.id.book_btn) {
                DoctorModel doctor = (DoctorModel) v.getTag();
                GlobValues g = GlobValues.getInstance();
                GlobValues.getInstance().setAboutDoctor(doctor);
                g.addAppointmentInputParams("SpecialistID", doctor.getSpecialistID());
                g.addAppointmentInputParams("SpecialistName", doctor.getName());
                g.setDoctor(doctor);

                Intent mIntent = new Intent(context, AboutDoctorActivity.class);
                mIntent.putExtra("SpecialistID", doctor.getSpecialistID().toString());
                context.startActivity(mIntent);

//                getVideoCallConfigurations(doctor.getName());

//                Intent intent = new Intent(context, VideoConference.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                intent.putExtra("SESSION_ID",SESSION);
//                intent.putExtra("TOKEN",TOKEN);
//                intent.putExtra("CALER_NAME",docName.getText().toString());
//                intent.putExtra("CALL_TYPE",2);
//                Utilities.pushAnimation((Activity) context);


//                context.startActivity(new Intent(context, AppointmentSummary.class));

            }


        }


    }

    public void getVideoCallConfigurations(String doctorName) {
//        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
//        inputParams.put("ComplaintID",GlobValues.getInstance().getSelectedAppointment());
        HashMap<String, Object> appointmentinputParams = GlobValues.getInstance().getAddAppointmentParams();
        SoapAPIManager apiManager = new SoapAPIManager(context, appointmentinputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(context, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(context, "Please check again!", false);
                            }
                            return;

                        }
//                        loadAppointments(responseAry);
                        if (commonDataInfo.has("VCToekn") && !commonDataInfo.getString("VCToekn").isEmpty() &&
                                commonDataInfo.has("VSSessionID") && !commonDataInfo.getString("VSSessionID").isEmpty()) {
//                            String TOKEN = commonDataInfo.getString("VCToekn");
//                            String SESSION = commonDataInfo.getString("VSSessionID");
//
//                            Intent intent = new Intent(context, VideoCaller.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            intent.putExtra("SESSION_ID",SESSION);
//                            intent.putExtra("TOKEN",TOKEN);
//
//                            intent.putExtra("CALER_NAME",docName.getText().toString());
//                            intent.putExtra("CALL_TYPE",2);
//                            startActivity(intent);
//                            Utilities.pushAnimation(context);

                            Intent intent = new Intent(context, VideoConferenceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                            intent.putExtra("SESSION_ID",commonDataInfo.getString("VSSessionID"));
//                            intent.putExtra("TOKEN",commonDataInfo.getString("VCToekn"));

                            OpenTokConfigConstants.SESSION_ID = commonDataInfo.getString("VSSessionID");
                            OpenTokConfigConstants.TOKEN = commonDataInfo.getString("VCToekn");
                            intent.putExtra("CALER_NAME", doctorName);

                            intent.putExtra("CALL_TYPE", 2);

                            context.startActivity(intent);
                            Utilities.pushAnimation((Activity) context);

                        }


                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.INITIATE_VIDEO_CALL, "POST"};

        if (Utilities.isNetworkAvailable(context)) {
            apiManager.execute(url);
        } else {

        }
    }
    public void updateList(ArrayList<DoctorModel> list){
        doctorslist = list;
        notifyDataSetChanged();
    }

}
