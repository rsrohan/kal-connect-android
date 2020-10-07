package com.kal.connect.customLibs.noInternetAlert;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.kal.connect.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;


public class NoInternetAlert {

    // MARK : UIElements
    Context mainContext;
    View view;
    Button btnSettings;
    DialogPlus customDialog;

    // MARK : Lifecycle
    public NoInternetAlert(Context context) {

        mainContext = context;
        view = LayoutInflater.from(context).inflate(
                R.layout.no_internet, null);
        initialize();

    }

    public void show() {
        showCurrentViewAsAlert();
    }


    public View getUI() {
        return view;
    }

    // MARK : Instance Methods
    private void initialize() {

        btnSettings = (Button) view.findViewById(R.id.btnSettings);

    }

    private void showCurrentViewAsAlert() {

        customDialog = DialogPlus.newDialog(mainContext)
                .setContentHolder(new ViewHolder(getUI()))
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .setMargin(0, 0, 0, 0)
                .setMargin(0, 0, 0, 0)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {

                            case R.id.btnSettings:
                                mainContext.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                                break;

                        }
                    }

                }).create();

        customDialog.show();

    }

    public void hide(){
        if(customDialog != null){
            customDialog.dismiss();
        }
    }

}

