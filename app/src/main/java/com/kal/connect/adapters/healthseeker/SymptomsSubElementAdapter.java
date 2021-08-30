package com.kal.connect.adapters.healthseeker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.kal.connect.R;
import com.kal.connect.models.healthseeker.MasterData;

import java.util.ArrayList;

public class SymptomsSubElementAdapter extends RecyclerView.Adapter<SymptomsSubElementAdapter.ViewHolder> {

    Context mContext;
    MasterData masterData;


    public SymptomsSubElementAdapter(Context context, MasterData masterData) {
        this.mContext = context;
        this.masterData = masterData;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


         View rootView;
         TextView issueTxtVw;
         ImageView issueIconVw;
         EditText textBox;
         TextInputLayout textBoxField;
        private CardView cardBG;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            issueTxtVw = (TextView) view.findViewById(R.id.issue_txt_vw);
            cardBG = (CardView) view.findViewById(R.id.issue_card);
            issueIconVw = (ImageView) view.findViewById(R.id.issue_icon);
            textBox = view.findViewById(R.id.textBox);
            textBoxField = view.findViewById(R.id.textBoxField);

        }
    }

    @Override
    public SymptomsSubElementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_item2, parent, false);
        return new SymptomsSubElementAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SymptomsSubElementAdapter.ViewHolder holder, int position) {
       MasterData.objSubElements dataBean = masterData.getObjSubElements().get(position);
       holder.issueTxtVw.setText(dataBean.getSubElement());
        dataBean.setCustomSubElement("");

        holder.rootView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (dataBean.isSelected()){
                   dataBean.setSelected(false);
               }else{
                   dataBean.setSelected(true);
               }
               notifyDataSetChanged();
           }
       });
       if (dataBean.isSelected()){
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_selected));
       }else{
           holder.cardBG.setCardBackgroundColor(mContext.getResources().getColor(R.color.issue_not_selected));
       }

       if (dataBean.getIsTextbox()!=null && dataBean.getIsTextbox().equals("1")){
           holder.textBoxField.setVisibility(View.VISIBLE);
           holder.textBox.setText(dataBean.getCustomSubElement());
       }else{
           holder.textBoxField.setVisibility(View.GONE);
       }

       holder.textBox.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               dataBean.setCustomSubElement(s+"");
               dataBean.setSelected(true);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });


    }

    @Override
    public int getItemCount() {
        return masterData.getObjSubElements().size();
    }


}
