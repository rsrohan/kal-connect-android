package com.kal.connect.modules.dashboard.BuyMedicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.adapters.MyPrescriptionAdapter;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.BuyMedicine.models.PrescriptionModel;

import java.util.ArrayList;

public class MyPrescriptionActivity extends CustomActivity {

    RecyclerView mRvPrescription;
    ArrayList<PrescriptionModel> mAlPrescription;
    CardView mCardSearch;
    TextView mTxtHomeProduct;
    MyPrescriptionAdapter mAdapter;
    Button mBtnDone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        initialize();
        buildUI();
        clickEvents();
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new MyPrescriptionAdapter(mAlPrescription, MyPrescriptionActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRvPrescription.setHasFixedSize(true);
        mRvPrescription.setLayoutManager(gridLayoutManager);
        mRvPrescription.setAdapter(mAdapter);
    }

    private void clickEvents() {
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Image", mAlPrescription);
                setResult(2, intent);
                finish();
            }
        });
    }

    private void buildUI() {

        setHeaderView(R.id.headerView, MyPrescriptionActivity.this, "Existing Prescription");
        headerView.showBackOption();

        mBtnDone = (Button) findViewById(R.id.btn_continue);
        mRvPrescription = (RecyclerView) findViewById(R.id.rv_menu);
        mCardSearch = (CardView) findViewById(R.id.carview_search);
        mTxtHomeProduct = (TextView) findViewById(R.id.txt_all_product);

        mCardSearch.setVisibility(View.GONE);
        mTxtHomeProduct.setVisibility(View.GONE);
        mBtnDone.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        mAlPrescription = new ArrayList<>();
        mAlPrescription = (ArrayList<PrescriptionModel>)getIntent().getSerializableExtra("Image");
    }

}
