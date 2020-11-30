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

public class WebService {


    private static String NAMESPACE = "https://www.medi360.in/";
    public static final String BASE_URL="https://www.medi360.in/";
    //static String LIVE_URL = "https://www.ayurvaidya.live/WebServices/";
    private static String LIVE_URL = "http://ec2-13-127-154-179.ap-south-1.compute.amazonaws.com/WebServices/";

    private static String SOAP_ACTION = "https://www.medi360.in/";

    public static String LIVE_LINK = "https://www.medi360.in/";

    public static String invokeWebservice(String jsonObjSend,String urlEnd, String webMethName) {
        String resTxt = null;


        Log.e("Webservices*** API URl", "$$$$$$$ "+SOAP_ACTION+webMethName);
        Log.e("Webservices***", "$$$$$$$ "+urlEnd+"/"+webMethName);
        Log.e("jsdata***", "$$$$$$$    : "+jsonObjSend);

        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo jsonObj = new PropertyInfo();

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

    private static TrustManager[] trustManagers;

    public static class _FakeX509TrustManager implements
            javax.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    public static void allowAllSSL() {

        HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        javax.net.ssl.SSLContext context = null;

        if (trustManagers == null) {
            trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
        }

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }


}
