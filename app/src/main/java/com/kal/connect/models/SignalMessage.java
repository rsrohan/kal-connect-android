package com.kal.connect.models;

public class SignalMessage {

    private String mMessageText;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    private Boolean remote;

    public SignalMessage(String messageText) {

        this.mMessageText = messageText;
        this.remote = false;
    }

    public SignalMessage(String messageText, Boolean remote, String userName) {

        this.mMessageText = messageText;
        this.remote = remote;
        this.userName = userName;
    }

    public String getMessageText() {
        return this.mMessageText;
    }

    public void setMessageText(String mMessageText) {
        this.mMessageText = mMessageText;
    }

    public Boolean isRemote() {
        return this.remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }

}