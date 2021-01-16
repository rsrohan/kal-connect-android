package com.kal.connect.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjdocAvailModel {

    @SerializedName("AvailableDate")
    @Expose
    private String availableDate;
    @SerializedName("objAvalTimes")
    @Expose
    private List<ObjAvalTimeModel> objAvalTimeModels = null;

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public List<ObjAvalTimeModel> getObjAvalTimeModels() {
        return objAvalTimeModels !=null? objAvalTimeModels : new ArrayList<ObjAvalTimeModel>();
    }

    public void setObjAvalTimeModels(List<ObjAvalTimeModel> objAvalTimeModels) {
        this.objAvalTimeModels = objAvalTimeModels;
    }

}