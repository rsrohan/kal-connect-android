package com.kal.connect.models.healthseeker;

public class F1Model {
    /*{"LiveWith":"Parents","HealthConsultHope":"Well Being","SelfConnect":"Meditation","PatientID":40903,"ComplaintID":1519}*/

    String LiveWith, HealthConsultHope, SelfConnect, PatientID, ComplaintID;

    public F1Model(String liveWith, String healthConsultHope, String selfConnect, String patientID, String complaintID) {
        LiveWith = liveWith;
        HealthConsultHope = healthConsultHope;
        SelfConnect = selfConnect;
        PatientID = patientID;
        ComplaintID = complaintID;
    }

    public F1Model() {
    }

    public String getLiveWith() {
        return LiveWith;
    }

    public void setLiveWith(String liveWith) {
        LiveWith = liveWith;
    }

    public String getHealthConsultHope() {
        return HealthConsultHope;
    }

    public void setHealthConsultHope(String healthConsultHope) {
        HealthConsultHope = healthConsultHope;
    }

    public String getSelfConnect() {
        return SelfConnect;
    }

    public void setSelfConnect(String selfConnect) {
        SelfConnect = selfConnect;
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
