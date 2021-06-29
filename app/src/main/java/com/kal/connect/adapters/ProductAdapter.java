package com.kal.connect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.modules.dashboard.BuyMedicine.MedicineActivity;
import com.kal.connect.modules.dashboard.BuyMedicine.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {


    Context mContext;
    ArrayList<ProductModel> mAlProduct;
    Activity mActivity;
    ArrayList<ProductModel> arraylist;
    ValueFilter valueFilter;
    TextView mTxtNoData;
    RecyclerView mLvLocal;

    public ProductAdapter(ArrayList<ProductModel> mAlProduct, Context context, RecyclerView mLvLocal, TextView mTxtNoData) {
        this.mAlProduct = mAlProduct;
        this.mContext = context;
        this.arraylist = mAlProduct;
        this.mTxtNoData = mTxtNoData;
        this.mLvLocal = mLvLocal;

        mActivity = (Activity) context;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    // Step 2: Create View Holder class to set the data for each cell
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblMedicineName, lblOfferAmount,mTxtOriginalAmt,mTxtAdd,mTxtOffer;
        ImageView mImgMedicine;

        public ViewHolder(View view) {
            super(view);
            mTxtOffer = (TextView) view.findViewById(R.id.txt_medicine_offer);
            lblMedicineName = (TextView) view.findViewById(R.id.txt_medicine_name);
            lblOfferAmount = (TextView) view.findViewById(R.id.txt_medicine_offer_amt);
            mTxtOriginalAmt = (TextView) view.findViewById(R.id.txt_medicine_amt);
            mTxtAdd = (TextView) view.findViewById(R.id.txt_add);
            mImgMedicine = (ImageView)view.findViewById(R.id.img_medicine);

            mTxtOriginalAmt.setPaintFlags(mTxtOriginalAmt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    // Step 3: Override Recyclerview methods to load the data one by one
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ProductAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        ProductModel mProductModel = mAlProduct.get(position);
        holder.lblMedicineName.setText(mProductModel.getMedicineName());
        holder.lblOfferAmount.setText(mProductModel.getDiscountedprice()+"");
        holder.mTxtOriginalAmt.setText(mProductModel.getOriginalprice()+"");
        if (!String.valueOf(mProductModel.getDiscount()).equalsIgnoreCase("0.0")) {
            holder.mTxtOffer.setVisibility(View.VISIBLE);
            holder.mTxtOffer.setText("  "+mProductModel.getDiscount()+"0% Off ");
            holder.mTxtOriginalAmt.setVisibility(View.VISIBLE);
        }else{
            holder.mTxtOffer.setVisibility(View.GONE);
            holder.mTxtOriginalAmt.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(mProductModel.getMedimage())
                .noFade()
                .into(holder.mImgMedicine);



        holder.mTxtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mActivity, MedicineActivity.class);
                mIntent.putExtra("Model",mProductModel);
                mActivity.setResult(mActivity.RESULT_OK, mIntent);
                mActivity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlProduct.size();
    }

    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<ProductModel> filterList = new ArrayList<ProductModel>();
                for (int i = 0; i < arraylist.size(); i++) {
                    if (arraylist.get(i).getMedicineName().toUpperCase()
                            .contains(constraint.toString().toUpperCase())) {
                        filterList.add(arraylist.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = arraylist.size();
                results.values = arraylist;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mAlProduct = (ArrayList<ProductModel>) results.values;

            if (mAlProduct.size() == 0) {
                mLvLocal.setVisibility(View.GONE);
                mTxtNoData.setVisibility(View.VISIBLE);
            } else {
                mLvLocal.setVisibility(View.VISIBLE);
                mTxtNoData.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }
    }

}
