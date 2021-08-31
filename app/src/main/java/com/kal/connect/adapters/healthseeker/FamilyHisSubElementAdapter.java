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
import com.kal.connect.models.healthseeker.FamilyHistory;
import com.kal.connect.models.healthseeker.MasterData;

public class FamilyHisSubElementAdapter extends RecyclerView.Adapter<FamilyHisSubElementAdapter.ViewHolder> {

    Context mContext;
    FamilyHistory masterData;


    public FamilyHisSubElementAdapter(Context context, FamilyHistory masterData) {
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
    public FamilyHisSubElementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_item2, parent, false);
        return new FamilyHisSubElementAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(FamilyHisSubElementAdapter.ViewHolder holder, int position) {
       FamilyHistory.objRelations dataBean = masterData.getObjRelations().get(position);
       holder.issueTxtVw.setText(dataBean.getRelationshipName());
       holder.rootView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (dataBean.isSelected()){
                   dataBean.setSelected(false);
                   if (dataBean.getRelationshipName().equals("Father")){
                       masterData.setFather(false);
                   } else if (dataBean.getRelationshipName().equals("Mother")){
                       masterData.setMother(false);
                   } else if (dataBean.getRelationshipName().equals("Brother")){
                       masterData.setBrother(false);
                   } else if (dataBean.getRelationshipName().equals("Sister")){
                       masterData.setSister(false);
                   } else if (dataBean.getRelationshipName().equals("PGM")){
                       masterData.setPgm(false);
                   } else if (dataBean.getRelationshipName().equals("PGF")){
                       masterData.setPgf(false);
                   } else if (dataBean.getRelationshipName().equals("MGM")){
                       masterData.setMgm(false);
                   } else if (dataBean.getRelationshipName().equals("MGF")){
                       masterData.setMgf(false);
                   }
               }else{
                   dataBean.setSelected(true);
                   if (dataBean.getRelationshipName().equals("Father")){
                       masterData.setFather(true);
                   } else if (dataBean.getRelationshipName().equals("Mother")){
                       masterData.setMother(true);
                   } else if (dataBean.getRelationshipName().equals("Brother")){
                       masterData.setBrother(true);
                   } else if (dataBean.getRelationshipName().equals("Sister")){
                       masterData.setSister(true);
                   } else if (dataBean.getRelationshipName().equals("PGM")){
                       masterData.setPgm(true);
                   } else if (dataBean.getRelationshipName().equals("PGF")){
                       masterData.setPgf(true);
                   } else if (dataBean.getRelationshipName().equals("MGM")){
                       masterData.setMgm(true);
                   } else if (dataBean.getRelationshipName().equals("MGF")){
                       masterData.setMgf(true);
                   }
               }
               notifyDataSetChanged();
           }
       });
       if (masterData.isFather() && dataBean.getRelationshipName().equals("Father")){
           dataBean.setSelected(true);
       }
       if (masterData.isMother() && dataBean.getRelationshipName().equals("Mother")){
           dataBean.setSelected(true);
       }
       if (masterData.isBrother() && dataBean.getRelationshipName().equals("Brother")){
           dataBean.setSelected(true);
       }
       if (masterData.isSister() && dataBean.getRelationshipName().equals("Sister")){
           dataBean.setSelected(true);
       }
       if (masterData.isPgm() && dataBean.getRelationshipName().equals("PGM")){
           dataBean.setSelected(true);
       }
       if (masterData.isPgf() && dataBean.getRelationshipName().equals("PGF")){
           dataBean.setSelected(true);
       }
       if (masterData.isMgm() && dataBean.getRelationshipName().equals("MGM")){
           dataBean.setSelected(true);
       }
       if (masterData.isMgf() && dataBean.getRelationshipName().equals("MGF")){
           dataBean.setSelected(true);
       }

       if (dataBean.isSelected()){
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_selected));
       }else{
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_not_selected));
       }

    }

    @Override
    public int getItemCount() {
        return masterData.getObjRelations().size();
    }


}
