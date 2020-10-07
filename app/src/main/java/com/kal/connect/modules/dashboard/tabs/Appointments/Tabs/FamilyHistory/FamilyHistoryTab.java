package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.FamilyHistory;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.common.util.ArrayUtils;
import com.kal.connect.R;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FamilyHistoryTab extends Fragment implements View.OnClickListener{

    // MARK : UIElements
    View view;
//    TagGroup familyTags;

//    ScrollView contentsScroller;
//    LinearLayout familyLayout;
    ArrayList<HashMap<String, Object>> items = new ArrayList<>();
    FamilyHistoryAdapter familyHistoryAdapter;
    GridView familyGridView;
    Button updateBtn;


    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.family_history, container, false);
        buildUI();
        return view;

    }

    // MARK : Instance Methods
    private void buildUI() {

//        familyTags = (TagGroup) view.findViewById(R.id.familyTags);
//        contentsScroller = (ScrollView) view.findViewById(R.id.contentsScroller);
//        familyLayout = (LinearLayout) view.findViewById(R.id.familyLayout);

        familyGridView = (GridView) view.findViewById(R.id.family_gridview);
        updateBtn = (Button) view.findViewById(R.id.updatebutton);
        updateBtn.setOnClickListener(this);

        loadFamilyHistory();

        familyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = items.get(position);
                if((boolean)item.get("isSelected") == true)
                    item.put("isSelected",false);
                else
                    item.put("isSelected",true);

                items.set(position,item);
                familyHistoryAdapter.notifyDataSetChanged();
            }
        });

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.updatebutton:
                updateFamilyHistory();
                break;

        }

    }

    void updateFamilyHistory(){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        ArrayList<String> familySelected= new ArrayList<>();
        for(int i =0; i<items.size();i++){
            HashMap<String, Object> item = items.get(i);
            if((boolean)item.get("isSelected") == true)
            {
                familySelected.add(item.get("issueName").toString());
            }

        }
        String family = (familySelected.size()>0)?android.text.TextUtils.join(",", familySelected):"";
        inputParams.put("EMRSectionIndicator","2");
        inputParams.put("familyhistory",family);
        Utilities.getInstance().updateEMR(getContext(),inputParams);

    }

    // MARK : APIs
    private void loadFamilyHistory(){

        List<String> tags = new ArrayList<String>();
//        tags.add("Alcoholism");
//        tags.add("Anemia");
//        tags.add("Asthma");
//        tags.add("Depression");
//        tags.add("Diabetes");
        String [] issuesAry = new String [] {"Diabetes","Hypertension","Epilepsy","Asthma","Cancer","Anemia","Stroke","Blood Presure","Heart Disease","Obesity","Arthritis","Depression","Alzeheimer","Migraine Headache","Alcoholism","Drug","Smoking"};

        try{
            JSONArray familyHistory = GlobValues.getAppointmentCompleteDetails().getJSONArray("Family");
            final String [] selectedFamily = familyHistory.toString().replace("},{", " ,").split(" ");
            items.clear();
            for (int loop = 0; loop < issuesAry.length; loop++) {
//                tags.add(issuesAry[loop]);

                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("issueName",issuesAry[loop]);
                item.put("isSelected",false);
                if ( ArrayUtils.contains( selectedFamily, issuesAry[loop]) )
                {
                    item.put("isSelected",true);
                }
                items.add(item);

            }
            familyHistoryAdapter = new FamilyHistoryAdapter(getContext(),items);
            familyGridView.setAdapter(familyHistoryAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

//        familyTags.getTagAt(0).setChecked(true)
//        familyTags.setTags(tags);
    }
}