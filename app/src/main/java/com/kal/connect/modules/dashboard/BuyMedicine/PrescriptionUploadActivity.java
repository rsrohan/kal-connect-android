package com.kal.connect.modules.dashboard.BuyMedicine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.kal.connect.R;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.modules.dashboard.BuyMedicine.models.FileUtil;
import com.kal.connect.modules.dashboard.BuyMedicine.models.PrescriptionModel;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrescriptionUploadActivity extends CustomActivity {


    private static final String TAG = "PrescriptionUploadAct";
    LinearLayout mLlCamera, mLlGallery, mLlPrescription, mLlHideDetails, mLlShowPrescription, mLlContinue;
    ImageView mImgHide;
    LinearLayout mHorizontalScrollView;
    LayoutInflater mLayoutInflater;
    ProgressBar mProgressBar;
    ArrayList<PrescriptionModel> mAlImage;
    public static ArrayList<String> mAlBase64;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_upload);
        intializeViews();
        buildUI();
        clickEvents();
    }

    private void intializeViews() {
        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAlImage = new ArrayList<>();
        mAlBase64 = new ArrayList<>();
    }


    private void clickEvents() {

        mLlHideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLlShowPrescription.getVisibility() == View.VISIBLE) {
                    mImgHide.setImageResource(R.drawable.ic_up_arrow);
                    mLlShowPrescription.setVisibility(View.GONE);
                } else {
                    mImgHide.setImageResource(R.drawable.ic_down_arrow);
                    mLlShowPrescription.setVisibility(View.VISIBLE);
                }
            }
        });

        mLlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(PrescriptionUploadActivity.this)
                        .cameraOnly()            //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        mLlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(PrescriptionUploadActivity.this)
                        .galleryOnly()            //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        mLlPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), MyPrescriptionActivity.class);
                mIntent.putExtra("Image", mAlImage);
                startActivityForResult(mIntent, 2);

            }
        });

        mLlContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlBase64.clear();
                if (mAlImage != null && mAlImage.size() > 0) {
                    for (int i = 0; i < mAlImage.size(); i++) {
                        PrescriptionModel mPrescriptionModel = mAlImage.get(i);
                        mAlBase64.add(convertBase64(mPrescriptionModel.getmStrUrl()));
                    }
                    uploadPrescription();
                }else{
                    Utilities.showAlert(PrescriptionUploadActivity.this, "Please select images", false);
                }
            }
        });
    }

    void uploadPrescription() {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();

        inputParams.put("uploadedFiles", new JSONArray(mAlBase64));
        Log.e(TAG, "uploadPrescription: "+inputParams.get("uploadedFiles") );

        Bundle bundle = new Bundle();
        bundle.putBoolean("uploadedFiles", true);

        Intent i = new Intent(getApplicationContext(), MedicineActivity.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();

//        SoapAPIManager apiManager = new SoapAPIManager(PrescriptionUploadActivity.this, inputParams, new APICallback() {
//            @Override
//            public void responseCallback(Context context, String response) throws JSONException {
//                Log.e("***response***", response);
//
//                try {
//                    JSONArray responseAry = new JSONArray(response);
//                    if (responseAry.length() > 0) {
//                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
//                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
//                            if (commonDataInfo.has("RespText")) {
//                                Utilities.showAlert(PrescriptionUploadActivity.this, commonDataInfo.getString("RespText"), false);
//                                Toast.makeText(context, commonDataInfo.getString("RespText"), Toast.LENGTH_SHORT).show();
//                                Intent mIntent = new Intent(PrescriptionUploadActivity.this, MedicineActivity.class);
//                                startActivity(mIntent);
//                                finish();
//                            } else {
//                                Log.e(TAG, "responseCallback: do not has response" );
//                                Utilities.showAlert(PrescriptionUploadActivity.this, "Please check again!", false);
//                            }
//                        }else{
//                            Utilities.showAlert(PrescriptionUploadActivity.this, "Error Occurred", false);
//
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.getMessage();
//                    Log.e(TAG, "responseCallback: "+e );
//                }
//            }
//        }, true);
//        String[] url = {Config.WEB_Services1, Config.UPLOAD_FILES, "POST"};
//
//        if (Utilities.isNetworkAvailable(PrescriptionUploadActivity.this)) {
//            apiManager.execute(url);
//        } else {
//            Utilities.showAlert(PrescriptionUploadActivity.this, "Please check internet!", false);
//        }
    }

    private void buildUI() {

        mImgHide = (ImageView) findViewById(R.id.img_up_arrow);
        mLlCamera = (LinearLayout) findViewById(R.id.ll_camera);
        mLlGallery = (LinearLayout) findViewById(R.id.ll_gallery);
        mLlPrescription = (LinearLayout) findViewById(R.id.ll_root_prescription);
        mHorizontalScrollView = (LinearLayout) findViewById(R.id.horizontal);
        mLlHideDetails = (LinearLayout) findViewById(R.id.ll_prescription);
        mLlShowPrescription = (LinearLayout) findViewById(R.id.ll_show_prescription);
        mLlContinue = (LinearLayout) findViewById(R.id.ll_continue);

        setHeaderView(R.id.headerView, PrescriptionUploadActivity.this, "Upload Prescription");
        headerView.showBackOption();
    }


    String convertBase64(String filePath) {
        String base64Image = "";
        File imgFile = new File(filePath);
        if (imgFile.exists() && imgFile.length() > 0) {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
            return base64Image;
        }
        return base64Image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                File file = FileUtil.from(PrescriptionUploadActivity.this, selectedImage);
                Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                AddImage(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Wrong image", Toast.LENGTH_SHORT).show();
        }
    }


    public void AddImage(final String MediaPath) {
        final View mView = mLayoutInflater.inflate(R.layout.app_common_media, null, false);
        ImageView mImgView = (ImageView) mView.findViewById(R.id.img_thumb);
        ImageView mImgDelete = (ImageView) mView.findViewById(R.id.img_delete);
        RelativeLayout mRlImage = (RelativeLayout) mView.findViewById(R.id.rl_root);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_loading);
        mHorizontalScrollView.addView(mView);

        mView.setTag(MediaPath);
        mImgDelete.setTag(MediaPath);

        PrescriptionModel mPrescriptionModel = new PrescriptionModel();
        mPrescriptionModel.setmStrUrl(MediaPath);
        mPrescriptionModel.setSelected(false);
        mAlImage.add(mPrescriptionModel);

        Glide.with(PrescriptionUploadActivity.this).load("file://" + MediaPath)
                .into(mImgView);

        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mVV = (View) mHorizontalScrollView.findViewWithTag(v.getTag().toString());
                mHorizontalScrollView.removeView(mVV);

            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), MedicineActivity.class));
        finish();

    }
}
