package com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubElement {

    @SerializedName("SubElementID")
    @Expose
    private Integer subElementID;
    @SerializedName("ayurvedaModules")
    @Expose
    private List<AyurvedaModule> ayurvedaModules = null;

    public Integer getSubElementID() {
        return subElementID;
    }

    public void setSubElementID(Integer subElementID) {
        this.subElementID = subElementID;
    }

    public List<AyurvedaModule> getAyurvedaModules() {
        return ayurvedaModules;
    }

    public void setAyurvedaModules(List<AyurvedaModule> ayurvedaModules) {
        this.ayurvedaModules = ayurvedaModules;
    }

}