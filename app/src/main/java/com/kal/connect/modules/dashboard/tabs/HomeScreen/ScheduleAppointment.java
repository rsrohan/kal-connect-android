package com.kal.connect.modules.dashboard.tabs.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.kal.connect.R;
import com.kal.connect.adapters.AppointmentSchedulerAdapter;
import com.kal.connect.customLibs.appCustomization.CustomActivity;

import com.kal.connect.models.DoctorModel;
import com.kal.connect.models.ObjAvalTimeModel;
import com.kal.connect.adapters.AppointmentsAdapter;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAppointment extends CustomActivity implements CalendarView.OnDateChangeListener {

    @BindView(R.id.appointments)
    RecyclerView appointments;

    @BindView(R.id.calender)
    CalendarView calenderView;

    AppointmentSchedulerAdapter dataAdapter;
    ArrayList<ObjAvalTimeModel> dateItems = new ArrayList<>();

    GlobValues g = GlobValues.getInstance();

    DoctorModel doctorModel = g.getAboutDoctor();

    String selectedDate = "";
    String selectedTime = "";
//    @OnClick(R.id.consult_later)
//    void consultLater(){
//
//        if(selectedDate.isEmpty() || selectedTime.isEmpty()){
//            Utilities.showAlert(this,"Please select Appointment date and Time",false);
//            return;
//        }
//        GlobValues.getInstance().addAppointmentInputParams("ConsultNow",2);
//
//        g.addAppointmentInputParams("AppointmentDate", selectedDate);
//        g.addAppointmentInputParams("AppointmentTime", selectedTime);
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        ButterKnife.bind(this);
        buildUI();
    }

    public void buildUI() {
        setHeaderView(R.id.headerView, ScheduleAppointment.this, ScheduleAppointment.this.getResources().getString(R.string.schedule_appointment));
        headerView.showBackOption();

        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long date = calendar.getTime().getTime();
        calenderView.setMinDate(date);

        Calendar maximumDate = Calendar.getInstance();

        maximumDate.add(Calendar.DATE, 4);
        calenderView.setMaxDate(maximumDate.getTime().getTime());
        calenderView.setDate(date);
        calenderView.setOnDateChangeListener(this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL  , false);

        appointments.setNestedScrollingEnabled(false);
        appointments.setLayoutManager(mLayoutManager);
        appointments.setItemAnimator(new DefaultItemAnimator());

        dataAdapter = new AppointmentSchedulerAdapter(this,dateItems);

        AppointmentSchedulerAdapter.setItemClickListener(new AppointmentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(dateItems.get(position).getBooked()){
//                    g.addAppointmentInputParams("AppointmentTime", dateItems.get(position).getTime());
//                    startActivity(new Intent(ScheduleAppointment.this, AppointmentSummary.class));
//                }else{
                    Utilities.showAlert(ScheduleAppointment.this, "Please choose available slots",false);
                    return;
                }

                g.addAppointmentInputParams("AppointmentTime", dateItems.get(position).getTime());
                g.addAppointmentInputParams("AppointmentDate", selectedDate);
                startActivity(new Intent(ScheduleAppointment.this, AppointmentSummaryActivity.class));

            }
        });

        appointments.setAdapter(dataAdapter);

//        Calendar c = Calendar.getInstance();
//        int Hr24=c.get(Calendar.HOUR_OF_DAY);
        selectedDate = Utilities.getCurrentDate("MM/dd/yyyy");
        createDataForDate(selectedDate);
    }

    void createDataForDate(String date){
        dateItems.clear();
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());
//        dateItems.add(new AppointmentScheduleTimer());

//        String[] quarterHours = {"00","15","30","45"};

//        Calendar c = Calendar.getInstance();
//
//        int Hr24=c.get(Calendar.HOUR_OF_DAY);
//        int Min=c.get(Calendar.MINUTE);

//        String[] quarterHours = {"00","30"};
//        List<String> times = new ArrayList<String>; // <-- List instead of array

//        for(int i = Hr24; i < 24; i++) {
//            for(int j = 0; j < 2; j++) {
//                String amPM = " am";
//                String time = "";
//                if(i >13){
//                    time = i-12 + ":" + quarterHours[j];
//                    amPM = " pm";
//                }else{
//                     time = i + ":" + quarterHours[j];
//                }
//
//                if(i < 10) {
//                    time = "0" + time;
//                }
//                time = time + amPM;
//
//                Random r = new Random();
//                int low = 10;
//                int high = 100;
//                int result = r.nextInt(high-low) + low;
//
//                if(result % 2 == 0)
//                    dateItems.add(new AppointmentScheduleTimer(time, true));
//                else
//                    dateItems.add(new AppointmentScheduleTimer(time,false));
//            }
//        }

        for(int i = 0; i< doctorModel.getObjdocAvailModel().size(); i++){
            if(doctorModel.getObjdocAvailModel().get(i).getAvailableDate().equals(date)){
                dateItems = new ArrayList(doctorModel.getObjdocAvailModel().get(i).getObjAvalTimeModels());
                break;
            }
        }
//        dateItems = doctorModel.getObjdocAvail().get

        dataAdapter.setAppointmentSchedules(dateItems);
        AppComponents.reloadRecyclerViewCustomDataWithEmptyHint(appointments,dataAdapter,dateItems,"Please select different date!");
//        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//        Calendar c = Calendar.getInstance();
//
//        int Hr24=c.get(Calendar.HOUR_OF_DAY);
//        int Min=c.get(Calendar.MINUTE);
//        if(year == c.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_MONTH) == dayOfMonth && month == c.get(Calendar.MONTH)){
//            createSampleData(Hr24+1);
//        }else{
//            createSampleData(0);
//        }

        month = month+1;
        String monthStr = ""+month;
        String dayString = ""+dayOfMonth;
        if(month<10)
            monthStr = "0"+monthStr;

        if(dayOfMonth<10)
            dayString = "0"+dayString;

        selectedDate = monthStr + "/" + dayString + "/" + year;
//        MM/dd/yyyy
        createDataForDate(selectedDate);

    }
}
