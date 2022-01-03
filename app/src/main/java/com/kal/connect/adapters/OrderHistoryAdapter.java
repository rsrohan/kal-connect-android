package com.kal.connect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {


    private static final String TAG = "OrderHisAdapter";
    Context mContext;
    ArrayList<HashMap<String, Object>> items;


    public OrderHistoryAdapter(Context context, ArrayList<HashMap<String, Object>> partnerItems) {
        this.mContext = context;
        this.items = partnerItems;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_status, tv_date, tv_order_id, tv_amount, tv_desc, tv_pay_method;
        View v_status_clr;

        public ViewHolder(View view) {
            super(view);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_order_id = (TextView) view.findViewById(R.id.tv_order_id);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_pay_method = (TextView) view.findViewById(R.id.tv_pay_method);
            v_status_clr = view.findViewById(R.id.v_status_clr);

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false);

        return new ViewHolder(itemView);

    }

    /*
    *            0 - Order status not available
                 1 - Order Confirmed
                 2 - Payment received
                 3 - Order Shipped
                 4 - Order delivered
                 9 - Order Deleted
                 (5,6,7,8) reserved for future enhancements*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HashMap<String, Object> item = items.get(position);

        holder.tv_status.setText("Order Status: " + item.get("OrderStatusString").toString());
        holder.tv_date.setText("Date: " + item.get("OrderDate").toString());
        holder.tv_order_id.setText("Order ID: " + item.get("OrderCompleteID").toString());
        holder.tv_pay_method.setText("Payment Mode: " + item.get("PaymentMode").toString());
        holder.tv_desc.setText("Description: " + item.get("OrderDescription").toString());
        DecimalFormat precision = new DecimalFormat("0.00");
        try {
            holder.tv_amount.setText("Total Amount: Rs " + precision.format(Double.parseDouble(item.get("OrderTotalPrice").toString())));
        } catch (Exception e) {
            holder.tv_amount.setText("Total Amount: Rs " + item.get("OrderTotalPrice").toString());

        }

        String status = item.get("OrderStatus").toString();
        if (status.equals("0")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.gray_color));
        } else if (status.equals("1")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.appoinment_red));
        } else if (status.equals("2")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else if (status.equals("3")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        } else if (status.equals("4")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else if (status.equals("9")) {
            holder.v_status_clr.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}

