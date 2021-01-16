package com.kal.connect.utilities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.kal.connect.adapters.EmptyRecyclerViewAdapter;
import com.kal.connect.models.DoctorModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppComponents {

    // MARK : Date Picker
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    // Handle Empty data in Recyclerview
    // call this method instead of -> .notifyDataSetChanged();
    public static void reloadDataWithEmptyHint(RecyclerView recyclerView, RecyclerView.Adapter dataAdapter, ArrayList<HashMap<String, Object>> dataItems, String emptyMessage){

        if(dataItems == null || dataItems.size() == 0){

            EmptyRecyclerViewAdapter emptyAdapter = new EmptyRecyclerViewAdapter(emptyMessage);
            recyclerView.setAdapter(emptyAdapter);
            emptyAdapter.notifyDataSetChanged();

        }
        else {

            if(dataAdapter != null) {
                recyclerView.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }

        }

    }

    // Handle Empty data in Recyclerview
    // call this method instead of -> .notifyDataSetChanged();
    public static void reloadCustomDataWithEmptyHint(RecyclerView recyclerView, RecyclerView.Adapter dataAdapter, ArrayList<DoctorModel> dataItems, String emptyMessage){

        if(dataItems == null || dataItems.size() == 0){

            EmptyRecyclerViewAdapter emptyAdapter = new EmptyRecyclerViewAdapter(emptyMessage);
            recyclerView.setAdapter(emptyAdapter);
            emptyAdapter.notifyDataSetChanged();

        }
        else {

            if(dataAdapter != null) {
                recyclerView.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }

        }

    }

    // Handle Empty data in Recyclerview
    // call this method instead of -> .notifyDataSetChanged();
    public static <T> void  reloadRecyclerViewCustomDataWithEmptyHint(RecyclerView recyclerView, RecyclerView.Adapter dataAdapter, ArrayList<T> dataItems, String emptyMessage){

        if(dataItems == null || dataItems.size() == 0){

            EmptyRecyclerViewAdapter emptyAdapter = new EmptyRecyclerViewAdapter(emptyMessage);
            recyclerView.setAdapter(emptyAdapter);
            emptyAdapter.notifyDataSetChanged();

        }
        else {

            if(dataAdapter != null) {
                recyclerView.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }

        }

    }

    // Date Picker
    public static DatePickerDialog createDatePicker(Context context, final String dateFormat, final EditText fieldToUpdate) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fieldToUpdate.setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePicker;

    }

    // Time Picker
    public static TimePickerDialog createTimePicker(Context context, final EditText fieldToUpdate) {

        //Get the current Time from the calender class
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String apm = (selectedHour >= 12 && selectedMinute > 0) ? " PM" : " AM";
                fieldToUpdate.setText(selectedHour + ":" + selectedMinute + apm);

            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        return mTimePicker;

    }

    // Build Spinner
    public static ArrayAdapter<String> buildSpinnerWithArray(Context context, String[] dataArray, Spinner spinner, ArrayAdapter<String> adapter) {

        ArrayList<String> dataList = new ArrayList<String>();
        for (int loop = 0; loop < dataArray.length; loop++)
            dataList.add(dataArray[loop]);

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set the ArrayAdapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        return adapter;

        /*
            Code Hint:
            String states[] = new String[]{"Normal User"};
            Utils.buildSpinnerWithDialog(getActivity(), states, "Choose User Type", editText);
        * */


    }

    // Build Spinner in an Alert view
    public static void buildSpinnerInDialog(final Context context, final ArrayList<HashMap<String, String>> itemsToLoad, String title, String keyToParse, UtilitiesInterfaces.SpinnerCallback spinnerCallback) {

        // To receive the selectedItem
        final UtilitiesInterfaces.SpinnerCallback callback = spinnerCallback;

        // Parse Items
        List<String> itemsToDisplay = new ArrayList<String>();
        for(HashMap<String, String> itemInfo :itemsToLoad){
            itemsToDisplay.add("" + itemInfo.get(keyToParse));
        }

        // Build Spinner in alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        String[] items = itemsToDisplay.toArray(new String[itemsToDisplay.size()]);
        // Show Alert view with items
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {


                callback.receiveSelectedItem(itemsToLoad.get(position));
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY)); // set color
        listView.setDividerHeight(1); // set height
        alert.show();

    }


    // Choose Spinner by its value
    public static void chooseSpinnerByString(Spinner spinner, String myString) {

        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        spinner.setSelection(index);

    }

    // Get Screen Width
    public static float getScreenWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth;
    }

    // Get Screen Height
    public static float getScreenHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        return dpHeight;
    }

    // Set Margin for an UIView programmatically
    public static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void setCustomColorForImageView(String colorToSet, ImageView forImageView){
        forImageView.getDrawable().setColorFilter(Color.parseColor(colorToSet), PorterDuff.Mode.SRC_ATOP);
    }

    // MARK : Calculate the time difference
    public static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }

    }

    private String convertUTCFormatToLocalFormat(String inputDate, String inputDateFormat, String outputDateFormat)
    {
        String outputDateString = "00-00-0000 00:00";
        try
        {

            SimpleDateFormat formatter = new SimpleDateFormat(inputDateFormat);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date UTCDateTime = formatter.parse(inputDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat(outputDateFormat);
            formatter.setTimeZone(TimeZone.getDefault());
            outputDateString = dateFormatter.format(UTCDateTime);

        }
        catch (Exception e)  {
            outputDateString = "00-00-0000 00:00";
        }
        return outputDateString;

    }

    /**
     * To get Date value from timestamp
     * @param timeStamp
     * @return
     */
    public static String getValueFromTimeStamp(long timeStamp, String customFormat) {

        String currentTime = "";
        try{

            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timeStamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

            SimpleDateFormat sdf = new SimpleDateFormat(customFormat);
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            sdf.setTimeZone(utcZone);// Set UTC time zone

            Date currenTimeZone = (Date) calendar.getTime();
            currentTime = sdf.format(currenTimeZone);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return currentTime;

    }

    /**
     * To change the date format into the users desired format
     * @param dateToConvert
     * @param dateFormatInPut
     * @param dateFormatOutPut
     * @return
     */
    public static String changeDateFormat(String dateToConvert, String dateFormatInPut, String dateFormatOutPut) {


        String dateToReturn = dateToConvert;

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInPut);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(dateFormatOutPut);
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(dateToConvert);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn;

    }

    /**
     * To change the date format into the users desired format
     * @param dateToConvert
     * @param dateFormat
     * @return
     */
    public static String getTimeDifference(String dateToConvert, String dateFormat) {


        String dateToReturn = dateToConvert;
        String timeDifference = "";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(dateFormat);
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(dateToConvert);
            dateToReturn = sdfOutPutToSend.format(gmt);
            Date localDateTime = sdfOutPutToSend.parse(dateToReturn);
            long milliseconds = localDateTime.getTime();
            timeDifference = getTimeAgo(milliseconds);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;

    }

    public static void downloadImageFileFromURL(final Context fromContext,final String imgURLToDownload, final UtilitiesInterfaces.DownloadImageCallback downloadCallback){

        Picasso.get().load(imgURLToDownload).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                try {
                    ContextWrapper cw = new ContextWrapper(fromContext);
                    File downloadedFile = new File(cw.getDir("imageDir", Context.MODE_PRIVATE), "profile.jpg");// new File(Environment.getExternalStorageDirectory().getPath() + "/" + imgURLToDownload);
                    downloadedFile.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(downloadedFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    ostream.flush();
                    ostream.close();
                    downloadCallback.callbackWithDownloadImageFile(true, downloadedFile);

                } catch (IOException e) {
                    e.printStackTrace();
                    downloadCallback.callbackWithDownloadImageFile(false, null);
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                downloadCallback.callbackWithDownloadImageFile(false, null);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }
}
