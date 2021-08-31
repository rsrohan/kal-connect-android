package com.kal.connect.models.healthseeker;

public class DescribeIssue {

    /*[{"MainProblemID":3,"Perticular":"Heavy Breathing issue","FromDate":"2019-02-02","Scale":2,"TreatAndResponse":"Using breathing pump","PatientID":40903,"ComplaintID":1519},
    {"MainProblemID":4,"Perticular":"Soar Throt issue","FromDate":"2020-08-08","Scale":1,"TreatAndResponse":"Using ayurvadic toffees","PatientID":40903,"ComplaintID":1519},
    {"MainProblemID":0,"Perticular":"Hearing issue","FromDate":"2021-05-07","Scale":3,"TreatAndResponse":"Hearing aids","PatientID":40903,"ComplaintID":1519}]*/


    String ProblemName, MainProblemID="0", Perticular, FromDate, Scale, TreatAndResponse, PatientID, ComplaintID;


    public DescribeIssue() {
    }

    public DescribeIssue(String problemName, String mainProblemID, String perticular, String fromDate, String scale, String treatAndResponse, String patientID, String complaintID) {
        ProblemName = problemName;
        MainProblemID = mainProblemID;
        Perticular = perticular;
        FromDate = fromDate;
        Scale = scale;
        TreatAndResponse = treatAndResponse;
        PatientID = patientID;
        ComplaintID = complaintID;
    }

    public String getProblemName() {
        return ProblemName;
    }

    public void setProblemName(String problemName) {
        ProblemName = problemName;
    }

    public String getMainProblemID() {
        return MainProblemID;
    }

    public void setMainProblemID(String mainProblemID) {
        MainProblemID = mainProblemID;
    }

    public String getPerticular() {
        return Perticular;
    }

    public void setPerticular(String perticular) {
        Perticular = perticular;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getTreatAndResponse() {
        return TreatAndResponse;
    }

    public void setTreatAndResponse(String treatAndResponse) {
        TreatAndResponse = treatAndResponse;
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
