package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterElement {

    @SerializedName("MasterElementID")
    @Expose
    private Integer masterElementID;
    @SerializedName("MasterElementType")
    @Expose
    private Integer masterElementType;
    @SerializedName("subElements")
    @Expose
    private List<SubElement> subElements = null;

    public Integer getMasterElementID() {
        return masterElementID;
    }

    public void setMasterElementID(Integer masterElementID) {
        this.masterElementID = masterElementID;
    }

    public Integer getMasterElementType() {
        return masterElementType;
    }

    public void setMasterElementType(Integer masterElementType) {
        this.masterElementType = masterElementType;
    }

    public List<SubElement> getSubElements() {
        return subElements;
    }

    public void setSubElements(List<SubElement> subElements) {
        this.subElements = subElements;
    }

}