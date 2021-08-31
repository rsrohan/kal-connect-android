package com.kal.connect.adapters.healthseeker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F0Profile;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F10Symptoms;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F11FamilyHistory;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F12Consent;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F1Questions;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F2DescribeIssue;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F3PastIssuesMedicalCondition;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F4AboutMedications;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F5PastDiseaseSurgeryHospitalization;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F6ScaleQuestions;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F7ScaleQuestions;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F8Habits;
import com.kal.connect.modules.dashboard.BookAppointment.healthseeker.F9PersonalPreference;
import com.koushikdutta.ion.builder.Builders;

public class HealthSeekerTabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Fragment[] pages = new Fragment[20];
    public static F0Profile f0Profile = new F0Profile();
    public static F1Questions f1Questions = new F1Questions();
    public static F2DescribeIssue f2DescribeIssue = new F2DescribeIssue();
    public static F3PastIssuesMedicalCondition f3PastIssuesMedicalCondition = new F3PastIssuesMedicalCondition();
    public static F4AboutMedications f4AboutMedications = new F4AboutMedications();
    public static F5PastDiseaseSurgeryHospitalization f5PastDiseaseSurgeryHospitalization = new F5PastDiseaseSurgeryHospitalization();
    public static F6ScaleQuestions f6ScaleQuestions = new F6ScaleQuestions();
    public static F7ScaleQuestions f7ScaleQuestions = new F7ScaleQuestions();
    public static F8Habits f8Habits = new F8Habits();
    public static F9PersonalPreference f9PersonalPreference = new F9PersonalPreference();
    public static F10Symptoms f10Symptoms = new F10Symptoms();
    public static F11FamilyHistory f11FamilyHistory = new F11FamilyHistory();
    public static F12Consent f12Consent = new F12Consent();

    public HealthSeekerTabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        pages[0] = f0Profile;
        pages[1] = f1Questions;
        pages[2] =  f2DescribeIssue;
        pages[3] =  f3PastIssuesMedicalCondition;
        pages[4] =  f4AboutMedications;
        pages[5] =  f5PastDiseaseSurgeryHospitalization;
        pages[6] =  f6ScaleQuestions;
        pages[7] =  f7ScaleQuestions;
        pages[8] =  f8Habits;
        pages[9] =  f9PersonalPreference;
        pages[10] =  f10Symptoms;
        pages[11] =  f11FamilyHistory;
        pages[12] =  f12Consent;




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
