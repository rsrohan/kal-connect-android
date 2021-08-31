package com.kal.connect.adapters.healthseeker;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.myCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.DiseaseSurgeryHospitalizationModel;
import com.kal.connect.models.healthseeker.SurgeryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SurgeryAdapter extends RecyclerView.Adapter<SurgeryAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SurgeryModel> mediModels;


    public SurgeryAdapter(Context context, ArrayList<SurgeryModel> mediModels) {
        this.mContext = context;
        this.mediModels = mediModels;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText txt1, txt2, txt3;

        public ViewHolder(View view) {
            super(view);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            txt3 = view.findViewById(R.id.txt3);

        }
    }

    @Override
    public SurgeryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_describe_issue_past_surgery, parent, false);
        return new SurgeryAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SurgeryAdapter.ViewHolder holder, int position) {
        holder.txt1.setText(mediModels.get(position).getMethod());
        holder.txt2.setText(mediModels.get(position).getDateString());
        holder.txt3.setText(mediModels.get(position).getPerformedAt());

        holder.txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setMethod(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setPeriod(s+"");
                mediModels.get(position).setDateString(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.txt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setPerformedAt(s+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //setDatePicking(holder.txt2);

    }


    public void setDatePicking(EditText since) {

        since.setFocusable(false);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                since.setText(sdf.format(myCalendar.getTime()));        }
            //mediModels.get(position).setStartedIn(s+"");

        };

        since.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(mContext,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();

                Calendar calendar = Calendar.getInstance();//get the current day
                datePicker.setMaxDate(calendar.getTimeInMillis());//set the current day as the max date or put yore date in miliseconds.
                //datePicker.setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


    }
    @Override
    public int getItemCount() {
        return mediModels.size();
    }


}
