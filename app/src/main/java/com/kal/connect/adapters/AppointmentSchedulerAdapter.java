package com.kal.connect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kal.connect.R;
import com.kal.connect.models.ObjAvalTime;
import com.kal.connect.modules.dashboard.tabs.Appointments.AppointmentsAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.support.annotationon.NoonNull;
//import androidx.recyclerview.widget.RecyclerView;

public class AppointmentSchedulerAdapter extends RecyclerView.Adapter<AppointmentSchedulerAdapter.MyViewHolder> {

    Context context;
    ArrayList<ObjAvalTime> appointmentSchedules;

    private static AppointmentsAdapter.ItemClickListener itemClickListener;

    public static void setItemClickListener(AppointmentsAdapter.ItemClickListener itemClickListener) {
        AppointmentSchedulerAdapter.itemClickListener = itemClickListener;
    }

    public AppointmentSchedulerAdapter(Context context, ArrayList<ObjAvalTime> appointmentSchedules) {
        this.context = context;
        this.appointmentSchedules = appointmentSchedules;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_appointment_row, viewGroup, false);
//        itemView.setBackgroundColor(Color.WHITE);
        AppointmentSchedulerAdapter.MyViewHolder myViewHolder = new AppointmentSchedulerAdapter.MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        ObjAvalTime appointmentScheduleTimer = appointmentSchedules.get(i);
        holder.timeDisplay.setText(appointmentScheduleTimer.getTime());



        if(appointmentScheduleTimer.getBooked()){
            holder.availableLayout.setBackgroundColor(context.getResources().getColor(R.color.appoinment_red));
            holder.availablityImgVw.setImageDrawable(context.getResources().getDrawable(R.drawable.non_selectable,null));
        }
        else{
            holder.availableLayout.setBackgroundColor(context.getResources().getColor(R.color.mdtp_accent_color_dark));
            holder.availablityImgVw.setImageDrawable(context.getResources().getDrawable(R.drawable.check_box_selected,null));
        }


//        if(i >= appointmentSchedules.size() - 1){
//            holder.timeFromTo.setText(appointmentScheduleTimer.getTime() + " to 12:00 am");
//        }else{
//            holder.timeFromTo.setText(appointmentScheduleTimer.getTime() + " to "+ appointmentSchedules.get(i+1).getTime());
//        }
        holder.availableLayout.setTag(i);
    }

    public interface ItemClickListener {
        public void onItemClick(int position, View v);
    }


    public void setAppointmentSchedules(ArrayList<ObjAvalTime> appointmentSchedules) {
        this.appointmentSchedules = appointmentSchedules;
    }

    @Override
    public int getItemCount() {
        return appointmentSchedules.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.time)
        TextView timeDisplay;

        @BindView(R.id.time_from_to)
        TextView timeFromTo;

        @BindView(R.id.available_layout)
        RelativeLayout availableLayout;

        @BindView(R.id.availablity_img_ic)
        ImageView availablityImgVw;

        @OnClick(R.id.available_layout)
        void selectTime(){
            itemClickListener.onItemClick((int)availableLayout.getTag(), availableLayout);
        }



        @Override
        public void onClick(View v) {

        }

        public MyViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
