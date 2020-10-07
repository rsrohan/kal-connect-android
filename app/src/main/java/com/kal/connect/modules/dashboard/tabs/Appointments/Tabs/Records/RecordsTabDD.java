//package com.patientapp.modules.dashboard.tabs.Appointments.Tabs.Records;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore.Images.Media;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView.LayoutManager;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.MultiplePermissionsReport;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//import com.keralaayurveda.C1291R;
//import com.patientapp.customLibs.downloadService.DirectoryHelper;
//import com.patientapp.customLibs.downloadService.DownloadFileService;
//import com.patientapp.modules.dashboard.tabs.Appointments.Tabs.Records.RecordsAdapter.ItemClickListener;
//import com.patientapp.utilities.AppComponents;
//import com.patientapp.utilities.GlobValues;
//import com.patientapp.utilities.Utilities;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import org.androidannotations.api.rest.MediaType;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class RecordsTabDD extends Fragment implements OnClickListener, ItemClickListener {
//    private static final int PICKFILE_RESULT_CODE = 1001;
//    public static final int progress_bar_type = 0;
//    RecordsAdapter dataAdapter = null;
//    /* access modifiers changed from: private */
//    public ArrayList<HashMap<String, Object>> dataItems = new ArrayList<>();
//    private ProgressDialog pDialog;
//    @BindView(2131296847)
//    Button updateBtn;
//    View view;
//    RecyclerView vwRecords;
//
//    /* access modifiers changed from: 0000 */
//    @OnClick({2131296847})
//    public void update() {
//    }
//
//    /* access modifiers changed from: 0000 */
//    @OnClick({2131296850})
//    public void upload() {
//        Intent chooseFile = new Intent("android.intent.action.GET_CONTENT");
//        chooseFile.setType(MediaType.ALL);
//        startActivityForResult(Intent.createChooser(chooseFile, "Choose a file"), 1001);
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        this.view = inflater.inflate(C1291R.layout.records, container, false);
//        buildUI();
//        ButterKnife.bind((Object) this, this.view);
//        return this.view;
//    }
//
//    public String getPath(Uri uri) {
//        String path;
//        String[] projection = {"_data"};
//        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
//        if (cursor == null) {
//            path = uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
//            cursor.close();
//        }
//        return (path == null || path.isEmpty()) ? uri.getPath() : path;
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1001 && resultCode == -1) {
//            String filePath = getRealPathFromURI_API19(getContext(), data.getData());
//            HashMap<String, Object> item = new HashMap<>();
//            item.put("recordName", filePath.substring(filePath.lastIndexOf("/") + 1));
//            item.put("recordImgPath", filePath);
//            item.put("oldFile", Boolean.valueOf(false));
//            this.dataItems.add(item);
//            AppComponents.reloadDataWithEmptyHint(this.vwRecords, this.dataAdapter, this.dataItems, getActivity().getResources().getString(C1291R.string.no_data_found));
//            showUploadOptions();
//        }
//    }
//
//    public static String getRealPathFromURI_API19(Context context, Uri uri) {
//        String filePath = "";
//        if (!uri.getHost().contains("com.android.providers.media")) {
//            return "";
//        }
//        String id = DocumentsContract.getDocumentId(uri).split(":")[1];
//        String[] column = {"_data"};
//        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, column, "_id=?", new String[]{id}, null);
//        int columnIndex = cursor.getColumnIndex(column[0]);
//        if (cursor.moveToFirst()) {
//            filePath = cursor.getString(columnIndex);
//        }
//        cursor.close();
//        return filePath;
//    }
//
//    public void onResume() {
//        super.onResume();
//    }
//
//    public void onClick(View v) {
//        v.getId();
//    }
//
//    private void buildUI() {
//        this.vwRecords = (RecyclerView) this.view.findViewById(C1291R.C1293id.recordRecycler);
//        buildListView();
//    }
//
//    private void buildListView() {
//        this.dataAdapter = new RecordsAdapter(this.dataItems, getActivity());
//        LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
//        this.vwRecords.setNestedScrollingEnabled(false);
//        this.vwRecords.setLayoutManager(mLayoutManager);
//        this.vwRecords.setItemAnimator(new DefaultItemAnimator());
//        this.vwRecords.setAdapter(this.dataAdapter);
//        if (getActivity() != null) {
//            AppComponents.reloadDataWithEmptyHint(this.vwRecords, this.dataAdapter, this.dataItems, getActivity().getResources().getString(C1291R.string.no_appointments_found));
//        }
//        this.dataAdapter.setOnItemClickListener(new ItemClickListener() {
//            public void onItemClick(int position, View v) {
//                String str = "recordName";
//                try {
//                    HashMap<String, Object> selectedItem = (HashMap) RecordsTab.this.dataItems.get(position);
//                    if (!((Boolean) selectedItem.get("oldFile")).booleanValue()) {
//                        RecordsTab.this.dataItems.remove(position);
//                        AppComponents.reloadDataWithEmptyHint(RecordsTab.this.vwRecords, RecordsTab.this.dataAdapter, RecordsTab.this.dataItems, RecordsTab.this.getActivity().getResources().getString(C1291R.string.no_data_found));
//                        RecordsTab.this.showUploadOptions();
//                        return;
//                    }
//                    final JSONObject fileInfo = new JSONObject();
//                    fileInfo.put(DownloadFileService.FILE_NAME_KEY, selectedItem.get(str).toString());
//                    fileInfo.put(DownloadFileService.FILE_URL_KEY, selectedItem.get("recordImgPath"));
//                    final String fileName = selectedItem.get(str).toString();
//                    Dexter.withActivity(RecordsTab.this.getActivity()).withPermissions("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            RecordsTab.this.getActivity().startService(DownloadFileService.getDownloadService(RecordsTab.this.getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
//                            FragmentActivity activity = RecordsTab.this.getActivity();
//                            StringBuilder sb = new StringBuilder();
//                            sb.append("Downloading ");
//                            sb.append(fileName);
//                            Utilities.showAlert(activity, sb.toString(), Boolean.valueOf(false));
//                        }
//
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
//                        }
//                    }).check();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void openDocument() {
//    }
//
//    private void loadRecords() {
//        String str;
//        String str2 = "Filedescription";
//        Boolean valueOf = Boolean.valueOf(this.dataItems.size() == 0);
//        this.dataItems.clear();
//        try {
//            JSONArray patientRecords = GlobValues.getAppointmentCompleteDetails().getJSONArray("PatRec");
//            for (int loop = 0; loop < patientRecords.length(); loop++) {
//                JSONObject singleObj = patientRecords.getJSONObject(loop);
//                HashMap<String, Object> item = new HashMap<>();
//                String str3 = "recordName";
//                StringBuilder sb = new StringBuilder();
//                sb.append(Utilities.dateRT(singleObj.getString("CreatedDate")));
//                if (singleObj.getString(str2).isEmpty()) {
//                    str = "";
//                } else {
//                    StringBuilder sb2 = new StringBuilder();
//                    sb2.append(" : ");
//                    sb2.append(singleObj.getString(str2));
//                    str = sb2.toString();
//                }
//                sb.append(str);
//                item.put(str3, sb.toString());
//                item.put("recordImgPath", singleObj.getString("File"));
//                item.put("oldFile", Boolean.valueOf(true));
//                this.dataItems.add(item);
//            }
//        } catch (Exception e) {
//        }
//        AppComponents.reloadDataWithEmptyHint(this.vwRecords, this.dataAdapter, this.dataItems, getActivity().getResources().getString(C1291R.string.no_data_found));
//    }
//
//    public void onItemClick(int position, View v) {
//        HashMap hashMap = (HashMap) this.dataItems.get(position);
//        Utilities.showLoading(getContext());
//    }
//
//    /* access modifiers changed from: 0000 */
//    public void showUploadOptions() {
//        boolean shouldVisible = false;
//        int i = 0;
//        while (true) {
//            if (i >= this.dataItems.size()) {
//                break;
//            } else if (!((Boolean) ((HashMap) this.dataItems.get(i)).get("oldFile")).booleanValue()) {
//                shouldVisible = true;
//                break;
//            } else {
//                i++;
//            }
//        }
//        if (shouldVisible) {
//            this.updateBtn.setVisibility(0);
//        } else {
//            this.updateBtn.setVisibility(8);
//        }
//    }
//
//    public static void openFile(Context context, File url) throws IOException {
//        Uri uri = Uri.fromFile(url);
//        Intent intent = new Intent("android.intent.action.VIEW");
//        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
//            intent.setDataAndType(uri, "application/msword");
//        } else if (url.toString().contains(".pdf")) {
//            intent.setDataAndType(uri, "application/pdf");
//        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
//            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
//        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
//            intent.setDataAndType(uri, "application/x-wav");
//        } else if (url.toString().contains(".rtf")) {
//            intent.setDataAndType(uri, "application/rtf");
//        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
//            intent.setDataAndType(uri, "audio/x-wav");
//        } else if (url.toString().contains(".gif")) {
//            intent.setDataAndType(uri, MediaType.IMAGE_GIF);
//        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
//            intent.setDataAndType(uri, MediaType.IMAGE_JPEG);
//        } else if (url.toString().contains(".txt")) {
//            intent.setDataAndType(uri, "text/plain");
//        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//            intent.setDataAndType(uri, "video/*");
//        } else {
//            intent.setDataAndType(uri, MediaType.ALL);
//        }
//        intent.addFlags(ErrorDialogData.BINDER_CRASH);
//        context.startActivity(intent);
//    }
//}
