package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HospitalModel {

    @SerializedName("HospitalID")
    @Expose
    private Integer hospitalID;
    @SerializedName("HospitalName")
    @Expose
    private String hospitalName;
    @SerializedName("HospitalDescription")
    @Expose
    private String hospitalDescription;
    @SerializedName("HospitalLat")
    @Expose
    private String hospitalLat;
    @SerializedName("HospitalLong")
    @Expose
    private String hospitalLong;
    @SerializedName("DiagnosticName")
    @Expose
    private String diagnosticName;
    @SerializedName("PharmacyName")
    @Expose
    private String pharmacyName;
    @SerializedName("AmbulanceName")
    @Expose
    private String ambulanceName;
    @SerializedName("TechnicianName")
    @Expose
    private String technicianName;
    @SerializedName("DiagnosticPhone")
    @Expose
    private String diagnosticPhone;
    @SerializedName("PharmacyPhone")
    @Expose
    private String pharmacyPhone;
    @SerializedName("AmbulancePhone")
    @Expose
    private String ambulancePhone;
    @SerializedName("TechnicianPhone")
    @Expose
    private String technicianPhone;
    @SerializedName("isAmbulance")
    @Expose
    private Boolean isAmbulance;
    @SerializedName("isTechnician")
    @Expose
    private Boolean isTechnician;
    @SerializedName("isDiagnostic")
    @Expose
    private Boolean isDiagnostic;
    @SerializedName("isPharmacy")
    @Expose
    private Boolean isPharmacy;
    @SerializedName("StateName")
    @Expose
    private String stateName;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("ZipCode")
    @Expose
    private String zipCode;

    @SerializedName("HospitaImage")
    @Expose
    private String hospitaImage;



    public ChargesDetailsModel getChargesDetailsModel() {
        return chargesDetailsModel;
    }

    public void setChargesDetailsModel(ChargesDetailsModel chargesDetailsModel) {
        this.chargesDetailsModel = chargesDetailsModel;
    }

    @SerializedName("ChargesDetails")
    @Expose
    private ChargesDetailsModel chargesDetailsModel;

    public Integer getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(Integer hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalDescription() {
        return hospitalDescription;
    }

    public void setHospitalDescription(String hospitalDescription) {
        this.hospitalDescription = hospitalDescription;
    }

    public String getHospitalLat() {
        return hospitalLat;
    }

    public String getHospitaImage() {
        return hospitaImage != null ? hospitaImage:"";
    }

    public void setHospitaImage(String hospitaImage) {
        this.hospitaImage = hospitaImage;
    }

    public void setHospitalLat(String hospitalLat) {
        this.hospitalLat = hospitalLat;
    }

    public String getHospitalLong() {
        return hospitalLong;
    }

    public void setHospitalLong(String hospitalLong) {
        this.hospitalLong = hospitalLong;
    }

    public String getDiagnosticName() {
        return diagnosticName;
    }

    public void setDiagnosticName(String diagnosticName) {
        this.diagnosticName = diagnosticName;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getAmbulanceName() {
        return ambulanceName;
    }

    public void setAmbulanceName(String ambulanceName) {
        this.ambulanceName = ambulanceName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getDiagnosticPhone() {
        return diagnosticPhone;
    }

    public void setDiagnosticPhone(String diagnosticPhone) {
        this.diagnosticPhone = diagnosticPhone;
    }

    public String getPharmacyPhone() {
        return pharmacyPhone;
    }

    public void setPharmacyPhone(String pharmacyPhone) {
        this.pharmacyPhone = pharmacyPhone;
    }

    public String getAmbulancePhone() {
        return ambulancePhone;
    }

    public void setAmbulancePhone(String ambulancePhone) {
        this.ambulancePhone = ambulancePhone;
    }

    public String getTechnicianPhone() {
        return technicianPhone;
    }

    public void setTechnicianPhone(String technicianPhone) {
        this.technicianPhone = technicianPhone;
    }

    public Boolean getIsAmbulance() {
        return isAmbulance;
    }

    public void setIsAmbulance(Boolean isAmbulance) {
        this.isAmbulance = isAmbulance;
    }

    public Boolean getIsTechnician() {
        return isTechnician;
    }

    public void setIsTechnician(Boolean isTechnician) {
        this.isTechnician = isTechnician;
    }

    public Boolean getIsDiagnostic() {
        return isDiagnostic;
    }

    public void setIsDiagnostic(Boolean isDiagnostic) {
        this.isDiagnostic = isDiagnostic;
    }

    public Boolean getIsPharmacy() {
        return isPharmacy;
    }

    public void setIsPharmacy(Boolean isPharmacy) {
        this.isPharmacy = isPharmacy;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}