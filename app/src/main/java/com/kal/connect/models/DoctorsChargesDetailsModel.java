package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorsChargesDetailsModel {

    @SerializedName("TotalCharges")
    @Expose
    private Double totalCharges;
    @SerializedName("SpecialistCharges")
    @Expose
    private Double specialistCharges;
    @SerializedName("SpecialistAccountNumber")
    @Expose
    private String specialistAccountNumber;
    @SerializedName("Medi360lCharges")
    @Expose
    private Double medi360lCharges;
    @SerializedName("Medi360AccountNumber")
    @Expose
    private String medi360AccountNumber;

    public Double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public Double getSpecialistCharges() {
        return specialistCharges;
    }

    public void setSpecialistCharges(Double specialistCharges) {
        this.specialistCharges = specialistCharges;
    }

    public String getSpecialistAccountNumber() {
        return specialistAccountNumber;
    }

    public void setSpecialistAccountNumber(String specialistAccountNumber) {
        this.specialistAccountNumber = specialistAccountNumber;
    }

    public Double getMedi360lCharges() {
        return medi360lCharges;
    }

    public void setMedi360lCharges(Double medi360lCharges) {
        this.medi360lCharges = medi360lCharges;
    }

    public String getMedi360AccountNumber() {
        return medi360AccountNumber;
    }

    public void setMedi360AccountNumber(String medi360AccountNumber) {
        this.medi360AccountNumber = medi360AccountNumber;
    }

}