package com.kal.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DoctorModel {

    @SerializedName("objdocAvail")
    @Expose
    private List<ObjdocAvail> objdocAvail = null;

    @SerializedName("SpecialistID")
    @Expose
    private Integer specialistID;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("RoleID")
    @Expose
    private Integer roleID;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Breifnote")
    @Expose
    private String breifnote;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("CityID")
    @Expose
    private Integer cityID;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("HospitalID")
    @Expose
    private String hospitalID;
    @SerializedName("SpecialistFullID")
    @Expose
    private String specialistFullID;
    @SerializedName("AllClient")
    @Expose
    private Boolean allClient;

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    @SerializedName("Image")
    @Expose
    private Boolean isPay;
    @SerializedName("isPay")
    @Expose
    private String image;
    @SerializedName("Panno")
    @Expose
    private Object panno;
    @SerializedName("skypeid")
    @Expose
    private String skypeid;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    @SerializedName("checkpic")
    @Expose
    private String checkpic;
    @SerializedName("testcolmn")
    @Expose
    private Object testcolmn;
    @SerializedName("Zipcode")
    @Expose
    private String zipcode;
    @SerializedName("Addressline1")
    @Expose
    private String addressline1;
    @SerializedName("Addressline2")
    @Expose
    private String addressline2;
    @SerializedName("DRegistrationno")
    @Expose
    private String dRegistrationno;
    @SerializedName("Signature")
    @Expose
    private String signature;
    @SerializedName("Unit")
    @Expose
    private String unit;
    @SerializedName("Createrid")
    @Expose
    private Object createrid;
    @SerializedName("VCCharge")
    @Expose
    private Object vCCharge;
    @SerializedName("PaymentAccNumber")
    @Expose
    private Object paymentAccNumber;
    @SerializedName("DocCharge")
    @Expose
    private Object docCharge;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("isLoggedIn")
    @Expose
    private Boolean isLoggedIn;
    @SerializedName("SpecializationName")
    @Expose
    private String specializationName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("DoctorImage")
    @Expose
    private String doctorImage;
    @SerializedName("TechnicianCharge")
    @Expose
    private String technicianCharge;


    @SerializedName("ChargesDetails")
    @Expose
    private DoctorsChargesDetails chargesDetails;

    public List<ObjdocAvail> getObjdocAvail() {
        return objdocAvail != null ? objdocAvail : new ArrayList<ObjdocAvail>();
    }

    public void setObjdocAvail(List<ObjdocAvail> objdocAvail) {
        this.objdocAvail = objdocAvail;
    }

    public Integer getSpecialistID() {
        return specialistID;
    }

    public void setSpecialistID(Integer specialistID) {
        this.specialistID = specialistID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getBreifnote() {
        return breifnote;
    }

    public void setBreifnote(String breifnote) {
        this.breifnote = breifnote;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getSpecialistFullID() {
        return specialistFullID;
    }

    public void setSpecialistFullID(String specialistFullID) {
        this.specialistFullID = specialistFullID;
    }

    public Boolean getAllClient() {
        return allClient;
    }

    public void setAllClient(Boolean allClient) {
        this.allClient = allClient;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getPanno() {
        return panno;
    }

    public void setPanno(Object panno) {
        this.panno = panno;
    }

    public String getSkypeid() {
        return skypeid;
    }

    public void setSkypeid(String skypeid) {
        this.skypeid = skypeid;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getCheckpic() {
        return checkpic;
    }

    public void setCheckpic(String checkpic) {
        this.checkpic = checkpic;
    }

    public Object getTestcolmn() {
        return testcolmn;
    }

    public void setTestcolmn(Object testcolmn) {
        this.testcolmn = testcolmn;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getDRegistrationno() {
        return dRegistrationno;
    }

    public void setDRegistrationno(String dRegistrationno) {
        this.dRegistrationno = dRegistrationno;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Object getCreaterid() {
        return createrid;
    }

    public void setCreaterid(Object createrid) {
        this.createrid = createrid;
    }

    public Object getVCCharge() {
        return vCCharge;
    }

    public void setVCCharge(Object vCCharge) {
        this.vCCharge = vCCharge;
    }

    public Object getPaymentAccNumber() {
        return paymentAccNumber;
    }

    public void setPaymentAccNumber(Object paymentAccNumber) {
        this.paymentAccNumber = paymentAccNumber;
    }

    public String getDocCharge() {
        if(docCharge == null){
            return "";
        }
        return docCharge.toString();
    }

    public void setDocCharge(Object docCharge) {
        this.docCharge = docCharge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation(){
        return address+" "+zipcode;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(String doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getTechnicianCharge() {
        return technicianCharge;
    }

    public void setTechnicianCharge(String technicianCharge) {
        this.technicianCharge = technicianCharge;
    }

    public DoctorsChargesDetails getChargesDetails() {
        return chargesDetails;
    }

    public void setChargesDetails(DoctorsChargesDetails chargesDetails) {
        this.chargesDetails = chargesDetails;
    }

}