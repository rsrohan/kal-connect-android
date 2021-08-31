package com.kal.connect.models.healthseeker;

public class AboutSupplimentModel {

    /*{"SupplimentID":0,"Name":'LimC',"FromDate":'2021-02-22',"Dosage":'1-1-1',"Regular":true,"GivenBy":'Dr. Prasad(MBBS)',"PatientID":40903,"ComplaintID":1519}*/

    String SupplimentID="0", Name, FromDate, Dosage, GivenBy, Regular = "false", DateString;

    public AboutSupplimentModel() {
    }

    public AboutSupplimentModel(String supplimentID, String name, String fromDate, String dosage, String givenBy, String regular, String dateString) {
        SupplimentID = supplimentID;
        Name = name;
        FromDate = fromDate;
        Dosage = dosage;
        GivenBy = givenBy;
        Regular = regular;
        DateString = dateString;
    }

    public String getDateString() {
        return FromDate;
    }

    public void setDateString(String dateString) {
        FromDate = dateString;
    }

    public String getSupplimentID() {
        return SupplimentID;
    }

    public void setSupplimentID(String supplimentID) {
        SupplimentID = supplimentID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getDosage() {
        return Dosage;
    }

    public void setDosage(String dosage) {
        Dosage = dosage;
    }

    public String getGivenBy() {
        return GivenBy;
    }

    public void setGivenBy(String givenBy) {
        GivenBy = givenBy;
    }

    public String getRegular() {
        return Regular;
    }

    public void setRegular(String regular) {
        Regular = regular;
    }
}
