package com.kal.connect.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentStatePagerAdapter;

//import com.patientapp.modules.dashboard.tabs.Appointments.Tabs.Examination.PatientExamination;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.PatientDetails;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.PatientExamination;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Prescription.PrescriptionTab;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.PresentCompliant.PresentCompliantTab;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Records.RecordsTab;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Vitals.VitalsTab;

public class TabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Fragment[] pages = new Fragment[10];

    public TabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;



        pages[0] = new PrescriptionTab();
        pages[1] = new PresentCompliantTab();
        pages[2] = new PatientDetails();
        pages[3] = new VitalsTab();
        pages[4] = new RecordsTab();
        pages[5] = new PatientExamination();




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
