package com.kal.connect.models;


public class AppointmentScheduleTimer{

    String time;
    Boolean isAvailable;

    public String getTime() {
        return time;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setTime(String time) {
        this.time = time;

    }

    public AppointmentScheduleTimer(String time, Boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
    }
}