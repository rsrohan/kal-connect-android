package com.kal.connect.customLibs.autoCompletor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.kal.connect.customLibs.HTTP.GetPost.HTTPClient;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MysteryMachine on 12/29/2016.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> {

    // Params to search via API
    public String apiURL = null;
    HashMap<String, Object> inputParams = new HashMap<String, Object>();
    public String nameToParse = "";
    public String arrayNameToParse = "";

    public Context context = null;
    public ArrayList<String> itemsToDisplay = new ArrayList<String>();
    public ArrayList<HashMap<String, Object>> sourceItems = new ArrayList<HashMap<String, Object>>();

    public AutoCompleteAdapter(Context context, String nameToParse, ArrayList<HashMap<String, Object>> sourceItems) {

        super(context, android.R.layout.simple_dropdown_item_1line);
        this.context = context;

        // Field name to compare it in the array of hash items
        this.nameToParse = nameToParse;
        // Pass Items to filter without making API
        this.sourceItems = sourceItems;

    }

    public AutoCompleteAdapter(Context context, String searchURL, HashMap<String, Object> inputParams, String nameToParse, String arrayNameToParse) {

        super(context, android.R.layout.simple_dropdown_item_1line);
        this.context = context;
        this.apiURL = searchURL;
        this.inputParams = inputParams;
        this.nameToParse = nameToParse;
        this.arrayNameToParse = arrayNameToParse;

    }

    @Override
    public int getCount() {
        return sourceItems.size();
    }

    @Override
    public String getItem(int index) {
        return sourceItems.get(index).get(nameToParse).toString();
    }

    public HashMap<String, Object> getAutoCompleteItemByIndex(int index) {
        return sourceItems.get(index);
    }

    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence inputText) {

                FilterResults filterResults = new FilterResults();
                if (inputText != null && inputText.toString().length() > 0) {

                    if (inputText != null) {

                        // To Filter the text from input search text
                        if (apiURL == null) {

                            itemsToDisplay.clear();
                            for (int index = 0; index < sourceItems.size(); index++) {

                                HashMap<String, Object> matchedItem = sourceItems.get(index);
                                if ((inputText != null && matchedItem.get(nameToParse) != null) && matchedItem.get(nameToParse).toString().toLowerCase().contains(inputText.toString().toLowerCase())) {
                                    itemsToDisplay.add(matchedItem.get(nameToParse).toString());
                                }

                            }

                        }
                        // To load data from API
                        else {

                            try {

                                FilterViaAPI searchTask = new FilterViaAPI();
                                String response = searchTask.execute().get();

                                if (Utilities.isValidResponse(context, response) != null) {

                                    JSONObject APIResponse = new JSONObject(response);
                                    JSONArray searchResults = APIResponse.getJSONArray(arrayNameToParse);
                                    for (int index = 0; index < searchResults.length(); index++) {

                                        JSONObject currentItem = searchResults.getJSONObject(index);

                                        // Filtered items to display
                                        HashMap<String, Object> matchedItem = Utilities.convertJSONObjectToHashMapObject(currentItem.toString());
                                        sourceItems.add(matchedItem);

                                        // Item name to display
                                        itemsToDisplay.add(matchedItem.get(nameToParse).toString());

                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        filterResults.values = itemsToDisplay;
                        filterResults.count = itemsToDisplay.size();

                    }

                }
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence inputText, FilterResults filterResults) {

                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }

        };
        return myFilter;

    }

    private class FilterViaAPI extends AsyncTask<Void, Void, String> {

        String apiResponse = "";

        @Override
        protected String doInBackground(Void... voids) {

            //Build HTTPClient
            HTTPClient myHttpClient = new HTTPClient();
            myHttpClient.context = context;

            //set the Response
            Log.d("API URL", apiURL);
            Log.d("Input Params", "" + inputParams);

            //Check interne connection
            if (Utilities.isNetworkAvailable(context)) {

                apiResponse = myHttpClient.makeHttpRequest(apiURL, "POST", inputParams,null);
                if (apiResponse != null) {
                    if (Utilities.isValidResponse(context, apiResponse) != null) {
                        return apiResponse;
                    }
                }

            }

            return apiResponse;

        }
    }

}
