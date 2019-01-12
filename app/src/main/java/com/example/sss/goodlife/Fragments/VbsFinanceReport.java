package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Adapters.VendorListAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.VendorDataFromDb;
import com.example.sss.goodlife.Models.VendorDataModel;
import com.example.sss.goodlife.R;
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

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class VbsFinanceReport extends Fragment {

    private TextView financeGetDetails;
    private Spinner financeReportSpinner;
    private RecyclerView financeRecyclerView;



    private int GALLERY = 1, CAMERA = 2;
    private boolean permissionCheck=false;
    private Bitmap bmp;

    //Api calls
    private ApiService apiService;
    private List<ProgramIds> programIds;
    private List<VendorDataModel> vendorList;
    private String programId,locationid;
    private ProgramsIdAdapter programidsAdapter;
    private VendorListAdapter vendorListAdapter;
    private ProgressDialog progressDialog;

    public VbsFinanceReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Finance Report");
        View view= inflater.inflate(R.layout.fragment_vbs_finance_report, container, false);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        financeGetDetails=view.findViewById(R.id.financeGetDetails);
        financeReportSpinner=view.findViewById(R.id.financeReportSpinner);
        financeRecyclerView=view.findViewById(R.id.financeRecyclerView);



        //Spinner Dropdown for ProgramNames
        apiService= APIUrl.getApiClient().create(ApiService.class);
        final retrofit2.Call<ProgramIdsStatus> call=apiService.getProgramids();
        call.enqueue(new Callback<ProgramIdsStatus>() {
            @Override
            public void onResponse(retrofit2.Call<ProgramIdsStatus> call, Response<ProgramIdsStatus> response) {
                if (response.body()==null){
                    Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().getMessage().size()!=0) {
                    programIds = response.body().getMessage();
                    financeReportSpinner.setPrompt("Select Location");
                    programidsAdapter = new ProgramsIdAdapter(getActivity(), (ArrayList<ProgramIds>) programIds);
                    financeReportSpinner.setAdapter(programidsAdapter);
                    progressDialog.dismiss();

                    financeReportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            programId= String.valueOf(programidsAdapter.getItem(position).getProgram_id());
                            locationid= String.valueOf(programidsAdapter.getItem(position).getLocation_id());
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

        financeGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Getting Data");
                progressDialog.setMessage("Please wait...,");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                retrofit2.Call<VendorDataFromDb> call=apiService.vendorData(programId,locationid);
                call.enqueue(new Callback<VendorDataFromDb>() {
                    @Override
                    public void onResponse(Call<VendorDataFromDb> call, Response<VendorDataFromDb> response) {
                        if (response.body()==null){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"respomce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.dismiss();
                        vendorList=response.body().getMessage();
                        Log.e("vender data",vendorList.toString());
                        vendorListAdapter=new VendorListAdapter(getActivity(),vendorList,programId,locationid);
                        financeRecyclerView.setHasFixedSize(true);
                        financeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        financeRecyclerView.setAdapter(vendorListAdapter);

                    }

                    @Override
                    public void onFailure(Call<VendorDataFromDb> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });




            }
        });



//        submitUpdatedFinanceReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(amountFromDb.getText().toString())){
//                    amountFromDb.setError("field cannot be empty");
//                    return;
//                }
//                if (TextUtils.isEmpty(EnterAmountFinance.getText().toString())){
//                    EnterAmountFinance.setError("field cannot be empty");
//                    return;
//                }
//                if (amountFr.getVisibility()==View.GONE){
//                    Toast.makeText(getActivity(),"please tally amount",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (bmp==null){
//                    Toast.makeText(getActivity(),"please upload quotation image",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                progressDialog.setTitle("Updating finance report");
//                progressDialog.setMessage("Please wait...,while we are submitting your details");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
//                apiService= APIUrl.getApiClient().create(ApiService.class);
//                Call<FormStatus> transportCall=apiService.updateFinanceReport(
//                        programId,
//                        amountFromDb.getText().toString(),
//                        EnterAmountFinance.getText().toString(),
//                        amountFr.getText().toString(),
//                        imageToString(bmp));
//                transportCall.enqueue(new Callback<FormStatus>() {
//                    @Override
//                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
//                        if (response.body()==null){
//                            progressDialog.dismiss();
//                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//
//                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        HomeFragment fragment=new HomeFragment();
//                        transaction.replace(R.id.frameContainer, fragment);
//                        transaction.commit();
//
//                    }
//                    @Override
//                    public void onFailure(Call<FormStatus> call, Throwable t) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
        return view;
    }

}
