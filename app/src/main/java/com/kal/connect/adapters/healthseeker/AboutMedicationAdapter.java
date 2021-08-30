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
import com.kal.connect.models.healthseeker.AboutMedicationModel;
import com.kal.connect.models.healthseeker.PastIssuesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AboutMedicationAdapter extends RecyclerView.Adapter<AboutMedicationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<AboutMedicationModel> mediModels;


    public AboutMedicationAdapter(Context context, ArrayList<AboutMedicationModel> mediModels) {
        this.mContext = context;
        this.mediModels = mediModels;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText mediName, startedIn, dosage, prescribedBy;

        public ViewHolder(View view) {
            super(view);
            mediName = view.findViewById(R.id.mediName);
            startedIn = view.findViewById(R.id.startedIn);
            dosage = view.findViewById(R.id.dosage);
            prescribedBy = view.findViewById(R.id.prescribedBy);

        }
    }

    @Override
    public AboutMedicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_describe_medication, parent, false);
        return new AboutMedicationAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AboutMedicationAdapter.ViewHolder holder, int position) {
        holder.mediName.setText(mediModels.get(position).getMedicineName());
        holder.startedIn.setText(mediModels.get(position).getDateString());
        holder.dosage.setText(mediModels.get(position).getDosage());
        holder.prescribedBy.setText(mediModels.get(position).getPrescribedBy());

        holder.mediName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setMedicineName(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.startedIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setStartedIn(s+"");
                mediModels.get(position).setDateString(s+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.dosage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setDosage(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.prescribedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediModels.get(position).setPrescribedBy(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //setDatePicking(holder.startedIn);

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
