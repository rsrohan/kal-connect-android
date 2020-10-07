package com.kal.connect.customLibs.JSONHandler;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JSONHandler {

    public static String convertToJSONString(HashMap<String, Object> inputHashMap){

        // Save categories inside the preferrenens
        Gson gson = new Gson();
        String json = gson.toJson(inputHashMap);
        return json;

    }

    // MARK : Convert Any JSON object into Map
    public static HashMap<String, Object> jsonObjectToHashMap(JSONObject json) throws JSONException {

        HashMap<String, Object> retMap = new HashMap<String, Object>();

        try {
            if(json != JSONObject.NULL) {
                retMap = toMap(json);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return retMap;
    }

    // MARK : Conversion of json object Map
    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        try {

            Iterator<String> keysItr = object.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if(value instanceof JSONArray) {
                    value = jsonArrayToList((JSONArray) value);
                }

                else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                map.put(key, value);
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return new HashMap<String, Object>();
        }

        return map;
    }

    // MARK : Conversion of JSONArray to Array list
    public static List<Object> jsonArrayToList(JSONArray array) throws JSONException {

        List<Object> list = new ArrayList<Object>();

        try{

            for(int i = 0; i < array.length(); i++) {

                Object value = array.get(i);
                if(value instanceof JSONArray) {
                    value = jsonArrayToList((JSONArray) value);
                }

                else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);

            }

        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Object>();
        }

        return list;

    }

}
