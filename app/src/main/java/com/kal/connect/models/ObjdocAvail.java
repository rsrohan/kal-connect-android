package com.kal.connect.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjdocAvail {

    @SerializedName("AvailableDate")
    @Expose
    private String availableDate;
    @SerializedName("objAvalTimes")
    @Expose
    private List<ObjAvalTime> objAvalTimes = null;

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public List<ObjAvalTime> getObjAvalTimes() {
        return objAvalTimes!=null? objAvalTimes: new ArrayList<ObjAvalTime>();
    }

    public void setObjAvalTimes(List<ObjAvalTime> objAvalTimes) {
        this.objAvalTimes = objAvalTimes;
    }

}