package com.kal.connect.customLibs.mediaManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class MediaManagerV2 extends CustomActivity {

    public static int IMAGE = 1;
    public static int MULTIPLE_IMAGE = 2;

    protected MediaCallback mediaCallback;
    protected MultipleMediaCallback multipleMediaCallback;

    // Default Ratio
    protected int defaultAspectW = 1, defaultAspectH = 1;

    // Max WidthxHeight
    // protected int MAX_CROP_WIDTH = 1024, MAX_CROP_HEIGHT = 900;
    protected int MAX_CROP_WIDTH = 0, MAX_CROP_HEIGHT = 0;

    // Default Max Sizes
    private void resetToDefaultMaxSizes(){
        MAX_CROP_WIDTH = 1024;
        MAX_CROP_HEIGHT = 900;
    }

    // Type 1: Choose image from Camera and Gallery
    public void chooseImageFromOptions(final MediaCallback callback) {

        Utilities.showAlertDialogWithOptions(MediaManagerV2.this, "Choose image from?", new String[]{"Cancel", "Camera", "Gallery"}, new UtilitiesInterfaces.AlertCallback() {

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

        this.defaultAspectW = 1;
        this.defaultAspectH = 1;
        resetToDefaultMaxSizes();

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick image from the gallery
                    EasyImage.openGallery(MediaManagerV2.this, IMAGE);

                }

            }

        });

    }

    // Type 2: Choose Image only from Gallery - Supports - Rectangle and Square Both
    public void chooseImageFromGalleryWithManualCropRatio(MediaCallback callback) {

        this.defaultAspectW = 0;
        this.defaultAspectH = 0;
        resetToDefaultMaxSizes();

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick image from the gallery
                    EasyImage.openGallery(MediaManagerV2.this, IMAGE);

                }

            }

        });

    }

    // Type 2: Choose Image only from Gallery - Supports - Rectangle and Square Both
    public void chooseImageFromGalleryWithManualCropRatioWithMaxSize(int maxWidth, int maxHeight, MediaCallback callback) {


        this.defaultAspectW = 0;
        this.defaultAspectH = 0;
        MAX_CROP_WIDTH = maxWidth;
        MAX_CROP_HEIGHT = maxHeight;

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick image from the gallery
                    EasyImage.openGallery(MediaManagerV2.this, IMAGE);

                }

            }

        });

    }

    // Type 2: Choose Image only from Gallery
    public void chooseImageFromGalleryWithCrop(int rationW, int ratioH, MediaCallback callback) {

        this.defaultAspectW = rationW;
        this.defaultAspectH = ratioH;
        resetToDefaultMaxSizes();

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick image from the gallery
                    EasyImage.openGallery(MediaManagerV2.this, IMAGE);

                }

            }

        });

    }

    // Type 2: Choose Image only from Gallery
    public void chooseMultipleImagesFromGallery(final int maxCount, MultipleMediaCallback callback) {

        // To Receive Selected Image
        this.multipleMediaCallback = callback;

        // Check permission for External storage
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new UtilitiesInterfaces.PermissionCallback() {

            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    // Launch intent to pick multiple image from the gallery
                    Matisse.from(MediaManagerV2.this)
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


    // Type 3: Choose Image only from Camera
    public void chooseImageFromCamera(MediaCallback callback) {

        // To Receive Selected Image
        this.mediaCallback = callback;

        // Check permission for Camera
        Utilities.requestPermissions(MediaManagerV2.this, new String[]{Manifest.permission.CAMERA}, new UtilitiesInterfaces.PermissionCallback() {
            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {
                    // Launch intent to pick image from the gallery
                    EasyImage.openCamera(MediaManagerV2.this, IMAGE);
                }

            }
        });

    }

    // Receive File Selected from Gallery/Camera/File
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Crop Result
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                // Return the cropped image
                File croppedFile = new File(resultUri.getPath());
                Bitmap selectedImgInBitmap = BitmapFactory.decodeFile(croppedFile.getPath());

                // Get Size Width and Height
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(resultUri.getPath()).getAbsolutePath(), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                System.out.println("WIDTH : "+ imageWidth);
                System.out.println("HEIGHT : "+ imageHeight);

                mediaCallback.receiveSelectedImage(croppedFile, selectedImgInBitmap);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
        // Choose Multiple Images
        else if(requestCode == MULTIPLE_IMAGE && data != null){

            ArrayList<File> imagesInFile = new ArrayList<File>();
            ArrayList<Bitmap> imagesInBitmap = new ArrayList<Bitmap>();;
            for(int loop = 0; loop < Matisse.obtainPathResult(data).size(); loop++){

                File fileImg = new File(Matisse.obtainPathResult(data).get(loop));
                Bitmap selectedImgInBitmap = BitmapFactory.decodeFile(fileImg.getPath());
                imagesInFile.add(fileImg);
                imagesInBitmap.add(selectedImgInBitmap);

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
                    if(type == IMAGE) {

                        // Perform Cropping
                        CropImage.ActivityBuilder cropActivity = CropImage.activity(Uri.fromFile(imageFile))
                                .setMaxCropResultSize(MAX_CROP_WIDTH, MAX_CROP_HEIGHT)
                                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setCropShape(CropImageView.CropShape.RECTANGLE);

                        // Choose Crop
                        if(defaultAspectW == 0 && defaultAspectH == 0) {
                            cropActivity.start(MediaManagerV2.this);
                        }
                        else {
                            cropActivity.setAspectRatio(defaultAspectW,defaultAspectH)
                                        .setFixAspectRatio(true).start(MediaManagerV2.this);
                        }

                    }

                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {

                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {

                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MediaManagerV2.this);
                        if (photoFile != null) photoFile.delete();

                    }

                }

            });
        }

    }

    // Interface to receive the selected image
    protected interface MediaCallback {

        void receiveSelectedImage(File file, Bitmap image);

    }
    protected interface MultipleMediaCallback {

        void receiveSelectedImages(ArrayList<File> imagesFiles, ArrayList<Bitmap> imagesInBitmap);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.popAnimation(MediaManagerV2.this);
    }


}
