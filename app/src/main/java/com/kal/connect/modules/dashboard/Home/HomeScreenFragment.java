package com.kal.connect.modules.dashboard.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.kal.connect.R;
import com.kal.connect.customLibs.Callbacks.ItemHomeScreen;


public class HomeScreenFragment extends Fragment {

    CardView cv_shopping, cv_consultation, cv_appointments, cv_account;
    ItemHomeScreen itemHomeScreen;
    public HomeScreenFragment(ItemHomeScreen itemHomeScreen) {
        // Required empty public constructor
        this.itemHomeScreen = itemHomeScreen;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_screen, container, false);
        cv_shopping = v.findViewById(R.id.cv_shopping);
        cv_consultation = v.findViewById(R.id.cv_consultation);
        cv_appointments = v.findViewById(R.id.cv_appointments);
        cv_account = v.findViewById(R.id.cv_account);
        cv_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHomeScreen.onSelected(1);
            }
        });
        cv_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHomeScreen.onSelected(2);
            }
        });
        cv_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHomeScreen.onSelected(3);
            }
        });
        cv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHomeScreen.onSelected(4);
            }
        });

        return v;
    }
}