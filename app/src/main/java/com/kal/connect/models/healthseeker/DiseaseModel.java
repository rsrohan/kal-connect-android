package com.kal.connect.models.healthseeker;

public class DiseaseModel {
    /*{"PastDiseaseID":0,"Disease":"Covid","Period":"14","Treatment":"Home isolation","PatientID":40903,"ComplaintID":1519}*/
    String PastDiseaseID="0", Disease, Period, Treatment;

    public DiseaseModel(String pastDiseaseID, String disease, String period, String treatment) {
        PastDiseaseID = pastDiseaseID;
        Disease = disease;
        Period = period;
        Treatment = treatment;
    }

    public DiseaseModel() {
    }

    public String getPastDiseaseID() {
        return PastDiseaseID;
    }

    public void setPastDiseaseID(String pastDiseaseID) {
        PastDiseaseID = pastDiseaseID;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getTreatment() {
        return Treatment;
    }

    public void setTreatment(String treatment) {
        Treatment = treatment;
    }
}
