package com.kal.connect.models.healthseeker;

import java.util.Date;

public class PastIssuesModel {
    String DignosedID="0", Condition, FromDate, ControlStatus, TreatingBy, PatientID, ComplaintID;
    String DateString;
/*{"DignosedID":0,"Condition":"cynus","FromDate":"2019-04-10","ControlStatus":2,"TreatingBy":"Dr. Nehra (MD)","PatientID":40903,"ComplaintID":1519}*/
    public PastIssuesModel() {
    }

    public PastIssuesModel(String dignosedID, String condition, String fromDate, String controlStatus, String treatingBy, String patientID, String complaintID, String DateString) {
        this.DignosedID = dignosedID;
        this.Condition = condition;
        this.FromDate = fromDate;
        this.ControlStatus = controlStatus;
        this.TreatingBy = treatingBy;
        this.PatientID = patientID;
        this.ComplaintID = complaintID;
        this.DateString = DateString;
    }

    public String getDateString() {
        return DateString;
    }

    public void setDateString(String dateString) {
        DateString = dateString;
    }

    public String getDignosedID() {
        return DignosedID;
    }

    public void setDignosedID(String dignosedID) {
        DignosedID = dignosedID;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getControlStatus() {
        return ControlStatus;
    }

    public void setControlStatus(String controlStatus) {
        ControlStatus = controlStatus;
    }

    public String getTreatingBy() {
        return TreatingBy;
    }

    public void setTreatingBy(String treatingBy) {
        TreatingBy = treatingBy;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(String complaintID) {
        ComplaintID = complaintID;
    }
}
