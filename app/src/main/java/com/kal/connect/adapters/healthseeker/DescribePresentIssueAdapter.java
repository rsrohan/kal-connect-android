package com.kal.connect.adapters.healthseeker;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.myCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.models.healthseeker.DescribeIssue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import lib.kingja.switchbutton.SwitchMultiButton;

public class DescribePresentIssueAdapter extends RecyclerView.Adapter<DescribePresentIssueAdapter.ViewHolder> {

    private static final String TAG = "HealthSeekerForm";
    Context mContext;
    ArrayList<DescribeIssue> issuesModels;


    public DescribePresentIssueAdapter(Context context, ArrayList<DescribeIssue> issuesModels) {
        this.mContext = context;
        this.issuesModels = issuesModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        EditText describe, since, treatment;
        SwitchMultiButton level;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            describe = view.findViewById(R.id.describe);
            since = view.findViewById(R.id.since);
            treatment = view.findViewById(R.id.attempted_treatment);
            level = view.findViewById(R.id.level);


        }
    }

    @Override
    public DescribePresentIssueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_describe_issue, parent, false);
        return new DescribePresentIssueAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DescribePresentIssueAdapter.ViewHolder holder, int position) {

        holder.tvTitle.setText(issuesModels.get(position).getProblemName());
        holder.level.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int pos, String tabText) {
                //Log.e(TAG, "onSwitch: "+position );
                issuesModels.get(position).setScale((position+1)+"");
            }
        });
        holder.describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setPerticular(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.since.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setFromDate(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.treatment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setTreatAndResponse(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //setDatePicking(holder.since);

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
        return issuesModels.size();
    }


}
