package com.kal.connect.modules.dashboard.tabs.Appointments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentStatePagerAdapter;

//import com.patientapp.modules.dashboard.tabs.Appointments.Tabs.Examination.PatientExamination;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.PatientDetails;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.PatientExamination;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Prescription.PrescriptionTab;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.PresentCompliant.PresentCompliantTab;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Records.RecordsTab;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Vitals.VitalsTab;

public class TabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Fragment[] pages = new Fragment[10];

    public TabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


//        // To Stop reloading fragments all the time
//        pages[0] = new PrescriptionTab();
//        pages[1] = new RecordsTab();
//        pages[2] = new SemiAnalyzerTab();
//        pages[3] = new BloodInvestigationTab();
//        pages[4] = new CholestrolTab();
////        pages[5] = new UrineAnalyzerTab();
//        pages[5] = new FamilyHistoryTab();
//        pages[6] = new BloodGlucoseTab();
//        pages[7] = new VitalsTab();
//        pages[8] = new PresentCompliantTab();
//        pages[9] = new ECG();

// To Stop reloading fragments all the time





//        pages[5] = new UrineAnalyzerTab();



//        pages[0] = new VitalsTab();
//        pages[1] = new ECG();
//        pages[2] = new RecordsTab();
//        pages[3] = new PrescriptionTab();
//        pages[4] = new BloodGlucoseTab();
//        pages[5] = new BloodInvestigationTab();
//        pages[6] = new CholestrolTab();
//        pages[7] = new SemiAnalyzerTab();
//        pages[8] = new PresentCompliantTab();
//        pages[9] = new FamilyHistoryTab();

        pages[0] = new PresentCompliantTab();
//        pages[0] = new PatientExamination();
        pages[1] = new PatientDetails();
        pages[2] = new VitalsTab();
        pages[3] = new RecordsTab();
        pages[4] = new PatientExamination();
        pages[5] = new PrescriptionTab();




    }

    @Override
    public Fragment getItem(int position) {

        if (pages[position] != null) {
            return pages[position];
        }
        return null;

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
