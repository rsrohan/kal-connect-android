package com.kal.connect.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferModel {

    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("notes")
    @Expose
    private NotesModel notesModel;
    @SerializedName("linked_account_notes")
    @Expose
    private List<String> linkedAccountNotes = null;
    @SerializedName("on_hold")
    @Expose
    private Integer onHold;
    @SerializedName("on_hold_until")
    @Expose
    private Integer onHoldUntil;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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

    public NotesModel getNotesModel() {
        return notesModel;
    }

    public void setNotesModel(NotesModel notesModel) {
        this.notesModel = notesModel;
    }

    public List<String> getLinkedAccountNotes() {
        return linkedAccountNotes;
    }

    public void setLinkedAccountNotes(List<String> linkedAccountNotes) {
        this.linkedAccountNotes = linkedAccountNotes;
    }

    public Integer getOnHold() {
        return onHold;
    }

    public void setOnHold(Integer onHold) {
        this.onHold = onHold;
    }

    public Integer getOnHoldUntil() {
        return onHoldUntil;
    }

    public void setOnHoldUntil(Integer onHoldUntil) {
        this.onHoldUntil = onHoldUntil;
    }

}