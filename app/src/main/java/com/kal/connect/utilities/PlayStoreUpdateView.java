package com.kal.connect.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kal.connect.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class PlayStoreUpdateView {

    // MARK : UIElements
    private Context mainContext;
    private View view;

    // MARK: UIElements
    private TextView lblMessage, lblTitle, lblLine;
    private Button btnUpdate, btnCancel;
    LinearLayout mLlRoot;
    static int versionCode;
    static String currentVersion;
    static Activity mActivity;

    ImageView mImgGoogle;

    // MARK : Lifecycle
    public PlayStoreUpdateView(Context context, Boolean forceUpdate) {

        mainContext = context;
        view = LayoutInflater.from(context).inflate(
                R.layout.playstore_update_view, null);
        initialize();

    }

    // MARK : Instance Methods
    private void initialize() {
        mLlRoot = (LinearLayout) view.findViewById(R.id.ll_root);
        lblTitle = (TextView) view.findViewById(R.id.title);
        lblLine = (TextView) view.findViewById(R.id.txt_line);
        lblMessage = (TextView) view.findViewById(R.id.lblMessage);
        btnCancel = (Button) view.findViewById(R.id.playstore_cancel);
        btnUpdate = (Button) view.findViewById(R.id.playstore_update);
        mImgGoogle = (ImageView) view.findViewById(R.id.img_google);
        lblTitle.setVisibility(View.VISIBLE);
        mImgGoogle.setVisibility(View.VISIBLE);
        lblLine.setVisibility(View.VISIBLE);
        btnUpdate.setText("Update");
        mLlRoot.setPadding(40, 40, 40, 40);
    }

    private void showCurrentViewAsAlert() {

        final DialogPlus customDialog = DialogPlus.newDialog(mainContext)
                .setContentHolder(new ViewHolder(getUI()))
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {

                            case R.id.playstore_cancel:
                                dialog.dismiss();
                                break;

                            case R.id.playstore_update:
                                moveToPlayStore();
                                dialog.dismiss();
                                break;

                        }
                    }

                }).create();


        customDialog.show();

    }

    public void show() {
        showCurrentViewAsAlert();
    }

    public View getUI() {
        return view;
    }

    private void moveToPlayStore() {
        Activity mActivity = (Activity) mainContext;
        if (mActivity != null) {
            try {
                PackageInfo pInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
                final String appPackageName = mActivity.getPackageName();
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
                mActivity.startActivity(playStoreIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void versionCheck1(Activity activity) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);

            int versionCode;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = (int) pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                versionCode = pInfo.versionCode;
            }

            // Show Alert - Mandatory Update
            if (versionCode > Config.minVersionCode) {

                if (AppPreferences.getInstance().getVersionCode() == 0) {
                    AppPreferences.getInstance().setVersionCode(Config.minVersionCode + 1);

                    PlayStoreUpdateView playStoreUpdateView = new PlayStoreUpdateView(activity, true);
                    playStoreUpdateView.show();

                } else {
                    return;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void versionCheck(Activity activity) {
        mActivity = activity;

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = (int) pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                versionCode = pInfo.versionCode;
            }

            currentVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;

            new GetVersionCode().execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.kal.connect" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            try {
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (Integer.parseInt(currentVersion.replace(".", "")) < Integer.parseInt(onlineVersion.replace(".", ""))) {
                        PlayStoreUpdateView playStoreUpdateView = new PlayStoreUpdateView(mActivity, true);
                        playStoreUpdateView.show();
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

}
