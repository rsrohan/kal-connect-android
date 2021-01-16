package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientExaminationData {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("masterElements")
    @Expose
    private List<MasterElement> masterElements = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<MasterElement> getMasterElements() {
        return masterElements;
    }

    public void setMasterElements(List<MasterElement> masterElements) {
        this.masterElements = masterElements;
    }

}