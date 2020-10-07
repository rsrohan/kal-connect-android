package com.kal.connect.utilities;

import android.content.DialogInterface;

import java.io.File;
import java.util.HashMap;

public class UtilitiesInterfaces {

    /**
     * To Receive Alert dialog click events
     */
    public interface AlertCallback {
        public void onOptionClick(DialogInterface dialog, int buttonIndex);
    }

    // Interface
    public interface PermissionCallback {
        void receivePermissionStatus(Boolean isGranted);
    }

    // Interface
    public interface SpinnerCallback {
        void receiveSelectedItem(HashMap<String, String> selectedItem);
    }

    public interface StatusCallback {
        void callbackWithStatus(Boolean status);
    }

    public interface DownloadImageCallback {
        void callbackWithDownloadImageFile(Boolean status, File imageAsFile);
    }


    /**

     <EditText
         android:id="@+id/txtContent"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:layout_marginTop="@dimen/default_padding"
         android:background="@drawable/edit_text_style"
         android:padding="@dimen/edit_text_padding"
         android:textAppearance="@style/CustomFont.Regular"
         android:textSize="@dimen/default_font_size"

         android:inputType="textMultiLine"
         android:lines="5"
         android:minLines="5"
         android:gravity="top|left"
         android:scrollbars="vertical" />

     */
}
