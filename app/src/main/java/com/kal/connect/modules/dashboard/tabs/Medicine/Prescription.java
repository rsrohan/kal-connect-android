package com.kal.connect.modules.dashboard.tabs.Medicine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.github.dhaval2404.imagepicker.ImagePicker;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Prescription extends CustomActivity {


    LinearLayout mLlCamera, mLlGallery, mLlPrescription, mLlHideDetails, mLlShowPrescription, mLlContinue;
    ImageView mImgHide;
    LinearLayout mHorizontalScrollView;
    LayoutInflater mLayoutInflater;
    ProgressBar mProgressBar;
    ArrayList<PrescriptionModel> mAlImage;
    ArrayList<String> mAlBase64;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_prescription);
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
                ImagePicker.Companion.with(Prescription.this)
                        .cameraOnly()            //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        mLlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Prescription.this)
                        .galleryOnly()            //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        mLlPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), MyPrescription.class);
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
                    Utilities.showAlert(Prescription.this, "Please select images", false);
                }
            }
        });
    }

    void uploadPrescription() {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParamBuyMedicine();


        inputParams.put("objMedicineList", new JSONArray(mAlBase64));

        SoapAPIManager apiManager = new SoapAPIManager(Prescription.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText")) {
                                Utilities.showAlert(Prescription.this, commonDataInfo.getString("RespText"), false);
                            } else {
                                Utilities.showAlert(Prescription.this, "Please check again!", false);
                            }
                            return;
                        }

                    }
                } catch (Exception e) {
                    e.getMessage();
                } finally {
                    Intent mIntent = new Intent(Prescription.this, Medicine.class);
                    startActivity(mIntent);
                    finish();
                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.EMAIL_MEDICINE_TO_PHARMACY, "POST"};

        if (Utilities.isNetworkAvailable(Prescription.this)) {
            apiManager.execute(url);
        } else {

        }
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

        setHeaderView(R.id.headerView, Prescription.this, "Upload Prescription");
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
                File file = FileUtil.from(Prescription.this, selectedImage);
                Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                AddImage(selectedImage, file.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Wrong image", Toast.LENGTH_SHORT).show();
        }
    }


    public void AddImage(final Uri mUri, final String MediaPath) {
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

        Glide.with(Prescription.this).load("file://" + MediaPath)
                .into(mImgView);

        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mVV = (View) mHorizontalScrollView.findViewWithTag(v.getTag().toString());
                mHorizontalScrollView.removeView(mVV);

            }
        });
    }


}
