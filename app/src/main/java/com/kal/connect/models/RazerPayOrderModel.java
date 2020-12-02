package com.kal.connect.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RazerPayOrderModel {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payment_capture")
    @Expose
    private Integer paymentCapture;
    @SerializedName("transfers")
    @Expose
    private List<TransferModel> transferModels = null;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPaymentCapture() {
        return paymentCapture;
    }

    public void setPaymentCapture(Integer paymentCapture) {
        this.paymentCapture = paymentCapture;
    }

    public List<TransferModel> getTransferModels() {
        return transferModels;
    }

    public void setTransferModels(List<TransferModel> transferModels) {
        this.transferModels = transferModels;
    }

}