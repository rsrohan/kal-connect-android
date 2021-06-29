package com.kal.connect.utilities;

import android.app.Activity;
import android.media.Ringtone;

import org.json.JSONArray;

import static com.kal.connect.appconstants.APIWebServiceConstants.new_url;

public class Config {

    // For Default Validations
    public static int minReplyLength = 20;

    public static int passMinLength = 4;

    public static int minPhoneLength = 10;
    public static int maxPhoneLength = 13;

    public static int minLength = 1;
    public static int maxLength = 158;

    public static int commentsMinLenght = 20;

    public static int contentMaxLength = 10000000;

    // Media / Crop properties
    public static int maxImageSelection = 5;

    //App Prefs
    public static String AppName = "Kerala Ayurveda";
    public static String AppColorPrimary = "#F99E2D";
    public static String DefaultSelectionColor = "#A9A9A9";

    public static int DeviceType = 1;
    public static  Boolean isBack = false;
    public static  Boolean isChat = false;
    public static Ringtone ringtone;
    static int minVersionCode = 3;

    /**  API Services **/

    /** List of APIs **/

    public  static  final String PAYMENT_BASE_URL = "https://api.razorpay.com/v1/";

    //EndURL/ File
    public  static  final String WEB_Services1 = "PatientMobileData.asmx";
    public  static  final String WEB_Services2 = "kiosknewdata.asmx";
    public  static  final String WEB_Services3 = "Ayurveda.asmx";
    public  static  final String WEB_Services4 = "SpecialistMobileData.asmx";
    public  static  final String WEB_Services5 = "GeneralUtility.asmx";
    public static Activity mActivity;
    public static Boolean isDisconnect = false;

    // Authentication

    public static final String LOGIN_VIA_PASSWORD =  "AuthenticatePatient";
    public static final String VERIFYOTP =  "VerifyOTP";
    public static final String CHANGE_PASSWORD =  "Changepassword";
    public static final String LOGOUT =  "LogOutPatient";


    // COmmonData
    public static final String GET_APP_COMMON_DATA =  "GetPatientAppCommonData";
    public static final String GET_PATIENT_DATA =  "GetPatientProfileData";

    public static final String GET_ISSUES_DATA =  "GetComplaintsList";

    //Appointments
    public static final String EMAIL_MEDICINE_TO_PHARMACY =  "EmailMedicinetoPharmacy";
    public static final String GET_BUY_MEDICINE =  "Buymedicine";
    public static final String GET_KAL_PRODUCT_LIST =  "GetKALProductList";
    public static final String GET_APPOINTMENTS_LIST =  "PatientAppointments";
    public static final String GET_APPOINTMENTS_DETAILS =  "GetPatientComplaintCompleteDetails";
    public static final String CANCEL_APPOINTMENT =  "CancelAppointment";
    public static final String PLACE_ORDER_FOR_MEDICINE =  "PatientPlaceMedicineOrder";

    //DoctorsList
    public static final String GET_DOCTORS_LIST =  "SearchDoctor";

    //Account
    public static final String UPDATE_PROFILE =  "UpdateProfile";

    //Appointment
    public static final String BOOK_APPOINTMENT =  "BookAppointment";

    //EMR data update
    public static final String UPDATE_EMR =  "UpdatePatientEMR";

    public static final String INITIATE_VIDEO_CALL =  "StartVCwithDoctor";

    public static final String END_CALL =  "endcall";

    public static final String UPDATE_LOCATION_DETAILS = "UpdateLocationDetails";

    public static final String GET_HOSPITALS_LIST =  "GetHospitalList";

    public static final String GET_HOSPITAL_DOCTORS_LIST =  "GetHospitalDoctorList";
    public static final String GET_CHECKING_PAYMENT_STATUS =  "CheckPatientsPaymentStatus";

    public static final String UPLOAD_FILES = "UploadPatFiles";

    public static final String GET_AYURVEDHA_MODULES = "GetAllAyurvedaModules";
    public static final String INITIATE_CHAT = "InitiateChat";

    public static final String CREATE_ORDER = PAYMENT_BASE_URL+ "orders";

    public static final String IMAGE_URL_FOR_SPEED = new_url+"images/logo.png";


    public static final String IS_FROM_PATIENT = "1";





}
