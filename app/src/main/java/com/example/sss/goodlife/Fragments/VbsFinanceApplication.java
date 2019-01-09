package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.BankAccountAdapter;
import com.example.sss.goodlife.Adapters.LocationAdapter;
import com.example.sss.goodlife.Adapters.PlaceAutocompleteAdapter;
import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.Models.BankAccountIdStatus;
import com.example.sss.goodlife.Models.BankAccountIds;
import com.example.sss.goodlife.Models.FinanceApplication;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.Locations;
import com.example.sss.goodlife.Models.ParticipantsList;
import com.example.sss.goodlife.Models.ProgramEventDates;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.Status;
import com.example.sss.goodlife.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VbsFinanceApplication extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    //Widgets
    private ImageButton VbsAddFinanceLayout;
    private TextView vendorDeleteVbs;
    private LinearLayout dynamicFinanceLayout,addMoreLayout;
    private EditText financeExpenditure,financeCompanyname,financeCompanyPhone
            ,financeTotalBidding,financeCompanyBankNum,financeCompanyBankIfsc;
    private Button submitVbsfinanceApplication,vbs_quotation_upload_button,addMoreVendorDetails;
    private ImageView vbs_upload_quotation_image;
    private Spinner finance_program_spinner,financeAccountType,financePaymentProcess;

    private ArrayList<EditText> selectedFinanceExpenditure=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyName=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyLocation=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyPhone=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyTotalBidding=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyBankNum=new ArrayList<>();
    private ArrayList<EditText> selectedFinanceCompanyBankIfsc=new ArrayList<>();
    private ArrayList<Spinner> selectedFinanceBankAccountTypes=new ArrayList<>();
    private ArrayList<ImageView> selectedFinanceQuataionImages=new ArrayList<>();
    private ArrayList<Spinner> selectedFinancePaymentType=new ArrayList<>();

    private ArrayList<String> ListselectedFinanceBankAccountTypes=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceTypeOfPayments=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceExpenditure=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyName=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyLocation=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyPhone=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyTotalBidding=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyBankNum=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceCompanyBankIfsc=new ArrayList<>();
    private ArrayList<String> ListselectedFinanceQuatationImages=new ArrayList<>();
    private ArrayList<String> ListselectedFinancePaymentType=new ArrayList<>();


    private int GALLERY = 1, CAMERA = 2;
    private boolean permissionCheck=false;
    private Bitmap bmp;

    //location variables
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136));
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUESTED_CODE = 1234;
    private GoogleApiClient googleApiClient;
    private Boolean mlocation_permission_granted = false;
    private AutoCompleteTextView financeCompanyLocation;

    //Api calls
    private ApiService apiService;
    private List<ProgramIds> programIds;
    private List<BankAccountIds> bankAccountIds;
    private String programId;
    private ProgramsIdAdapter programidsAdapter;
    private BankAccountAdapter bankAccountAdapter;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> paymentType=new ArrayList<>();
    private ProgressDialog progressDialog,financeSubmissionDialog;


    public VbsFinanceApplication() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Vendor Enroll");
        View view=inflater.inflate(R.layout.fragment_vbs_finance_application, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        financeSubmissionDialog=new ProgressDialog(getActivity());
        financeSubmissionDialog.setTitle("Vendor Enroll");
        financeSubmissionDialog.setMessage("Please wait, while we are submitting your details ");
        financeSubmissionDialog.setCanceledOnTouchOutside(false);

        //Initializing google api client
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        if (!permissionCheck){
            requestMultiplePermissions();
        }

        //initializing widgets
        VbsAddFinanceLayout=view.findViewById(R.id.VbsAddFinanceLayout);
        addMoreVendorDetails=view.findViewById(R.id.addMoreVendorDetails);
        dynamicFinanceLayout=view.findViewById(R.id.dynamicFinanceLayout);
        submitVbsfinanceApplication=view.findViewById(R.id.submitVbsfinanceApplication);
        finance_program_spinner=view.findViewById(R.id.finance_program_spinner);
        addMoreLayout=view.findViewById(R.id.addMoreLayout);



        //Spinner Dropdown for location
        apiService= APIUrl.getApiClient().create(ApiService.class);
        final retrofit2.Call<ProgramIdsStatus> call=apiService.getProgramids();
        call.enqueue(new Callback<ProgramIdsStatus>() {
            @Override
            public void onResponse(retrofit2.Call<ProgramIdsStatus> call, Response<ProgramIdsStatus> response) {
                if (response.body().getMessage().size()!=0) {
                    programIds = response.body().getMessage();
                    finance_program_spinner.setPrompt("Select Location");
                    programidsAdapter = new ProgramsIdAdapter(getActivity(), (ArrayList<ProgramIds>) programIds);
                    finance_program_spinner.setAdapter(programidsAdapter);
                    progressDialog.dismiss();

                    finance_program_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            programId= String.valueOf(programidsAdapter.getItem(position).getProgram_id());
                            addMoreLayout.setVisibility(View.VISIBLE);
                            addMoreVendorDetails.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {
                  progressDialog.dismiss();
                  Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ProgramIdsStatus> call, Throwable t) {
                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        paymentType.add("Cheque");
        paymentType.add("Cash");
        paymentType.add("Online");
        arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,paymentType);




        addMoreVendorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.dynamic_finance_layout, null);
                // Add the new row before the add field button.

                if (dynamicFinanceLayout.getChildCount()==0){
                    dynamicFinanceLayout.addView(rowView);
                }else {
                    if (TextUtils.isEmpty(financeExpenditure.getText().toString())){
                        financeExpenditure.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyname.getText().toString())){
                        financeCompanyname.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyLocation.getText().toString())){
                        financeCompanyLocation.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyPhone.getText().toString())){
                        financeCompanyPhone.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeTotalBidding.getText().toString())){
                        financeTotalBidding.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankNum.getText().toString())){
                        financeCompanyBankNum.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankIfsc.getText().toString())){
                        financeCompanyBankIfsc.setError("fields Cannot be Empty");
                        return;
                    }

                    dynamicFinanceLayout.addView(rowView);
                }

                financeExpenditure=rowView.findViewById(R.id.financeExpenditure);
                financeCompanyname=rowView.findViewById(R.id.financeCompanyname);
                financeCompanyLocation=rowView.findViewById(R.id.financeCompanyLocation);
                financeCompanyPhone=rowView.findViewById(R.id.financeCompanyPhone);
                financeTotalBidding=rowView.findViewById(R.id.financeTotalBidding);
                financeCompanyBankNum=rowView.findViewById(R.id.financeCompanyBankNum);
                financeCompanyBankIfsc=rowView.findViewById(R.id.financeCompanyBankIfsc);
                vendorDeleteVbs=rowView.findViewById(R.id.vendorDeleteVbs);
                financeAccountType=rowView.findViewById(R.id.financeAccountType);
                financePaymentProcess=rowView.findViewById(R.id.financePaymentProcess);
                vbs_upload_quotation_image=rowView.findViewById(R.id.vbs_upload_quotation_image);
                vbs_quotation_upload_button=rowView.findViewById(R.id.vbs_quotation_upload_button);

                progressDialog.show();
                //Spinner Dropdown for location
                apiService= APIUrl.getApiClient().create(ApiService.class);
                final retrofit2.Call<BankAccountIdStatus> call=apiService.getBankAccountNames();
                call.enqueue(new Callback<BankAccountIdStatus>() {
                    @Override
                    public void onResponse(retrofit2.Call<BankAccountIdStatus> call, Response<BankAccountIdStatus> response) {
                        if (response.body().getMessage().size()!=0) {
                            bankAccountIds = response.body().getMessage();
                            financeAccountType.setPrompt("Select Location");
                            bankAccountAdapter = new BankAccountAdapter(getActivity(), (ArrayList<BankAccountIds>) bankAccountIds);
                            financeAccountType.setAdapter(bankAccountAdapter);
                            progressDialog.dismiss();

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<BankAccountIdStatus> call, Throwable t) {
                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                financePaymentProcess.setAdapter(arrayAdapter);

                financeCompanyLocation.setOnItemClickListener(mAutoCompleteListener);
                autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                financeCompanyLocation.setAdapter(autocompleteAdapter);

                //uploading quotation image from the vendor
                vbs_quotation_upload_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPictureDialog();
                    }
                });

                selectedFinanceExpenditure.add(financeExpenditure);
                selectedFinanceCompanyName.add(financeCompanyname);
                selectedFinanceCompanyLocation.add(financeCompanyLocation);
                selectedFinanceCompanyPhone.add(financeCompanyPhone);
                selectedFinanceCompanyTotalBidding.add(financeTotalBidding);
                selectedFinanceCompanyBankNum.add(financeCompanyBankNum);
                selectedFinanceCompanyBankIfsc.add(financeCompanyBankIfsc);
                selectedFinanceBankAccountTypes.add(financeAccountType);
                selectedFinanceQuataionImages.add(vbs_upload_quotation_image);
                selectedFinancePaymentType.add(financePaymentProcess);


                vendorDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicFinanceLayout.removeView((View) v.getParent());
                        selectedFinanceExpenditure.remove(financeExpenditure);
                        selectedFinanceCompanyName.remove(financeCompanyname);
                        selectedFinanceCompanyLocation.remove(financeCompanyLocation);
                        selectedFinanceCompanyPhone.remove(financeCompanyPhone);
                        selectedFinanceCompanyTotalBidding.remove(financeTotalBidding);
                        selectedFinanceCompanyBankNum.remove(financeCompanyBankNum);
                        selectedFinanceCompanyBankIfsc.remove(financeCompanyBankIfsc);
                        selectedFinanceBankAccountTypes.remove(financeAccountType);
                        selectedFinanceQuataionImages.remove(vbs_upload_quotation_image);
                        selectedFinancePaymentType.remove(financePaymentProcess);

                    }
                });
            }
        });

        VbsAddFinanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.dynamic_finance_layout, null);
                // Add the new row before the add field button.

                if (dynamicFinanceLayout.getChildCount()==0){
                    dynamicFinanceLayout.addView(rowView);
                }else {
                    if (TextUtils.isEmpty(financeExpenditure.getText().toString())){
                        financeExpenditure.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyname.getText().toString())){
                        financeCompanyname.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyLocation.getText().toString())){
                        financeCompanyLocation.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyPhone.getText().toString())){
                        financeCompanyPhone.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeTotalBidding.getText().toString())){
                        financeTotalBidding.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankNum.getText().toString())){
                        financeCompanyBankNum.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankIfsc.getText().toString())){
                        financeCompanyBankIfsc.setError("fields Cannot be Empty");
                        return;
                    }

                    dynamicFinanceLayout.addView(rowView);
                }

                financeExpenditure=rowView.findViewById(R.id.financeExpenditure);
                financeCompanyname=rowView.findViewById(R.id.financeCompanyname);
                financeCompanyLocation=rowView.findViewById(R.id.financeCompanyLocation);
                financeCompanyPhone=rowView.findViewById(R.id.financeCompanyPhone);
                financeTotalBidding=rowView.findViewById(R.id.financeTotalBidding);
                financeCompanyBankNum=rowView.findViewById(R.id.financeCompanyBankNum);
                financeCompanyBankIfsc=rowView.findViewById(R.id.financeCompanyBankIfsc);
                financeAccountType=rowView.findViewById(R.id.financeAccountType);
                financePaymentProcess=rowView.findViewById(R.id.financePaymentProcess);
                vendorDeleteVbs=rowView.findViewById(R.id.vendorDeleteVbs);
                vbs_upload_quotation_image=rowView.findViewById(R.id.vbs_upload_quotation_image);
                vbs_quotation_upload_button=rowView.findViewById(R.id.vbs_quotation_upload_button);

                progressDialog.show();
                //Spinner Dropdown for location
                apiService= APIUrl.getApiClient().create(ApiService.class);
                final retrofit2.Call<BankAccountIdStatus> call=apiService.getBankAccountNames();
                call.enqueue(new Callback<BankAccountIdStatus>() {
                    @Override
                    public void onResponse(retrofit2.Call<BankAccountIdStatus> call, Response<BankAccountIdStatus> response) {
                        if (response.body().getMessage().size()!=0) {
                            bankAccountIds = response.body().getMessage();
                            financeAccountType.setPrompt("Select Location");
                            bankAccountAdapter = new BankAccountAdapter(getActivity(), (ArrayList<BankAccountIds>) bankAccountIds);
                            financeAccountType.setAdapter(bankAccountAdapter);
                            progressDialog.dismiss();

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<BankAccountIdStatus> call, Throwable t) {
                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

                financePaymentProcess.setAdapter(arrayAdapter);

                //AutoCompleteTextViews from google places api
                financeCompanyLocation.setOnItemClickListener(mAutoCompleteListener);
                autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                financeCompanyLocation.setAdapter(autocompleteAdapter);

                //uploading quotation image from the vendor
                vbs_quotation_upload_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPictureDialog();
                    }
                });

                selectedFinanceExpenditure.add(financeExpenditure);
                selectedFinanceCompanyName.add(financeCompanyname);
                selectedFinanceCompanyLocation.add(financeCompanyLocation);
                selectedFinanceCompanyPhone.add(financeCompanyPhone);
                selectedFinanceCompanyTotalBidding.add(financeTotalBidding);
                selectedFinanceCompanyBankNum.add(financeCompanyBankNum);
                selectedFinanceCompanyBankIfsc.add(financeCompanyBankIfsc);
                selectedFinanceBankAccountTypes.add(financeAccountType);
                selectedFinancePaymentType.add(financePaymentProcess);




                vendorDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicFinanceLayout.removeView((View) v.getParent());
                        selectedFinanceExpenditure.remove(financeExpenditure);
                        selectedFinanceCompanyName.remove(financeCompanyname);
                        selectedFinanceCompanyLocation.remove(financeCompanyLocation);
                        selectedFinanceCompanyPhone.remove(financeCompanyPhone);
                        selectedFinanceCompanyTotalBidding.remove(financeTotalBidding);
                        selectedFinanceCompanyBankNum.remove(financeCompanyBankNum);
                        selectedFinanceCompanyBankIfsc.remove(financeCompanyBankIfsc);
                        selectedFinanceBankAccountTypes.remove(financeAccountType);
                        selectedFinancePaymentType.remove(financePaymentProcess);

                        if (bmp!=null)
                        ListselectedFinanceQuatationImages.remove(imageToString(bmp));

                    }
                });
            }
        });

        submitVbsfinanceApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(financeExpenditure.getText().toString())){
                    financeExpenditure.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyname.getText().toString())){
                    financeCompanyname.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyLocation.getText().toString())){
                    financeCompanyLocation.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyPhone.getText().toString())){
                    financeCompanyPhone.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeTotalBidding.getText().toString())){
                    financeTotalBidding.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyBankNum.getText().toString())){
                    financeCompanyBankNum.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyBankIfsc.getText().toString())){
                    financeCompanyBankIfsc.setError("fields Cannot be Empty");
                    return;
                }
                if (bmp==null){
                    Toast.makeText(getActivity(),"please upload quotation image",Toast.LENGTH_SHORT).show();
                    return;
                }

                ListselectedFinanceExpenditure.clear();
                ListselectedFinanceCompanyName.clear();
                ListselectedFinanceCompanyLocation.clear();
                ListselectedFinanceCompanyPhone.clear();
                ListselectedFinanceCompanyTotalBidding.clear();
                ListselectedFinanceCompanyBankNum.clear();
                ListselectedFinanceCompanyBankIfsc.clear();
                ListselectedFinanceBankAccountTypes.clear();
                ListselectedFinanceTypeOfPayments.clear();

                    for (int i=0;i<selectedFinanceExpenditure.size();i++){
//                        Log.e("FinanceExpenditure",selectedFinanceExpenditure.get(i).getText().toString());
//                        Log.e("financeCompanyname",selectedFinanceCompanyName.get(i).getText().toString());
//                        Log.e("financeCompanyLocation",selectedFinanceCompanyLocation.get(i).getText().toString());
//                        Log.e("financeCompanyPhone",selectedFinanceCompanyPhone.get(i).getText().toString());
//                        Log.e("financeTotalBidding",selectedFinanceCompanyTotalBidding.get(i).getText().toString());
//                        Log.e("financeCompanyBankNum",selectedFinanceCompanyBankNum.get(i).getText().toString());
//                        Log.e("financeCompanyBankIfsc",selectedFinanceCompanyBankIfsc.get(i).getText().toString());

                        ListselectedFinanceExpenditure.add(selectedFinanceExpenditure.get(i).getText().toString());
                        ListselectedFinanceCompanyName.add(selectedFinanceCompanyName.get(i).getText().toString());
                        ListselectedFinanceCompanyLocation.add(selectedFinanceCompanyLocation.get(i).getText().toString());
                        ListselectedFinanceCompanyPhone.add(selectedFinanceCompanyPhone.get(i).getText().toString());
                        ListselectedFinanceCompanyTotalBidding.add(selectedFinanceCompanyTotalBidding.get(i).getText().toString());
                        ListselectedFinanceCompanyBankNum.add(selectedFinanceCompanyBankNum.get(i).getText().toString());
                        ListselectedFinanceCompanyBankIfsc.add(selectedFinanceCompanyBankIfsc.get(i).getText().toString());
                        ListselectedFinanceBankAccountTypes.add(bankAccountAdapter.getItem(financeAccountType.getSelectedItemPosition()).getBank_id());
                        ListselectedFinanceTypeOfPayments.add(selectedFinancePaymentType.get(i).getSelectedItem().toString());


                        Log.e("FinanceExpenditure",ListselectedFinanceExpenditure.toString());
                        Log.e("financeCompanyname",ListselectedFinanceCompanyName.toString());
                        Log.e("financeCompanyLocation",ListselectedFinanceCompanyLocation.toString());
                        Log.e("financeCompanyPhone",ListselectedFinanceCompanyPhone.toString());
                        Log.e("financeTotalBidding",ListselectedFinanceCompanyTotalBidding.toString());
                        Log.e("financeCompanyBankNum",ListselectedFinanceCompanyBankNum.toString());
                        Log.e("financeCompanyBankIfsc",ListselectedFinanceCompanyBankIfsc.toString());
                        Log.e("BankAccountTypes",ListselectedFinanceBankAccountTypes.toString());
                        Log.e("QuatationImages",ListselectedFinanceQuatationImages.toString());
                        Log.e("TypeOfPayments",ListselectedFinanceTypeOfPayments.toString());
                    }

                FinanceApplication financeList =new FinanceApplication(
                        ListselectedFinanceCompanyName,
                        ListselectedFinanceCompanyLocation,
                        ListselectedFinanceCompanyPhone,
                        ListselectedFinanceCompanyTotalBidding,
                        ListselectedFinanceBankAccountTypes,
                        ListselectedFinanceTypeOfPayments,
                        ListselectedFinanceCompanyBankNum,
                        ListselectedFinanceCompanyBankIfsc,
                        ListselectedFinanceExpenditure,
                        ListselectedFinanceQuatationImages);

                Gson gson = new Gson();
                String finan_list = gson.toJson(financeList);
                Log.e("program id ",programId);
                Log.e("participants ",finan_list);


                financeSubmissionDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> financeCall=apiService.financeSubmission(
                        programId,
                        finan_list);
                financeCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            financeSubmissionDialog.dismiss();
                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        financeSubmissionDialog.dismiss();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment fragment=new HomeFragment();
                        transaction.replace(R.id.frameContainer, fragment);
                        transaction.commit();

                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        financeSubmissionDialog.dismiss();
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        return view;
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        if (permissionCheck){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);
        }else {
            requestMultiplePermissions();
        }

    }

    private void takePhotoFromCamera() {
        if (permissionCheck){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }else {
            requestMultiplePermissions();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                     bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    vbs_upload_quotation_image.setImageBitmap(bmp);
                    ListselectedFinanceQuatationImages.add(imageToString(bmp));

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
             bmp = (Bitmap) data.getExtras().get("data");
            vbs_upload_quotation_image.setImageBitmap(bmp);
            ListselectedFinanceQuatationImages.add(imageToString(bmp));
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

        }
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCheck=true;
                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                            permissionCheck=false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    //upload images
    public String  imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }

    //setting location places to editText
    private AdapterView.OnItemClickListener mAutoCompleteListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final AutocompletePrediction item=autocompleteAdapter.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi
                    .getPlaceById(googleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallBack);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallBack=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG,"onresult : place Query did not complete Successfully  "+places.getStatus().toString());

                //you can use lat with qLoc.latitude;
                //and long with qLoc.longitude;

                places.release();
                return;
            }
            final Place mPlace = places.get(0);
            LatLng qLoc = mPlace.getLatLng();
            Toast.makeText(getActivity(),"location "+qLoc,Toast.LENGTH_LONG).show();
            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
    @Override
    public void onStart(){
        super.onStart();
        if(googleApiClient != null){
            googleApiClient.connect();
        }
    }

}
