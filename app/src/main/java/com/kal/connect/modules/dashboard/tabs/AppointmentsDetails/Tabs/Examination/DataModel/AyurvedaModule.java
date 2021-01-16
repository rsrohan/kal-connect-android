package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AyurvedaModule {

    @SerializedName("ModuleID")
    @Expose
    private Integer moduleID;
    @SerializedName("Module")
    @Expose
    private String module;
    @SerializedName("SubElementID")
    @Expose
    private Integer subElementID;
    @SerializedName("MasterElementID")
    @Expose
    private Integer masterElementID;
    @SerializedName("SubElement")
    @Expose
    private String subElement;
    @SerializedName("MasterElement")
    @Expose
    private String masterElement;
    @SerializedName("MasterElementType")
    @Expose
    private Integer masterElementType;

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    Boolean isCheck;

    public Integer getModuleID() {
        return moduleID;
    }

    public void setModuleID(Integer moduleID) {
        this.moduleID = moduleID;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getSubElementID() {
        return subElementID;
    }

    public void setSubElementID(Integer subElementID) {
        this.subElementID = subElementID;
    }

    public Integer getMasterElementID() {
        return masterElementID;
    }

    public void setMasterElementID(Integer masterElementID) {
        this.masterElementID = masterElementID;
    }

    public String getSubElement() {
        return subElement;
    }

    public void setSubElement(String subElement) {
        this.subElement = subElement;
    }

    public String getMasterElement() {
        return masterElement;
    }

    public void setMasterElement(String masterElement) {
        this.masterElement = masterElement;
    }

    public Integer getMasterElementType() {
        return masterElementType;
    }

    public void setMasterElementType(Integer masterElementType) {
        this.masterElementType = masterElementType;
    }

}
