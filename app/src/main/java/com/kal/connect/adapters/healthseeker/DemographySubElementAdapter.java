package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.models.healthseeker.DemographyData;
import com.kal.connect.models.healthseeker.MasterData;

public class DemographySubElementAdapter extends RecyclerView.Adapter<DemographySubElementAdapter.ViewHolder> {

    Context mContext;
    DemographyData masterData;


    public DemographySubElementAdapter(Context context, DemographyData masterData) {
        this.mContext = context;
        this.masterData = masterData;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


         View rootView;
         TextView issueTxtVw;
         ImageView issueIconVw;
        private CardView cardBG;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            issueTxtVw = (TextView) view.findViewById(R.id.issue_txt_vw);
            cardBG = (CardView) view.findViewById(R.id.issue_card);
            issueIconVw = (ImageView) view.findViewById(R.id.issue_icon);
        }
    }

    @Override
    public DemographySubElementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.demo_item, parent, false);
        return new DemographySubElementAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DemographySubElementAdapter.ViewHolder holder, int position) {
       DemographyData.objSubElements dataBean = masterData.getObjSubElements().get(position);
       holder.issueTxtVw.setText(dataBean.getType());
       holder.rootView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               if (dataBean.isSelected()){
                   dataBean.setSelected(false);
               }else{
                   dataBean.setSelected(true);
                   for (int i =0;i<masterData.getObjSubElements().size();i++){
                       if (i==position){
                           masterData.getObjSubElements().get(i).setSelected(true);
                       }else{
                           masterData.getObjSubElements().get(i).setSelected(false);
                       }
                   }
               }

               notifyDataSetChanged();
           }
       });
       if (dataBean.isSelected()){
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_selected));
       }else{
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_not_selected));
       }

    }

    @Override
    public int getItemCount() {
        return masterData.getObjSubElements().size();
    }


}
