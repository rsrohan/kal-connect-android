package com.kal.connect.modules.dashboard.tabs.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.adapters.SelectedIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.IssueHeader;
import com.kal.connect.models.Issues;
import com.kal.connect.modules.payment.PaymentActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class Home extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    RecyclerView issuesRecyclerView, selectedRecyclerView;

    SearchView searchView;

    private SectionedRecyclerViewAdapter sectionAdapter;
    private SelectedIssueAdapter selectedIssueAdapter;
    //    Button proceedBtn;
    private CardView issuesSelectedCard;
    EditText addComplaints;

    IssuesScections issuesScection;
    RelativeLayout header;
    LinearLayout proceedBtn;


    private ArrayList<Issues> selectedIssuesList = new ArrayList<>();
    private ArrayList<String> selectedIssuesListId = new ArrayList<>();


    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    public void checkUsername() {

    }

    public void setupInitialIssuesData(JSONArray issueDetailsAry) {
        try {
            for (int i = 0; i < issueDetailsAry.length(); i++) {
                JSONObject issueData = issueDetailsAry.getJSONObject(i);

                IssueHeader section = new IssueHeader(issueData.getString("CategoryName"));

                JSONArray issueItemsAry = issueData.getJSONArray("Complaint");
                List<Issues> issueItems = new ArrayList<>();
                for (int j = 0; j < issueItemsAry.length(); j++) {
                    JSONObject singleItemObj = issueItemsAry.getJSONObject(j);
                    issueItems.add(new Issues(singleItemObj.getString("ComplaintID"), singleItemObj.getString("ComplaintText"), 0));
                }

                issuesScection = new IssuesScections(section, issueItems);
                sectionAdapter.addSection(issuesScection);
            }
        } catch (Exception e) {

        }

//        issuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        issuesRecyclerView.setAdapter(sectionAdapter);
        updateSelectedIssue();
    }


    public JSONArray createSampleData() {

        String jsonStr = "[{'title':'Genral Issues','options':[{'title':'Fever','id':'001'},{'title':'Cold','id':'001'},{'title':'Cough','id':'001'}]},{'title':'Heart Issues','options':[{'title':'Attack 001','id':'001'},{'title':'Chest Pain','id':'001'},{'title':'Heart failure','id':'001'},{'title':'Arrhythmia','id':'001'},{'title':'Valve disease','id':'001'}]}]";
        try {
            JSONArray jsonAry = new JSONArray(jsonStr);
            return jsonAry;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_issues, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {

        for (Section section : sectionAdapter.getCopyOfSectionsMap().values()) {
            if (section instanceof FilterableSection) {
                ((FilterableSection) section).filter(query);
            }
        }
        sectionAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    //    public void onViewCreated(View view, Bundle savedInstanceState) {
////        final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                Utilities.showAlertDialogWithOptions(getActivity(), getResources().getString(R.string.alert_map_options), new String[]{getResources().getString(R.string.btn_yes), getResources().getString(R.string.cancel)}, new UtilitiesInterfaces.AlertCallback() {
////                    @Override
////                    public void onOptionClick(DialogInterface dialog, int buttonIndex) {
////                        if(buttonIndex == 0)
////                        {
////                            launchPlacePicker();
////                        }
////                        if(buttonIndex == 1)
////                        {
////
////                        }
////                    }
////                });
////            }
////        }, 2000);
//
//
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home, container, false);

        issuesSelectedCard = (CardView) v.findViewById(R.id.issuesSelectedCard);
        issuesRecyclerView = (RecyclerView) v.findViewById(R.id.issuesRecyclerVW);
        selectedRecyclerView = (RecyclerView) v.findViewById(R.id.issuesSelectedRecyclerVW);
        proceedBtn = (LinearLayout) v.findViewById(R.id.proceed_btn);
        addComplaints = (EditText) v.findViewById(R.id.addComplaints);


        searchView = (SearchView) v.findViewById(R.id.search_txt_vw);
        //Turn iconified to false:
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        //To remove the keyboard, but make sure you keep the expanded version:
        searchView.clearFocus();
        header = (RelativeLayout) v.findViewById(R.id.header);


        proceedBtn.setOnClickListener(this);

//        KeyboardVisibilityEvent.setEventListener(
//                (Activity)getContext(),
//                new KeyboardVisibilityEventListener() {
//                    @Override
//                    public void onVisibilityChanged(boolean isOpen) {
//                        // some code depending on keyboard visiblity status
//                        if(isOpen)
//                            header.setVisibility(View.GONE);
//                        else
//                            header.setVisibility(View.VISIBLE);
//                    }
//                });


        //SelectedRecy

        // add a divider after each item for more clarity
//        selectedRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        selectedIssueAdapter = new SelectedIssueAdapter(selectedIssuesList, getContext(), Home.this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        selectedRecyclerView.setLayoutManager(horizontalLayoutManager);
        selectedRecyclerView.setAdapter(selectedIssueAdapter);

        //

        // 2. set layoutManger
//        issuesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        issuesRecyclerView.setLayoutManager(glm);
//        recyclerView.setAdapter(sectionAdapter);
        sectionAdapter = new SectionedRecyclerViewAdapter();
//        setupInitialIssuesData(createSampleData());
        getIssuesList();

        addComplaints.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0 || selectedIssuesList.size() > 0) {
                    proceedBtn.setVisibility(View.VISIBLE);
                } else {
                    proceedBtn.setVisibility(View.GONE);
                }
            }
        });

        return v;//inflater.inflate(R.layout.select_issues, container, false);

    }

    public void updateSelectedIssue() {

//        selectedIssuesList.add(new Issues("11","Fever",0));
//        selectedIssuesList.add(new Issues("11","Cough",0));
        selectedIssueAdapter.notifyDataSetChanged();
        sectionAdapter.notifyDataSetChanged();

        if (selectedIssuesList.size() > 0) {
            issuesSelectedCard.setVisibility(View.VISIBLE);
            proceedBtn.setVisibility(View.VISIBLE);
        } else {
            issuesSelectedCard.setVisibility(View.GONE);
            proceedBtn.setVisibility(View.GONE);
        }

    }

    public void removeSelectedIssue(int pos) {
        selectedIssuesList.remove(pos);
        updateSelectedIssue();
    }

    void checkPayment() {
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.proceed_btn) {
//            Intent intent = new Intent(getContext(), IssueDescriptor.class);

//            if(true){
//                checkPayment();
//                return;
//            }

//            if (true) {
//                Intent videoIntent = new Intent(getActivity(), VideoConference.class);
//                videoIntent.putExtra("CALL_TYPE", 1);
//                startActivity(videoIntent);
//                return;
//            }


            Intent intent = new Intent(getContext(), PatientHistory.class);
            intent.putExtra("SelectedIssues", selectedIssuesList);

            GlobValues.getInstance().setSelectedIssuesList(selectedIssuesList);
            GlobValues.getInstance().setupAddAppointmentParams();
            if (!addComplaints.getText().toString().trim().isEmpty()) {
                selectedIssuesListId.add(addComplaints.getText().toString().trim());
                selectedIssuesList.add(new Issues("10001", addComplaints.getText().toString().trim(), 1));
                intent.putExtra("NewComplaints", addComplaints.getText().toString().trim());
                GlobValues.getInstance().addAppointmentInputParams("NewComplaints", addComplaints.getText().toString().trim());
            }

            GlobValues.getInstance().addAppointmentInputParams("PresentComplaint", android.text.TextUtils.join(",", selectedIssuesListId));
            GlobValues.getInstance().addAppointmentInputParams("ComplaintID", "0");
            setAppointmentParams();

            startActivity(intent);
            Utilities.pushAnimation(getActivity());

        }

    }

    void setAppointmentParams() {
        try {
            GlobValues g = GlobValues.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String selectedTime = Utilities.getCurrentTime("hh:mm");
            g.addAppointmentInputParams("AppointmentDate", Utilities.getCurrentDate("MM/dd/yyyy"));
            g.addAppointmentInputParams("AppointmentTime", selectedTime = Utilities.getCurrentTime("hh:mm"));
            g.addAppointmentInputParams("Offset", "-330");
            g.addAppointmentInputParams("ConsultationMode", "Video Conference");
            g.addAppointmentInputParams("DoctorRole", "1");
//            g.addAppointmentInputParams("isTechnician", "0");
//            g.addAppointmentInputParams("PatLoc", "");
//            g.addAppointmentInputParams("Lattitude", "");
//            g.addAppointmentInputParams("Longitude", "");
            g.addAppointmentInputParams("isInstant", "" + "1");
//            g.addAppointmentInputParams("ConsultNow", "1");
            g.addAppointmentInputParams("ComplaintDescp", "");
            g.addAppointmentInputParams("ClientID", AppPreferences.getInstance().getUserInfo().getString("ClientID"));
        } catch (Exception e) {
        }

    }

    private class IssuesScections extends StatelessSection implements FilterableSection {

        IssueHeader section;
        List<Issues> issueItems;
        List<Issues> filteredList;

        public IssuesScections(IssueHeader section, List<Issues> issueItems) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.issue_item)
                    .headerResourceId(R.layout.issue_header)
                    .build());
            this.section = section;
            this.issueItems = issueItems;
            this.filteredList = new ArrayList<>(issueItems);
        }


        @Override

        public int getContentItemsTotal() {
            return filteredList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final String name = filteredList.get(position).getTitle();

            // Selected Fields
            if (selectedIssuesList.contains(filteredList.get(position))) {
                // itemHolder.issueTxtVw.setTextColor(ContextCompat.getColor(getContext(), R.color.bio_red));
                itemHolder.issueIconVw.setImageResource(R.drawable.icon_symptom_selected);
                itemHolder.cardBG.setCardBackgroundColor(getResources().getColor(R.color.issue_selected));
            }
            // Non Selected fields
            else {
                // itemHolder.issueTxtVw.setTextColor(ContextCompat.getColor(getContext(), R.color.bio_grey));
                itemHolder.issueIconVw.setImageResource(R.drawable.icon_grey_checkup);
                itemHolder.cardBG.setCardBackgroundColor(getResources().getColor(R.color.issue_not_selected));
            }


            itemHolder.issueTxtVw.setText(name);
            ((ItemViewHolder) holder).rootView.setTag(position);
            ((ItemViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedIssuesList.contains(filteredList.get((int) v.getTag()))) {
                        selectedIssuesList.remove(filteredList.get((int) v.getTag()));
                        selectedIssuesListId.remove(filteredList.get((int) v.getTag()).getTitle());
                        updateSelectedIssue();
                    } else {
                        selectedIssuesList.add(filteredList.get((int) v.getTag()));
                        selectedIssuesListId.add(filteredList.get((int) v.getTag()).getTitle());
                        updateSelectedIssue();
                    }
                    if (selectedIssuesList.size() > 0)
                        selectedRecyclerView.scrollToPosition(selectedIssuesList.size() - 1);

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {

            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;


            headerHolder.issueTitleTxtVw.setText(section.getIssueHeadertitle());


        }

        @Override
        public void filter(String query) {
            if (TextUtils.isEmpty(query)) {
                filteredList = new ArrayList<>(issueItems);
                this.setVisible(true);
            } else {
                filteredList.clear();
                for (Issues value : issueItems) {
                    if (value.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(value);
                    }
                }

                this.setVisible(!filteredList.isEmpty());
            }
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {

            private final TextView issueTitleTxtVw;


            HeaderViewHolder(View view) {
                super(view);

                issueTitleTxtVw = (TextView) view.findViewById(R.id.issue_header_txt_vw);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            private final View rootView;
            //            private final ImageView imgItem;
            private final TextView issueTxtVw;
            private final ImageView issueIconVw;
            private CardView cardBG;

            ItemViewHolder(View view) {
                super(view);

                rootView = view;
//                imgItem = (ImageView) view.findViewById(R.id.imgItem);
                issueTxtVw = (TextView) view.findViewById(R.id.issue_txt_vw);
                cardBG = (CardView) view.findViewById(R.id.issue_card);
                issueIconVw = (ImageView) view.findViewById(R.id.issue_icon);
            }
        }
    }

    interface FilterableSection {
        void filter(String query);
    }

    void getIssuesList() {
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
//        inputParams.put("LanguageIndicator",AppPreferences.getInstance().retrieveLanguage());


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(getContext(), commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(getContext(), "Please check again!", false);
                            }
                            return;

                        }
//                        JSONArray cityAry = commonDataInfo.getJSONArray("City");
//                        JSONArray stateAry = commonDataInfo.getJSONArray("State");
//
//                        GlobValues.setCityAry(cityAry);
//                        GlobValues.setStateAry(stateAry);

                        setupInitialIssuesData(commonDataInfo.getJSONArray("Complaint"));

                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_ISSUES_DATA, "POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        } else {

        }
    }


}
