package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesModel {

    @SerializedName("ConsultationMode")
    @Expose
    private String consultationMode;
    @SerializedName("SpecialistID")
    @Expose
    private Integer specialistID;
    @SerializedName("patientID")
    @Expose
    private String patientID;
    @SerializedName("AppointmentTime")
    @Expose
    private String appointmentTime;
    @SerializedName("isTechnician")
    @Expose
    private String isTechnician;
    @SerializedName("ComplaintID")
    @Expose
    private String complaintID;
    @SerializedName("PatientName")
    @Expose
    private String patientName;
    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("SpecialistName")
    @Expose
    private String specialistName;

    public String getConsultationMode() {
        return consultationMode;
    }

    public void setConsultationMode(String consultationMode) {
        this.consultationMode = consultationMode;
    }

    public Integer getSpecialistID() {
        return specialistID;
    }

    public void setSpecialistID(Integer specialistID) {
        this.specialistID = specialistID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getIsTechnician() {
        return isTechnician;
    }

    public void setIsTechnician(String isTechnician) {
        this.isTechnician = isTechnician;
    }

    public String getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(String complaintID) {
        this.complaintID = complaintID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

}