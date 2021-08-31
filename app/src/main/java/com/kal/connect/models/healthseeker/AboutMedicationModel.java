package com.kal.connect.models.healthseeker;

public class AboutMedicationModel {

    /*{"PrescribedMedicineID":0,"MedicineName":"Antigrain","StartedIn":"2019-06-08","Dosage":"1-0-1","IsAlternative":false,"PrescribedBy":"Dr. Harshvardhan","PatientID":40903,"ComplaintID":1519}*/

    String PrescribedMedicineID="0", MedicineName, StartedIn, Dosage, PrescribedBy,  DateString;
    boolean IsAlternative;

    public AboutMedicationModel() {
    }

    public AboutMedicationModel(String prescribedMedicineID, String medicineName, String startedIn, String dosage, String prescribedBy, String dateString, boolean isAlternative) {
        PrescribedMedicineID = prescribedMedicineID;
        MedicineName = medicineName;
        StartedIn = startedIn;
        Dosage = dosage;
        PrescribedBy = prescribedBy;
        DateString = dateString;
        IsAlternative = isAlternative;
    }

    public String getDateString() {
        return StartedIn;
    }

    public void setDateString(String dateString) {
        StartedIn = dateString;
    }

    public String getPrescribedMedicineID() {
        return PrescribedMedicineID;
    }

    public void setPrescribedMedicineID(String prescribedMedicineID) {
        PrescribedMedicineID = prescribedMedicineID;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getStartedIn() {
        return StartedIn;
    }

    public void setStartedIn(String startedIn) {
        StartedIn = startedIn;
    }

    public String getDosage() {
        return Dosage;
    }

    public void setDosage(String dosage) {
        Dosage = dosage;
    }

    public String getPrescribedBy() {
        return PrescribedBy;
    }

    public void setPrescribedBy(String prescribedBy) {
        PrescribedBy = prescribedBy;
    }

    public boolean isAlternative() {
        return IsAlternative;
    }

    public void setAlternative(boolean alternative) {
        IsAlternative = alternative;
    }
}
