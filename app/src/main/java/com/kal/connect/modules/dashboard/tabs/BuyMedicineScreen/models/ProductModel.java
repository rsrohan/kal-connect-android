package com.kal.connect.modules.dashboard.tabs.BuyMedicineScreen.models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    String MedicineName,SKUNumber,meddiscription,medimage;
    Double Originalprice;
    Double discount;

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getSKUNumber() {
        return SKUNumber;
    }

    public void setSKUNumber(String SKUNumber) {
        this.SKUNumber = SKUNumber;
    }

    public String getMeddiscription() {
        return meddiscription;
    }

    public void setMeddiscription(String meddiscription) {
        this.meddiscription = meddiscription;
    }

    public String getMedimage() {
        return medimage;
    }

    public void setMedimage(String medimage) {
        this.medimage = medimage;
    }

    public Double getOriginalprice() {
        return Originalprice;
    }

    public void setOriginalprice(Double originalprice) {
        Originalprice = originalprice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountedprice() {
        return discountedprice;
    }

    public void setDiscountedprice(Double discountedprice) {
        this.discountedprice = discountedprice;
    }

    Double discountedprice;

}
