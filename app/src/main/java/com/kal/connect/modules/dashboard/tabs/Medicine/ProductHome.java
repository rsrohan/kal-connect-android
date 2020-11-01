package com.kal.connect.modules.dashboard.tabs.Medicine;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductHome extends CustomActivity implements SearchView.OnQueryTextListener {

    RecyclerView mRvMenu;
    CardView mCardMenu;
    ProductAdapter mAdapter;
    ArrayList<ProductModel> mAlProduct;
    EditText mEdtSearch;
    ImageView mImgClear;
    TextView mTxtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_home);
        initialization();
        buildUI();
        setAdapter();
        getProductList();
//        createSampleData();
    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRvMenu.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        mAdapter = new ProductAdapter(mAlProduct, ProductHome.this,mRvMenu,mTxtNoData);
        mRvMenu.setAdapter(mAdapter);
    }

    private void initialization() {
        mAlProduct = new ArrayList<>();
    }

    private void buildUI() {
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        mImgClear = (ImageView) findViewById(R.id.img_search);
        mEdtSearch = (EditText) findViewById(R.id.edt_search);

        mTxtNoData = (TextView) findViewById(R.id.txt_no_data);


        setHeaderView(R.id.headerView, ProductHome.this, ProductHome.this.getResources().getString(R.string.tab_home));
        headerView.showBackOption();

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    String text = mEdtSearch.getText().toString().trim();
                    if (text != null && !text.equals("")) {
                        mAdapter.getFilter().filter(text);
                    } else {
                        if (text != null) {
                            mAdapter.getFilter().filter(text);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mImgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdtSearch.setText("");
            }
        });


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void parsingData(JSONArray mJsonArray) {

//        String jsonStr = "[{'title':'New Celin 500 Tablet','amt':'34'},{'title':'Purayati Vitamin C','amt':'440'},{'title':'Good Health Package','amt':'440'},{'title':'Limcee Chewable Tablet','amt':'1038'},{'title':'Purayati Vitamin C','amt':'440'},{'title':'Good Health Package','amt':'440'},{'title':'Limcee Chewable Tablet','amt':'1038'},{'title':'Purayati Vitamin C','amt':'440'},{'title':'Good Health Package','amt':'440'},{'title':'Limcee Chewable Tablet','amt':'1038'}]";
        try {
            JSONArray jsonAry = mJsonArray;
            for (int i = 0; i < jsonAry.length(); i++) {
                JSONObject mJsonObject = jsonAry.getJSONObject(i);
                ProductModel mProductModel = new ProductModel();
                mProductModel.setMedicineName(mJsonObject.getString("MedicineName"));
                mProductModel.setOriginalprice(mJsonObject.getDouble("Originalprice"));
                mProductModel.setDiscountedprice(mJsonObject.getDouble("discountedprice"));
                mProductModel.setMedimage(mJsonObject.getString("medimage"));
                mProductModel.setMeddiscription(mJsonObject.getString("meddiscription"));
                mProductModel.setDiscount(mJsonObject.getDouble("discount"));
                mAlProduct.add(mProductModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            if (mAlProduct.size() > 0) {
                mTxtNoData.setVisibility(View.GONE);
                mRvMenu.setVisibility(View.VISIBLE);
            } else {
                mTxtNoData.setVisibility(View.VISIBLE);
                mRvMenu.setVisibility(View.GONE);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    public void getProductList() {
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();


        SoapAPIManager apiManager = new SoapAPIManager(ProductHome.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {

                        parsingData(responseAry);
                    }
                } catch (Exception e) {

                } finally {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_KAL_PRODUCT_LIST, "POST"};

        if (Utilities.isNetworkAvailable(ProductHome.this)) {
            apiManager.execute(url);
        } else {

        }
    }

}
