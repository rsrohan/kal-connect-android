//package com.patientapp.modules.dashboard.tabs.Appointments.Tabs.Records;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.core.internal.view.SupportMenu;
//import androidx.recyclerview.widget.RecyclerView.Adapter;
//import com.keralaayurveda.C1291R;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class RecordsAdapterDD extends Adapter<ViewHolder> {
//    /* access modifiers changed from: private */
//    public static ItemClickListener itemClickListener;
//    ArrayList<HashMap<String, Object>> items;
//    Context mContext;
//
//    public interface ItemClickListener {
//        void onItemClick(int i, View view);
//    }
//
//    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements OnClickListener {
//        public Button btnView;
//        public TextView lblRecordName;
//
//        public ViewHolder(View view) {
//            super(view);
//            this.btnView = (Button) view.findViewById(C1291R.C1293id.btnView);
//            this.lblRecordName = (TextView) view.findViewById(C1291R.C1293id.lblRecordName);
//            this.itemView.setOnClickListener(this);
//            this.btnView.setOnClickListener(this);
//        }
//
//        public void onClick(View v) {
//            RecordsAdapter.itemClickListener.onItemClick(getAdapterPosition(), v);
//            RecordsAdapter.this.notifyDataSetChanged();
//        }
//    }
//
//    public RecordsAdapter(ArrayList<HashMap<String, Object>> partnerItems, Context context) {
//        this.items = partnerItems;
//        this.mContext = context;
//    }
//
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1291R.layout.record_item, parent, false));
//    }
//
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        HashMap<String, Object> item = (HashMap) this.items.get(position);
//        String str = "recordName";
//        holder.lblRecordName.setText(item.get(str) != null ? item.get(str).toString() : "");
//        if (!((Boolean) item.get("oldFile")).booleanValue()) {
//            holder.btnView.setText(C1291R.string.delete);
//            holder.btnView.setBackgroundColor(SupportMenu.CATEGORY_MASK);
//        }
//    }
//
//    public int getItemCount() {
//        return this.items.size();
//    }
//
//    public void setOnItemClickListener(ItemClickListener clickListener) {
//        itemClickListener = clickListener;
//    }
//}
