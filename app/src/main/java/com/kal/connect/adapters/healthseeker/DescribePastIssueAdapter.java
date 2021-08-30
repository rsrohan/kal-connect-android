package com.kal.connect.adapters.healthseeker;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.myCalendar;

import android.annotation.SuppressLint;
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
import com.kal.connect.models.healthseeker.PastIssuesModel;
import com.kal.connect.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DescribePastIssueAdapter extends RecyclerView.Adapter<DescribePastIssueAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PastIssuesModel> issuesModels;


    public DescribePastIssueAdapter(Context context, ArrayList<PastIssuesModel> pastIssues) {
        this.mContext = context;
        this.issuesModels = pastIssues;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText condition, since, status, physician;

        public ViewHolder(View view) {
            super(view);
            condition = view.findViewById(R.id.condition);
            since = view.findViewById(R.id.since);
            status = view.findViewById(R.id.status);
            physician = view.findViewById(R.id.physician);

        }
    }

    @Override
    public DescribePastIssueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_describe_issue_past, parent, false);
        return new DescribePastIssueAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DescribePastIssueAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.condition.setText(issuesModels.get(position).getCondition());

        holder.since.setText(issuesModels.get(position).getDateString());

        holder.status.setText(issuesModels.get(position).getControlStatus());
        holder.physician.setText(issuesModels.get(position).getTreatingBy());

        holder.condition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setCondition(s+"");
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
                issuesModels.get(position).setDateString(s+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setControlStatus(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.physician.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issuesModels.get(position).setTreatingBy(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //setDatePicking(holder.since);

    }

    @Override
    public int getItemCount() {
        return issuesModels.size();
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

}
