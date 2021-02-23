package com.kal.connect.appconstants;


import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class APIWebServiceConstants {


    private static String NAMESPACE = "https://www.medi360.in/";
    public static final String BASE_URL="https://www.medi360.in/";

    /**
    * Make sure about isTesting variable.
    * */

    static String LIVE_URL = "https://www.ayurvaidya.live/WebServices/";
    public static boolean isTesting = true;

//    private static String LIVE_URL = "http://ec2-13-127-154-179.ap-south-1.compute.amazonaws.com/WebServices/";
//    public static boolean isTesting = true;

    private static String SOAP_ACTION = "https://www.medi360.in/";

    public static String LIVE_LINK = "https://www.medi360.in/";

    public static String invokeWebservice(String jsonObjSend,String urlEnd, String webMethName) {
        String resTxt = null;


        Log.e("Webservices*** API URl", "$$$$$$$ "+SOAP_ACTION+webMethName);
        Log.e("Webservices***", "$$$$$$$ "+urlEnd+"/"+webMethName);
        Log.e("jsdata***", "$$$$$$$    : "+jsonObjSend);

        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        request.addProperty("jsdata", jsonObjSend.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport;
        androidHttpTransport = new HttpTransportSE(LIVE_URL+urlEnd);


        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occurred";
        }

        return resTxt;
    }




}
