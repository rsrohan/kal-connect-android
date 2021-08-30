package com.kal.connect.models.healthseeker;

public class HospitalizationModel {
    /*[{"HospitalizationID":0,"Condition":"Passed","Year":"2016","Method":"Admit","PatientID":40903,"ComplaintID":1519}*/
    String HospitalizationID="0", Condition, Year, Method;

    public HospitalizationModel(String hospitalizationID, String condition, String year, String method) {
        HospitalizationID = hospitalizationID;
        Condition = condition;
        Year = year;
        Method = method;
    }

    public HospitalizationModel() {
    }



    public String getHospitalizationID() {
        return HospitalizationID;
    }

    public void setHospitalizationID(String hospitalizationID) {
        HospitalizationID = hospitalizationID;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }
}
