package com.kal.connect.customLibs.appCustomization;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
//import androidx.core.widget.DrawerLayoutLayout;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.kal.connect.customLibs.customMenu.CustomMenu;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import androidx.drawerlayout.widget.DrawerLayout;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class CustomActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public CustomMenu headerView;

    // Re-Render menu Colors
    @Override
    protected void onResume() {
        super.onResume();

        if (headerView != null) {
            headerView.refreshUI();
        }

    }

    /**
     * Build Menu bar
     */
    public void setHeaderView(int headerLayoutId, final Activity fromActivity, String headerTitle) {

        RelativeLayout menuLayout = (RelativeLayout) findViewById(headerLayoutId);
        headerView = new CustomMenu(fromActivity, menuLayout);
        headerView.setTitle(headerTitle);

        //Here you can perform the operations
        headerView.menuCallback = new CustomMenu.MenuCallback() {

            @Override
            public void menuButtonClicked() {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }

            @Override
            public void backButtonClicked() {

                if (Config.isChat) {
                    if (Config.isBack) {
                        Intent mIntent = new Intent(getApplicationContext(), DashboardMapActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        fromActivity.finish();
                        Utilities.popAnimation(fromActivity);
                    }
                    Config.isChat = false;
                } else {
                    fromActivity.finish();
                    Utilities.popAnimation(fromActivity);
                }


            }

        };

    }

    /**
     * To Set title from anywhere
     *
     * @param title
     */
    public void setHeaderTitle(String title) {
        headerView.setTitle(title);
    }


    /**
     * Custom font initialization
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.popAnimation(CustomActivity.this);
    }


    /**
     *
     // MARK : Properties

     // MARK : Lifecycle
     @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.otp_verification);
     buildUI();
     }

     // MARK : UIActions
     @Override public void onClick(View v) {
     switch (v.getId()) {

     }
     }

     // MARK : Instance Methods
     private void buildUI() {

     }

     */

}
