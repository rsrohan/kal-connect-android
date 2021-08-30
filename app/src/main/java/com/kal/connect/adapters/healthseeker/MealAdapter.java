package com.kal.connect.adapters.healthseeker;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.myCalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.DiseaseModel;
import com.kal.connect.models.healthseeker.PatientMealHabitsModel;

import java.util.ArrayList;
import java.util.Calendar;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PatientMealHabitsModel> mediModels;


    public MealAdapter(Context context, ArrayList<PatientMealHabitsModel> mediModels) {
        this.mContext = context;
        this.mediModels = mediModels;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText time;
        TextView mealType;
        SeekBar rating;

        public ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            mealType = view.findViewById(R.id.mealType);
            rating = view.findViewById(R.id.rating);

        }
    }

    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MealAdapter.ViewHolder holder, int position) {
        holder.time.setText(mediModels.get(position).getMealTime());
        holder.mealType.setText(mediModels.get(position).getMealType());
        if (mediModels.get(position).getHungerRatings()!=null){
            holder.rating.setProgress(Integer.parseInt(mediModels.get(position).getHungerRatings()));
        }

        holder.time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setMealTime(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediModels.get(position).setHungerRatings(progress+"");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setTime(holder.time);

    }

    public void setTime(EditText editText){
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ampm = "AM";
                        if (selectedHour>11){
                            ampm = "PM";
                            if (selectedHour>12){
                                selectedHour = selectedHour-12;
                            }
                        }else{
                            ampm = "AM";
                        }
                        if ((""+selectedHour).length()==1 && (""+selectedMinute).length()==1){
                            editText.setText( "0"+selectedHour + ":0" + selectedMinute+" "+ampm);
                        } else if ((""+selectedHour).length()==2 && (""+selectedMinute).length()==1){
                            editText.setText( ""+selectedHour + ":0" + selectedMinute+" "+ampm);
                        } if ((""+selectedHour).length()==1 && (""+selectedMinute).length()==2){
                            editText.setText( "0"+selectedHour + ":" + selectedMinute+" "+ampm);
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();            }
        });
    }

    @Override
    public int getItemCount() {
        return mediModels.size();
    }


}
