package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjAvalTime {

    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Available")
    @Expose
    private Boolean available;

    @SerializedName("IsBooked")
    @Expose
    private Boolean isBooked;

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }
//

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}