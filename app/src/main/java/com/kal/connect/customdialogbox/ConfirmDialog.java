package com.kal.connect.customdialogbox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kal.connect.R;


public class ConfirmDialog extends Dialog implements View.OnClickListener {

    DialogListener dialogListener;
    String message = "";
    public ConfirmDialog(@NonNull Context context, boolean cancelable, String message, DialogListener dialogListener, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable,cancelListener);
        this.message = message;
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_dialog);

        TextView tv_message = findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setText("Done");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener!=null)
                {
                    dialogListener.onYes();
                }
                dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    public interface DialogListener
    {
        void onYes();
        void onNO();
    }
}
