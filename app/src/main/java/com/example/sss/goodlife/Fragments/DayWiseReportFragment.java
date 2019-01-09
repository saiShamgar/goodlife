package com.example.sss.goodlife.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.BankAccountAdapter;
import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.Models.BankAccountIdStatus;
import com.example.sss.goodlife.Models.BankAccountIds;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayWiseReportFragment extends Fragment {
    private TextView daywise_report_selectDate;
    private Spinner daywise_report_spinnerWork;
    private EditText edt_dayWise_men_count,edt_dayWise_Women_count,edt_dayWise_boys_count,edt_dayWise_girls_count
            ,edt_dayWise_PlaceOfLocation,edt_dayWise_WorkDoneInformation,edt_dayWise_PrayerPoints,edt_dayWise_Acheivements
            ,edt_dayWise_Challenges;
    private Button submitDailyReport;

    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    //Api calls
    private ApiService apiService;
    private List<ProgramIds> programIds;
    private String programId;
    private ProgramsIdAdapter programidsAdapter;
    private ProgressDialog progressDialog,reportSubDialog;


    public DayWiseReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Day Wise Program Report");
        View view= inflater.inflate(R.layout.fragment_day_wise_report, container, false);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        reportSubDialog=new ProgressDialog(getActivity());
        reportSubDialog.setTitle("Transport Enroll");
        reportSubDialog.setMessage("Please wait, while we are submitting your details ");
        reportSubDialog.setCanceledOnTouchOutside(false);

        daywise_report_selectDate=view.findViewById(R.id.daywise_report_selectDate);
        daywise_report_spinnerWork=view.findViewById(R.id.daywise_report_spinnerWork);

        edt_dayWise_men_count=view.findViewById(R.id.edt_dayWise_men_count);
        edt_dayWise_Women_count=view.findViewById(R.id.edt_dayWise_Women_count);
        edt_dayWise_boys_count=view.findViewById(R.id.edt_dayWise_boys_count);
        edt_dayWise_girls_count=view.findViewById(R.id.edt_dayWise_girls_count);
        edt_dayWise_PlaceOfLocation=view.findViewById(R.id.edt_dayWise_PlaceOfLocation);
        edt_dayWise_WorkDoneInformation=view.findViewById(R.id.edt_dayWise_WorkDoneInformation);
        edt_dayWise_PrayerPoints=view.findViewById(R.id.edt_dayWise_PrayerPoints);
        edt_dayWise_Acheivements=view.findViewById(R.id.edt_dayWise_Acheivements);
        edt_dayWise_Challenges=view.findViewById(R.id.edt_dayWise_Challenges);

        submitDailyReport=view.findViewById(R.id.submitDailyReport);


        daywise_report_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        daywise_report_selectDate.setText(day + "/" + (month + 1)+ "/" + year);
                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
            }
        });



        //Spinner Dropdown for location
        apiService= APIUrl.getApiClient().create(ApiService.class);
        final retrofit2.Call<ProgramIdsStatus> call=apiService.getProgramids();
        call.enqueue(new Callback<ProgramIdsStatus>() {
            @Override
            public void onResponse(retrofit2.Call<ProgramIdsStatus> call, Response<ProgramIdsStatus> response) {
                if (response.body().getMessage().size()!=0) {
                    programIds = response.body().getMessage();
                    daywise_report_spinnerWork.setPrompt("Select Location");
                    programidsAdapter = new ProgramsIdAdapter(getActivity(), (ArrayList<ProgramIds>) programIds);
                    daywise_report_spinnerWork.setAdapter(programidsAdapter);
                    progressDialog.dismiss();

                    daywise_report_spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            programId= String.valueOf(programidsAdapter.getItem(position).getProgram_id());
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

        submitDailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (daywise_report_selectDate.getText().equals("Select Date")){
                   Toast.makeText(getActivity(),"please select date",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_men_count.getText().toString())){
                    edt_dayWise_men_count.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_Women_count.getText().toString())){
                    edt_dayWise_Women_count.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_boys_count.getText().toString())){
                    edt_dayWise_boys_count.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_girls_count.getText().toString())){
                    edt_dayWise_girls_count.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_PlaceOfLocation.getText().toString())){
                    edt_dayWise_PlaceOfLocation.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_WorkDoneInformation.getText().toString())){
                    edt_dayWise_WorkDoneInformation.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_PrayerPoints.getText().toString())){
                    edt_dayWise_PrayerPoints.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_Acheivements.getText().toString())){
                    edt_dayWise_Acheivements.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_dayWise_Challenges.getText().toString())){
                    edt_dayWise_Challenges.setError("field cannot be empty");
                    return;
                }


                reportSubDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> transportCall=apiService.daywiseReportSubmission(
                        programId,
                        daywise_report_selectDate.getText().toString(),
                        edt_dayWise_men_count.getText().toString(),
                        edt_dayWise_Women_count.getText().toString(),
                        edt_dayWise_boys_count.getText().toString(),
                        edt_dayWise_girls_count.getText().toString(),
                        edt_dayWise_PlaceOfLocation.getText().toString(),
                        edt_dayWise_WorkDoneInformation.getText().toString(),
                        edt_dayWise_PrayerPoints.getText().toString(),
                        edt_dayWise_Acheivements.getText().toString(),
                        edt_dayWise_Challenges.getText().toString());
                transportCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            reportSubDialog.dismiss();
                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        reportSubDialog.dismiss();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment fragment=new HomeFragment();
                        transaction.replace(R.id.frameContainer, fragment);
                        transaction.commit();

                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        reportSubDialog.dismiss();
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

}
