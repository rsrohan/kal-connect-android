package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChargesDetails {

    @SerializedName("TotalCharges")
    @Expose
    private Integer totalCharges;
    @SerializedName("HospitalCharges")
    @Expose
    private Integer hospitalCharges;
    @SerializedName("HospitalAccountNumber")
    @Expose
    private String hospitalAccountNumber;
    @SerializedName("Medi360lCharges")
    @Expose
    private String medi360lCharges;
    @SerializedName("Medi360AccountNumber")
    @Expose
    private String medi360AccountNumber;

    public Integer getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Integer totalCharges) {
        this.totalCharges = totalCharges;
    }

    public Integer getHospitalCharges() {
        return hospitalCharges;
    }

    public void setHospitalCharges(Integer hospitalCharges) {
        this.hospitalCharges = hospitalCharges;
    }

    public String getHospitalAccountNumber() {
        return hospitalAccountNumber;
    }

    public void setHospitalAccountNumber(String hospitalAccountNumber) {
        this.hospitalAccountNumber = hospitalAccountNumber;
    }

    public String getMedi360lCharges() {
        return medi360lCharges;
    }

    public void setMedi360lCharges(String medi360lCharges) {
        this.medi360lCharges = medi360lCharges;
    }

    public String getMedi360AccountNumber() {
        return medi360AccountNumber;
    }

    public void setMedi360AccountNumber(String medi360AccountNumber) {
        this.medi360AccountNumber = medi360AccountNumber;
    }

}