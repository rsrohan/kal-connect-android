package com.kal.connect.utilities;

import com.kal.connect.models.DoctorModel;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.AyurvedaModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobValues {


    private static GlobValues instance = new GlobValues();

    private static String selectedAppointment;
    private static HashMap<String, Object> selectedAppointmentData;
    private static HashMap<String, Object> addAppointmentinputParams;
    private static JSONObject appointmentCompleteDetails;
    private static DoctorModel doctor;

    public static ArrayList<AyurvedaModule> mAlDuplicate = new ArrayList<>();

    private static DoctorModel aboutDoctor;

    public static DoctorModel getAboutDoctor() {
        return aboutDoctor;
    }

    public static void setAboutDoctor(DoctorModel aboutDoctor) {
        GlobValues.aboutDoctor = aboutDoctor;
    }

    public static JSONArray cityAry, stateAry;

    // Getter-Setters


    public static JSONArray getCityAry() {
        return cityAry;
    }

    public static String cityNameFromID(String cityID)
    {
        try{
            for (int i = 0; i < cityAry.length(); i++) {
                if (cityAry.getJSONObject(i).getString("cityid").equals(cityID)) {
                    return cityAry.getJSONObject(i).getString("cityname");
                }

            }

        }catch (Exception e){}
        return "";
    }

    public static void setCityAry(JSONArray cityAry) {
        GlobValues.cityAry = cityAry;
    }

    public static JSONArray getStateAry() {
        return stateAry;
    }

    public static void setStateAry(JSONArray stateAry) {
        GlobValues.stateAry = stateAry;
    }

    public static DoctorModel getDoctor() {
        return doctor;
    }

    public static void setDoctor(DoctorModel doctor) {
        GlobValues.doctor = doctor;
    }

    public static GlobValues getInstance() {
        return instance;
    }

    public static void clearGlobalData()
    {
        selectedAppointment = "";
    }

    public static HashMap<String, Object>  getSelectedAppointmentData() {
        return selectedAppointmentData;
    }

    public static HashMap<String, Object>  getAddAppointmentParams() {
        return addAppointmentinputParams;
    }

    public static void setupAddAppointmentParams() {
        addAppointmentinputParams = AppPreferences.getInstance().sendingInputParam();

    }



    public void addAppointmentInputParams(String key,Object value){
        addAppointmentinputParams.put(key,value);

    }

    public static void setSelectedAppointmentData(HashMap<String, Object>  selectedAppointmentData) {
        GlobValues.selectedAppointmentData = selectedAppointmentData;
    }

    public static void setInstance(GlobValues instance) {
        GlobValues.instance = instance;
    }

    private ArrayList<IssuesModel> selectedIssuesModelList = new ArrayList<>();

    private GlobValues() {
    }

    public static String getSelectedAppointment() {
        return selectedAppointment;
    }

    public static void setSelectedAppointment(String selectedAppointment) {
        GlobValues.selectedAppointment = selectedAppointment;
    }

    public ArrayList<IssuesModel> getSelectedIssuesModelList() {
        return selectedIssuesModelList;
    }

    public void setSelectedIssuesModelList(ArrayList<IssuesModel> selectedIssuesModelList) {
        this.selectedIssuesModelList = selectedIssuesModelList;
    }

    public static JSONObject getAppointmentCompleteDetails() {
        return appointmentCompleteDetails;
    }

    public static void setAppointmentCompleteDetails(JSONObject appointmentCompleteDetails) {
        GlobValues.appointmentCompleteDetails = appointmentCompleteDetails;
    }
}
