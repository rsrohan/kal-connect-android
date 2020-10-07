package com.kal.connect.customLibs.customMenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kal.connect.R;


/**
 * Created by lakeba_prabhu on 04/03/17.
 */
public class CustomMenu implements View.OnClickListener {

    //interfaces
    public MenuCallback menuCallback;

    //controls
    TextView lblTitle;
    ImageButton btnMenu, btnBack;
    Context mainContext;
    View view;
    String menuBGColor, menuTitleColor;

    /**
     * Create Custom menu and append the created menu inside the received parent layout
     * @param context
     * @param parentLayout
     */
    public CustomMenu(Context context, RelativeLayout parentLayout) {
        mainContext = context;
        view = LayoutInflater.from(context).inflate(
                R.layout.custom_menu_header, parentLayout, true);
        initialize();
    }

    /**
     * Create Custom menu without base layout - Call getUI() after creation of object with this Constructor
     * @param context
     */
    public CustomMenu(Context context) {
        mainContext = context;
        view = LayoutInflater.from(context).inflate(
                R.layout.custom_menu_header, null);
        initialize();
    }

    /**
     * Call this method after calling - public CustomMenu(Context context) - This method will return the ui
     * @return
     */
    public View getUI() {
        return view;
    }

    /**
     * Initialize required controls
     */
    public void initialize() {

        lblTitle = (TextView) view.findViewById(R.id.lblTitle);
        btnMenu = (ImageButton) view.findViewById(R.id.btnMenu);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnMenu.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        showMenuOption();

    }

    public void setDynamicHeader(String bgColor, String titleColor) {

        if(bgColor != null && titleColor != null) {
            menuBGColor = bgColor;
            menuTitleColor = titleColor;
            refreshUI();
        }

    }

    public void refreshUI(){

        if(menuBGColor != null && menuTitleColor != null) {

            view.setBackgroundColor(Color.parseColor(menuBGColor));
            lblTitle.setTextColor(Color.parseColor(menuTitleColor));
            btnMenu.getDrawable().setColorFilter(Color.parseColor(menuTitleColor), PorterDuff.Mode.SRC_ATOP);
            btnBack.getDrawable().setColorFilter(Color.parseColor(menuTitleColor), PorterDuff.Mode.SRC_ATOP);

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMenu:
                menuAction();
                break;
            case R.id.btnBack:
                backAction();
                break;
        }

    }


    public void menuAction() {
        menuCallback.menuButtonClicked();
    }

    public void backAction() {
        menuCallback.backButtonClicked();
    }

    public void setTitle(String menuTitle) {
        lblTitle.setText(menuTitle);
    }

    public void showMenuOption() {
        btnBack.setVisibility(View.GONE);
        btnMenu.setVisibility(View.VISIBLE);
    }

    public void showBackOption() {
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
    }

    /**
     * Interface to get event from the Menu and Back button
     */
    public interface MenuCallback {

        void menuButtonClicked();
        void backButtonClicked();

    }

}
