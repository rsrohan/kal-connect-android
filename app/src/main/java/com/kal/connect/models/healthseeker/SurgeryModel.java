package com.kal.connect.models.healthseeker;

public class SurgeryModel {

    /*{"SurgeryID":0,"Method":"Hernia","Period":"2017-04-20","PerformedAt":"Somani Hospital","PatientID":40903,"ComplaintID":1519}*/
    String SurgeryID="0", Method, Period, PerformedAt, DateString;

    public SurgeryModel(String surgeryID, String method, String period, String performedAt, String dateString) {
        SurgeryID = surgeryID;
        Method = method;
        Period = period;
        PerformedAt = performedAt;
        DateString = dateString;
    }

    public SurgeryModel() {
    }

    public String getDateString() {
        return Period;
    }

    public void setDateString(String dateString) {
        Period = dateString;
    }

    public String getSurgeryID() {
        return SurgeryID;
    }

    public void setSurgeryID(String surgeryID) {
        SurgeryID = surgeryID;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getPerformedAt() {
        return PerformedAt;
    }

    public void setPerformedAt(String performedAt) {
        PerformedAt = performedAt;
    }
}
