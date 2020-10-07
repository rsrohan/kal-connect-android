package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel;

public class GetPatientPojo {
    private String PatExaminationID;

    public String getSystematicExam() {
        return SystematicExam;
    }

    public void setSystematicExam(String systematicExam) {
        SystematicExam = systematicExam;
    }

    private String SystematicExam;

    private String ModuleID;

    private String MasterElementTypeID;

    private String SpecialistID;

    private String PatientID;

    private String ComplaintID;

    private String UpdatedDateTime;

    public String getPatExaminationID() {
        return PatExaminationID;
    }

    public void setPatExaminationID(String PatExaminationID) {
        this.PatExaminationID = PatExaminationID;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String ModuleID) {
        this.ModuleID = ModuleID;
    }

    public String getMasterElementTypeID() {
        return MasterElementTypeID;
    }

    public void setMasterElementTypeID(String MasterElementTypeID) {
        this.MasterElementTypeID = MasterElementTypeID;
    }

    public String getSpecialistID() {
        return SpecialistID;
    }

    public void setSpecialistID(String SpecialistID) {
        this.SpecialistID = SpecialistID;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String PatientID) {
        this.PatientID = PatientID;
    }

    public String getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(String ComplaintID) {
        this.ComplaintID = ComplaintID;
    }

    public String getUpdatedDateTime() {
        return UpdatedDateTime;
    }

    public void setUpdatedDateTime(String UpdatedDateTime) {
        this.UpdatedDateTime = UpdatedDateTime;
    }

}
