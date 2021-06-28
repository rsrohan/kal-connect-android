package com.kal.connect.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kal.connect.R;
import com.kal.connect.modules.dashboard.BuyMedicine.models.PrescriptionModel;

import java.util.ArrayList;

public class MyPrescriptionAdapter extends RecyclerView.Adapter<MyPrescriptionAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PrescriptionModel> mAlProduct;
    Activity mActivity;


    public MyPrescriptionAdapter(ArrayList<PrescriptionModel> mAlProduct, Context context) {
        this.mAlProduct = mAlProduct;
        this.mContext = context;
        mActivity = (Activity) context;
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mPrescriptionImage,mImgTick;
        RelativeLayout mRlRoot;

        public ViewHolder(View view) {
            super(view);
            mPrescriptionImage = (ImageView) view.findViewById(R.id.img_prescription);
            mImgTick = (ImageView) view.findViewById(R.id.img_tick);
            mRlRoot =(RelativeLayout)view.findViewById(R.id.rl_root);
        }
    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public MyPrescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myprescription_adapter, parent, false);
        return new MyPrescriptionAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyPrescriptionAdapter.ViewHolder holder, int position) {
        Glide.with(mActivity).load("file://" + mAlProduct.get(position).getmStrUrl())
                .into(holder.mPrescriptionImage);

        holder.mRlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlProduct.get(position).setSelected(!mAlProduct.get(position).isSelected());
                if (mAlProduct.get(position).isSelected()) {
//                    holder.mPrescriptionImage.setAlpha(0.5f);
                    holder.mImgTick.setVisibility(View.VISIBLE);
                }else{
//                    holder.mPrescriptionImage.setAlpha(1f);
                    holder.mImgTick.setVisibility(View.GONE);
                }
//                holder.mRlRoot.setBackgroundColor(mAlProduct.get(position).isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

//        holder.mRlRoot.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                return false;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mAlProduct.size();
    }


}
