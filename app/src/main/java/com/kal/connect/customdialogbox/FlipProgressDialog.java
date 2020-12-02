package com.kal.connect.customdialogbox;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.kal.connect.R;

import java.util.Objects;

//dialog destroy
//http://stackoverflow.com/questions/14657490/how-to-properly-retain-a-dialogfragment-through-rotation


public class FlipProgressDialog extends DialogFragment {

    ImageView image;


    public FlipProgressDialog() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setBackgroundDim();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Can not dismiss when pressing outside the dialog
        getDialog().setCanceledOnTouchOutside(false);

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoDimDialogFragmentStyle);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.progress_dialog, null, false);


        return view;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }


    private void setBackgroundDim(){
        Window window = getDialog().getWindow();
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams windowParams = null;
        if (window != null) {
            windowParams = window.getAttributes();
        }
        if (windowParams != null) {
            try{
                windowParams.dimAmount = 0.8f;

            }catch (Exception e){}
        }
        if (windowParams != null) {
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        }
        if (window != null) {
            window.setAttributes(windowParams);
        }
    }



}

