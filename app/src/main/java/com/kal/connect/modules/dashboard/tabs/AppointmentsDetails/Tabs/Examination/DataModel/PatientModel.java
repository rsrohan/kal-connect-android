package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel;

import java.util.ArrayList;

public class PatientModel
{

    public String getCommonHeader() {
        return commonHeader;
    }

    public void setCommonHeader(String commonHeader) {
        this.commonHeader = commonHeader;
    }

    String commonHeader;
    ArrayList<Items> mAlSubItems;

    public ArrayList<Items> getmAlSubItems() {
        return mAlSubItems;
    }

    public void setmAlSubItems(ArrayList<Items> mAlSubItems) {
        this.mAlSubItems = mAlSubItems;
    }
}