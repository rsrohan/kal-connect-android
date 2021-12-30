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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class APIWebServiceConstants {


    public static String new_url = "https://www.myayurveda.com/";
    static String old_url = "https://www.medi360.in/";

    private static String NAMESPACE = new_url;
    public static final String BASE_URL = new_url;
    private static String SOAP_ACTION = new_url;

    /*************************************
     * todo Make sure about isTesting variable.
     *************************************/

    //older domain
   //static String LIVE_URL = "https://www.ayurvaidya.live/WebServices/";

    //new domain
    static String LIVE_URL = "https://www.myayurveda.com/WebServices/";
    //public static boolean isTesting = false;

    //private static String LIVE_URL = "http://ec2-13-127-154-179.ap-south-1.compute.amazonaws.com/WebServices/";
    public static boolean isTesting = true;


    public static String invokeWebservice(String jsonObjSend, String urlEnd, String webMethName) {
        String resTxt = null;


        Log.e("Webservices*** API URl", "$$$$$$$ " + SOAP_ACTION + webMethName);
        Log.e("Webservices***", "$$$$$$$ " + urlEnd + "/" + webMethName);
        Log.e("jsdata***", "$$$$$$$    : " + jsonObjSend);

        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        request.addProperty("jsdata", jsonObjSend.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport;
        allowAllSSL();

        androidHttpTransport = new HttpTransportSE(LIVE_URL + urlEnd);


        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occurred\n" + e;
        }

        return resTxt;
    }


    private static TrustManager[] trustManagers;


    public static class EasyX509TrustManager
            implements X509TrustManager {

        private X509TrustManager standardTrustManager = null;

        /**
         * Constructor for EasyX509TrustManager.
         */
        public EasyX509TrustManager(KeyStore keystore)
                throws NoSuchAlgorithmException, KeyStoreException {
            super();
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keystore);
            TrustManager[] trustmanagers = factory.getTrustManagers();
            if (trustmanagers.length == 0) {
                throw new NoSuchAlgorithmException("no trust manager found");
            }
            this.standardTrustManager = (X509TrustManager) trustmanagers[0];
        }

        /**
         * @see X509TrustManager#checkClientTrusted(X509Certificate[], String authType)
         */
        public void checkClientTrusted(X509Certificate[] certificates, String authType)
                throws CertificateException {
            standardTrustManager.checkClientTrusted(certificates, authType);
        }

        /**
         * @see X509TrustManager#checkServerTrusted(X509Certificate[], String authType)
         */
        public void checkServerTrusted(X509Certificate[] certificates, String authType)
                throws CertificateException {
            if ((certificates != null) && (certificates.length == 1)) {
                certificates[0].checkValidity();
            } else {
                standardTrustManager.checkServerTrusted(certificates, authType);
            }
        }

        /**
         * @see X509TrustManager#getAcceptedIssuers()
         */
        public X509Certificate[] getAcceptedIssuers() {
            return this.standardTrustManager.getAcceptedIssuers();
        }

    }

    public static void allowAllSSL() {

        HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        if (hostname.equalsIgnoreCase("www.medi360.in") ||
                                hostname.equalsIgnoreCase("kal-connect.firebaseio.com") ||
                                hostname.equalsIgnoreCase("www.myayurveda.com") ||
                                hostname.equalsIgnoreCase("api.razorpay.com") ||
                                hostname.equalsIgnoreCase("telehealth.keralaayurveda.biz") ||
                                hostname.equalsIgnoreCase("ec2-13-127-154-179.ap-south-1.compute.amazonaws.com")
                                /*||
                                hostname.equalsIgnoreCase("telehealth.keralaayurveda.biz")*/) {
                            return true;
                        } else {
                            return false;
                        }
                    }
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }

                });

        javax.net.ssl.SSLContext context = null;

        if (trustManagers == null) {
            try {
                trustManagers = new TrustManager[]{new EasyX509TrustManager(null)};
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }


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
