package com.kal.connect.modules.hospitals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kal.connect.R;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutHospital extends CustomActivity {

    @BindView(R.id.hospital_name)
    TextView hospitalName;

    @BindView(R.id.hospital_image)
    ImageView hospitalImage;

    @BindView(R.id.description_text)
    TextView description;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.diagnostic_vw)
    CardView diagnosticVW;
    @BindView(R.id.diagnostic_name)
    TextView diagnosticName;
    @BindView(R.id.diagnostic_phone)
    TextView diagnosticPhone;

    @BindView(R.id.pharmacy)
    CardView pharmacyVW;
    @BindView(R.id.pharmacy_name)
    TextView pharmacyName;
    @BindView(R.id.pharmacy_phone)
    TextView pharmacyPhone;

    @BindView(R.id.ambulance)
    CardView ambulanceVW;
    @BindView(R.id.ambulance_name)
    TextView ambulanceName;
    @BindView(R.id.ambulance_phone)
    TextView ambulancePhone;

    @BindView(R.id.techninician_vw)
    CardView technicianVw;
    @BindView(R.id.tech_name)
    TextView techName;
    @BindView(R.id.tech_phone)
    TextView techPhone;

    @BindView(R.id.payment_cost)
    TextView paymentCost;

    @OnClick(R.id.open_map)
    void openMap(){
        try{
            if(!Utilities.selectedHospitalModel.getHospitalLat().isEmpty()){
                String uri = "http://maps.google.com/maps?q=loc:"+ Utilities.selectedHospitalModel.getHospitalLat()+","+Utilities.selectedHospitalModel.getHospitalLong();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_hospitals);
        ButterKnife.bind(this);
        buildUI();
    }

    public void buildUI() {
        HospitalModel hospitalModel = Utilities.selectedHospitalModel;
        hospitalName.setText(hospitalModel.getHospitalName());
        if (hospitalModel.getHospitalDescription().isEmpty())
        {
            description.setVisibility(View.GONE);
        }else{
            description.setVisibility(View.VISIBLE);
            description.setText(hospitalModel.getHospitalDescription());
        }
        address.setText(hospitalModel.getCityName()+", "+hospitalModel.getStateName()+", "+hospitalModel.getZipCode());

        Utilities.loadImageWithPlaceHoler(hospitalImage,hospitalModel.getHospitaImage(),hospitalModel.getHospitalName());

        if(hospitalModel.getIsDiagnostic()){
            diagnosticName.setText(hospitalModel.getDiagnosticName());
            diagnosticPhone.setText(hospitalModel.getDiagnosticPhone());
            diagnosticVW.setVisibility(View.VISIBLE);
        }else{
            diagnosticVW.setVisibility(View.GONE);
        }

        if(hospitalModel.getIsPharmacy()){
            pharmacyName.setText(hospitalModel.getPharmacyName());
            pharmacyPhone.setText(hospitalModel.getPharmacyPhone());
            diagnosticVW.setVisibility(View.VISIBLE);
        }else{
            pharmacyVW.setVisibility(View.GONE);
        }

        if(hospitalModel.getIsAmbulance()){
            ambulanceName.setText(hospitalModel.getAmbulanceName());
            ambulancePhone.setText(hospitalModel.getAmbulancePhone());
            ambulanceVW.setVisibility(View.VISIBLE);
        }else{
            ambulanceVW.setVisibility(View.GONE);
        }

        if(hospitalModel.getIsTechnician()){
            techName.setText(hospitalModel.getTechnicianName());
            techPhone.setText(hospitalModel.getTechnicianPhone());
            technicianVw.setVisibility(View.VISIBLE);
        }else{
            technicianVw.setVisibility(View.GONE);
        }

        paymentCost.setText(hospitalModel.getChargesDetails().getHospitalCharges()+" INR");



    }

}
