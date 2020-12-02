package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel;

import java.util.ArrayList;

public class Items {
    String itemItitle;

    public String getItemItitle() {
        return itemItitle;
    }

    public void setItemItitle(String itemItitle) {
        this.itemItitle = itemItitle;
    }

    public ArrayList<SubItems> getmAlItems() {
        return mAlItems;
    }

    public void setmAlItems(ArrayList<SubItems> mAlItems) {
        this.mAlItems = mAlItems;
    }

    ArrayList<SubItems> mAlItems;
}
