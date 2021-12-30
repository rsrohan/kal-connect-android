package com.kal.connect.customLibs.customdialogbox;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kal.connect.R;

import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;


public class ViewImage {

    public void showDialog(Activity activity, String imagePath){

        final Dialog dialog= new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.view_image);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView receiptImage = dialog.findViewById(R.id.receiptImage);
        final ImageView dismissDialog = dialog.findViewById(R.id.dismissDialog);
        dismissDialog.setVisibility(View.GONE);

        GifImageView img_loading = dialog.findViewById(R.id.loading_img);
        TextView errorMsg = dialog.findViewById(R.id.errorMsg);
        img_loading.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.GONE);


        Glide.with(activity.getApplicationContext()).load(Uri.parse(imagePath)).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                img_loading.setVisibility(View.GONE);
                errorMsg.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                img_loading.setVisibility(View.GONE);
                errorMsg.setVisibility(View.GONE);
                return false;
            }
        }).into(receiptImage);
        //receiptImage.setImageURI(Uri.parse(imagePath));

        receiptImage.setScaleX(0);
        receiptImage.setScaleY(0f);
        receiptImage.setOnTouchListener(new ImageMatrixTouchHandler(activity.getApplicationContext()));

        dialog.show();
        receiptImage.animate().scaleX(1f).scaleY(1f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                dismissDialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        dismissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
