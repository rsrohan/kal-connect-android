package com.kal.connect.modules.dashboard.tabs.Medicine;

import java.io.Serializable;

public class PrescriptionModel implements Serializable {
    private String mStrUrl;

    public String getmStrUrl() {
        return mStrUrl;
    }

    public void setmStrUrl(String mStrUrl) {
        this.mStrUrl = mStrUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false;
}
