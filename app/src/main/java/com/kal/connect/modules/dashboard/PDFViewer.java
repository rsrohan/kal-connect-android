package com.kal.connect.modules.dashboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kal.connect.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.customLibs.downloadService.DirectoryHelper;
import com.kal.connect.customLibs.downloadService.DownloadFileService;
import com.kal.connect.utilities.Utilities;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PDFViewer extends CustomActivity implements DownloadFile.Listener {
    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    EditText etPdfUrl;
    Button btnDownload;
    PDFPagerAdapter adapter;
    String url;

    @BindView(R.id.close)
    Button closeButton;

    @BindView(R.id.share)
    Button shareBtn;


    @BindView(R.id.pdf_container)
    LinearLayout pdfViewContainer;

    String localDownloadedFile;

    ProgressDialog progressDialog;

    @OnClick(R.id.close)
    void close(){
        finish();
    }

    @OnClick(R.id.share)
    void share(){

        if(true)
        {
            downloadToLocal();
            return;
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MEDI360 CONNECT");
        setContentView(R.layout.activity_remote_pdf);

        ButterKnife.bind(this);

        root = findViewById(R.id.remote_pdf_root);
//        etPdfUrl = findViewById(R.id.et_pdfUrl);
//        btnDownload = findViewById(R.id.btn_download);

        try{
             url = getIntent().getStringExtra("Url");
            if(url != null && !url.isEmpty()){
                setDownloadButtonListener();
            }else{
                finish();
            }
        }catch (Exception e){
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    void downloadToLocal(){

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */

                try{
                    final JSONObject fileInfo = new JSONObject();
                    String filename=url.substring(url.lastIndexOf("/")+1);
                    fileInfo.put(DownloadFileService.FILE_NAME_KEY,filename);
                    fileInfo.put(DownloadFileService.FILE_URL_KEY, url);
                    startService(DownloadFileService.getDownloadService(PDFViewer.this, fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));

                    // Step 3: Show the detail in alertview
                    Utilities.showAlert(PDFViewer.this, "Downloading "+ filename, false);

                }catch (Exception e){

                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                remotePDFViewPager = new RemotePDFViewPager(ctx, getUrlFromEditText(), listener);
//                remotePDFViewPager.setId(R.id.pdfViewPager);
//                hideDownloadButton();
//            }
//        });
        remotePDFViewPager = new RemotePDFViewPager(ctx, url, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
         progressDialog = Utilities.showLoading(this);


    }

//    protected String getUrlFromEditText() {
//        return etPdfUrl.getText().toString().trim();
//    }

//    public void showDownloadButton() {
//        btnDownload.setVisibility(View.VISIBLE);
//    }
//
//    public void hideDownloadButton() {
//        btnDownload.setVisibility(View.INVISIBLE);
//    }

    public void updateLayout() {
        pdfViewContainer.removeAllViewsInLayout();
//        root.addView(etPdfUrl,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        root.addView(closeButton,
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pdfViewContainer.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        shareBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        localDownloadedFile = destinationPath;
        updateLayout();
        progressDialog.hide();

    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        progressDialog.hide();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }
}