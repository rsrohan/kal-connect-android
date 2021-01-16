package com.kal.connect.customLibs.mediaManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.customdialogbox.FlipProgressDialog;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class MediaManager extends CustomActivity {

    public static int IMAGE = 1;
    public static int MULTIPLE_IMAGE = 2;
    public static double MAX_SIZE_IN_MB = 3.0 * 1000.0;


    protected MediaCallback mediaCallback;
    protected MultipleMediaCallback multipleMediaCallback;

    // Type 1: Choose image from Camera and Gallery
    public void chooseImageFromOptions(final MediaCallback callback) {

        Utilities.showAlertDialogWithOptions(MediaManager.this, "Choose image from?", new String[]{"Cancel", "Camera", "Gallery"}, new UtilitiesInterfaces.AlertCallback() {

            @Override
            public void onOptionClick(DialogInterface dialog, int buttonIndex) {

                if (buttonIndex == 1) {
                    chooseImageFromCamera(callback);
                } else if (buttonIndex == 2) {
                    chooseImageFromGallery(callback);
                }

            }

        });

    }

    // Type 2: Choose Image only from Gallery
    public void chooseImageFromGallery(MediaCallback callback) {

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManager.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick image from the gallery
                    EasyImage.openGallery(MediaManager.this, IMAGE);

                }

            }

        });

    }

    // Type 3: Choose Image only from Camera
    public void chooseImageFromCamera(MediaCallback callback) {

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for Camera
        Utilities.requestPermissions(MediaManager.this, new String[]{Manifest.permission.CAMERA}, new UtilitiesInterfaces.PermissionCallback() {
            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {
                    // Launch intent to pick image from the gallery
                    EasyImage.openCamera(MediaManager.this, IMAGE);
                }

            }
        });

    }

    // Type 4: Choose Image only from Gallery
    public void chooseMultipleImagesFromGallery(final int maxCount, MultipleMediaCallback callback) {

        // To Receive Selected Image
        this.multipleMediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManager.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick multiple image from the gallery
                    Matisse.from(MediaManager.this)
                            .choose(MimeType.ofImage(), false)
                            .countable(true)
                            .maxSelectable(maxCount)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .forResult(MULTIPLE_IMAGE);

                }

            }

        });

    }


    // Receive File Selected from Gallery/Camera/File
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Choose Multiple Images
        if (requestCode == MULTIPLE_IMAGE && data != null) {

            ArrayList<File> imagesInFile = new ArrayList<File>();
            ArrayList<Bitmap> imagesInBitmap = new ArrayList<Bitmap>();
            ;
            for (int loop = 0; loop < Matisse.obtainPathResult(data).size(); loop++) {

                File selectedFile = new File(Matisse.obtainPathResult(data).get(loop));
                File compressedFile = checkAndCompressSelectedImage(selectedFile);
                Bitmap compressedBitmap = BitmapFactory.decodeFile(compressedFile.getPath());
                imagesInFile.add(compressedFile);
                imagesInBitmap.add(compressedBitmap);

            }
            multipleMediaCallback.receiveSelectedImages(imagesInFile, imagesInBitmap);

        }
        // Handle for Picking single image from camera/gallery
        else {


            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                    e.printStackTrace();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                    // Check the type for remaining file types
                    if (type == IMAGE) {

                        File compressedFile = checkAndCompressSelectedImage(imageFile);
                        Bitmap compressedBitmap = BitmapFactory.decodeFile(compressedFile.getPath());
                        mediaCallback.receiveSelectedImage(compressedFile, compressedBitmap);

                    }

                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {

                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {

                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MediaManager.this);
                        if (photoFile != null) photoFile.delete();

                    }

                }

            });
        }

    }

    /**
     * Reduce the file size with the given size
     * @param imageFile
     * @return
     */
    private File checkAndCompressSelectedImage(File imageFile) {

        // Perform cropping and return the image Here
        double selectedFileSize = Double.parseDouble(String.valueOf(imageFile.length() / 1024));
        System.out.println("Original File Size : " + String.valueOf(imageFile.length() / 1024));
        if (selectedFileSize > MAX_SIZE_IN_MB) {

            try {

                FlipProgressDialog pDialog = Utilities.showLoading(MediaManager.this);
                // Get Bitmap from the file
                File compressedImageFile = new Compressor(MediaManager.this).setQuality(50).compressToFile(imageFile);
                Bitmap compressedBitmap = BitmapFactory.decodeFile(compressedImageFile.getPath());

                if (pDialog != null) {
                    pDialog.dismiss();
                }

                double compressedSize = Double.parseDouble(String.valueOf(compressedImageFile.length() / 1024));
                System.out.println("Compressed File Size : " + compressedSize);
                // SaveImage(compressedBitmap);
                return compressedImageFile;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageFile;

    }

    // Interface to receive the selected image
    public interface MediaCallback {

        void receiveSelectedImage(File file, Bitmap image);

    }

    protected interface MultipleMediaCallback {

        void receiveSelectedImages(ArrayList<File> imagesFiles, ArrayList<Bitmap> imagesInBitmap);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.popAnimation(MediaManager.this);
    }



    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
