package com.example.sss.goodlife.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.BankAccountAdapter;
import com.example.sss.goodlife.Adapters.LocationAdapter;
import com.example.sss.goodlife.Adapters.ParticipantsTypeAdapter;
import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.Models.BankAccountIds;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ParticipantsList;
import com.example.sss.goodlife.Models.Locations;
import com.example.sss.goodlife.Models.ParticipantsTypeIds;
import com.example.sss.goodlife.Models.ParticipantsTypeStatus;
import com.example.sss.goodlife.Models.ProgramEventDates;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.Status;
import com.example.sss.goodlife.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VBSProgramApplication extends Fragment{

    //Widgents
    private LinearLayout VbsEventparentLinearLayout,VbsParentLinearLayout2;
    private ImageButton VbsAddEventDateColumn,delete_button,VbsAddParticipaintsColumn,delete_button_participaints;
    private TextView selectDateTxt,selectStartTime,selectEndTime,participaintDate;
    private Spinner spinnerStartTimeEvent,spinnerEndTimeEvent,spinnerParticipaintsList,location;
    private EditText participaints_num_men,participaints_num_women,participaints_num_child,participaintsName,participaintsPhone,participaintsDescription,VbsProgramAim;
    private Button VbsApplicationSubmit;

    private ArrayList<TextView> selectedEventDates=new ArrayList<>();
    private ArrayList<TextView> selectedEventStartTime=new ArrayList<>();
    private ArrayList<TextView> selectedEventEndTime=new ArrayList<>();

    private ArrayList<EditText> selectedNumOfMen=new ArrayList<>();
    private ArrayList<EditText> selectedNumOfWomen=new ArrayList<>();
    private ArrayList<EditText> selectedNumOfChild=new ArrayList<>();
    private ArrayList<EditText> selectedPar_names=new ArrayList<>();
    private ArrayList<EditText> selectedPar_phone=new ArrayList<>();
    private ArrayList<EditText> selectedPar_des=new ArrayList<>();
    private ArrayList<TextView> participantDates=new ArrayList<>();

    private ArrayList<String> ListselectedEventDates=new ArrayList<>();
    private ArrayList<String> ListselectedEventStartTime=new ArrayList<>();
    private ArrayList<String> ListselectedEventEndTime=new ArrayList<>();

    private ArrayList<String> ListselectedNumOfMen=new ArrayList<>();
    private ArrayList<String> ListselectedNumOfWomen=new ArrayList<>();
    private ArrayList<String> ListselectedNumOfChild=new ArrayList<>();
    private ArrayList<String> ListselectedPar_names=new ArrayList<>();
    private ArrayList<String> ListselectedPar_ids=new ArrayList<>();
    private ArrayList<String> ListselectedPar_phone=new ArrayList<>();
    private ArrayList<String> ListselectedPar_des=new ArrayList<>();
    private ArrayList<String> ListparticipantDates=new ArrayList<>();


    private ArrayList<String> programNamesDropDownArrayList=new ArrayList<>();



    ArrayList<String> state = new ArrayList<String>();
    private ArrayAdapter adapter;
    private LocationAdapter locationAdapter;
    private ParticipantsTypeAdapter participantsTypeAdapter;

    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private String selectedItemStartEvent,selectedItemEndEvent;

    //Api calls
    private ApiService apiService;
    private List<Locations> locationTypes;
    private List<ParticipantsTypeIds> participantsTypeIds;
    private String locationId,participantId;
    private ProgressDialog progressDialog,progressDialog1,gettingParticipantsList;


    public VBSProgramApplication() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Program Application");
        final View view= inflater.inflate(R.layout.fragment_vbsprogram_application, container, false);

        location = (Spinner) view.findViewById(R.id.VbsLocation);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Applying Program application");
        progressDialog.setMessage("Please wait, while we are submitting your details ");
        progressDialog.setCanceledOnTouchOutside(false);

        gettingParticipantsList=new ProgressDialog(getActivity());
        gettingParticipantsList.setTitle("Getting Data");
        gettingParticipantsList.setMessage("Please wait..., ");
        gettingParticipantsList.setCanceledOnTouchOutside(false);

        progressDialog1=new ProgressDialog(getActivity());
        progressDialog1.setTitle("Getting Data");
        progressDialog1.setMessage("Please wait...,");
        progressDialog1.setCanceledOnTouchOutside(false);
        progressDialog1.show();

        //initializing widgets
        VbsEventparentLinearLayout=(LinearLayout)view.findViewById(R.id.VbsEventparentLinearLayout);
        VbsParentLinearLayout2=(LinearLayout)view.findViewById(R.id.VbsParentLinearLayout2);
        VbsAddEventDateColumn=view.findViewById(R.id.VbsAddEventDateColumn);
        VbsAddParticipaintsColumn=view.findViewById(R.id.VbsAddParticipaintsColumn);
        VbsProgramAim=view.findViewById(R.id.VbsProgramAim);
        VbsApplicationSubmit=view.findViewById(R.id.VbsApplicationSubmit);


        //Spinner Dropdown for location
        apiService= APIUrl.getApiClient().create(ApiService.class);
        final retrofit2.Call<Status> call=apiService.getLocations();
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
                if (response.body()==null){
                    Toast.makeText(getActivity(),"rexponce null",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().getMessage().size()!=0) {
                    progressDialog1.dismiss();
                    locationTypes = response.body().getMessage();
                    location.setPrompt("Select Location");
                         locationAdapter = new LocationAdapter(getActivity(), (ArrayList<Locations>) locationTypes);
                        location.setAdapter(locationAdapter);

                        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                               locationId= String.valueOf(locationAdapter.getItem(position).getLocation_id());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                }
                else {
                    progressDialog1.dismiss();
                    Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Status> call, Throwable t) {
                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                progressDialog1.dismiss();
            }
        });



        //addEventDate dynamic
        VbsAddEventDateColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                if (VbsEventparentLinearLayout.getChildCount()==0){
                    VbsEventparentLinearLayout.addView(rowView);
                }else {
                    if (selectDateTxt.getText().toString().contains("Select Date")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectStartTime.getText().toString().contains("Click")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectEndTime.getText().toString().contains("Click")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    VbsEventparentLinearLayout.addView(rowView);
                }
                selectDateTxt=(TextView) rowView.findViewById(R.id.selectDateTxt);
                delete_button=(ImageButton) rowView.findViewById(R.id.delete_button);
                selectStartTime=(TextView)rowView.findViewById(R.id.selectStartTime);
                selectEndTime=(TextView)rowView.findViewById(R.id.selectEndTime);
                spinnerStartTimeEvent=(Spinner)rowView.findViewById(R.id.spinnerStartTimeEvent);
                spinnerEndTimeEvent=(Spinner)rowView.findViewById(R.id.spinnerEndTimeEvent);





                //spinner data from time
                spinnerStartTimeEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItemStartEvent=parent.getSelectedItem().toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //spinner data to time
                spinnerEndTimeEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItemEndEvent=parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                selectDateTxt.setOnClickListener(new View.OnClickListener() {
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
                                        selectDateTxt.setText(day + "/" + (month + 1)+ "/" + year);
                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });

                selectedEventDates.add(selectDateTxt);
                selectedEventStartTime.add(selectStartTime);
                selectedEventEndTime.add(selectEndTime);

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VbsEventparentLinearLayout.removeView((View) v.getParent());

                        selectedEventDates.remove(selectDateTxt);
                        selectedEventStartTime.remove(selectStartTime);
                        selectedEventEndTime.remove(selectEndTime);

                    }
                });

                final int[] mSelectedItem = new int[1];
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select From time")
                        .setSingleChoiceItems(R.array.timeHours, 0,
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSelectedItem[0] = which;
                                    }
                                })
                        // Set the action buttons
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "You selected !! \n " + getResources().getStringArray(R.array.timeHours)[mSelectedItem[0]], Toast.LENGTH_SHORT).show();
                                selectStartTime.setText(getResources().getStringArray(R.array.timeHours)[mSelectedItem[0]]);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "You clicked Cancel \n No Item was selected !!", Toast.LENGTH_SHORT).show();

                            }
                        });

                builder.create();

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Select From time")
                        .setSingleChoiceItems(R.array.timeHours, 0,
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSelectedItem[0] = which;
                                    }
                                })
                        // Set the action buttons
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "You selected !! \n " + getResources().getStringArray(R.array.timeHours)[mSelectedItem[0]], Toast.LENGTH_SHORT).show();
                                selectEndTime.setText(getResources().getStringArray(R.array.timeHours)[mSelectedItem[0]]);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "You clicked Cancel \n No Item was selected !!", Toast.LENGTH_SHORT).show();

                            }
                        });

                builder1.create();

                selectStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.show();
                    }
                });
                selectEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder1.show();
                    }
                });

            }
        });

        VbsAddParticipaintsColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.participants_list_dynamic_field, null);
                // Add the new row before the add field button.

                if (VbsParentLinearLayout2.getChildCount()==0){
                    VbsParentLinearLayout2.addView(rowView);
                }else {
                    if (participaintDate.getText().toString().contains("Select Date")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(participaints_num_men.getText().toString())){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        participaints_num_men.setError("field cannot be blank");
                        return;
                    }
                    if (TextUtils.isEmpty(participaints_num_women.getText().toString())){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        participaints_num_women.setError("field cannot be blank");
                        return;
                    }
                    if (TextUtils.isEmpty(participaints_num_child.getText().toString())){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        participaints_num_child.setError("field cannot be blank");
                        return;
                    }
                    VbsParentLinearLayout2.addView(rowView);
                }

                participaints_num_men=rowView.findViewById(R.id.num_men);
                participaints_num_women=rowView.findViewById(R.id.num_women);
                participaints_num_child=rowView.findViewById(R.id.num_child);
                participaintDate=rowView.findViewById(R.id.participaintDate);
                spinnerParticipaintsList=rowView.findViewById(R.id.participaintsSpinner);
                delete_button_participaints=rowView.findViewById(R.id.delete_button_participaints);
                participaintsName=rowView.findViewById(R.id.participaintsName);
                participaintsPhone=rowView.findViewById(R.id.participaintsPhone);
                participaintsDescription=rowView.findViewById(R.id.participaintsDescription);

                gettingParticipantsList.show();

                //Spinner Dropdown for location
                apiService= APIUrl.getApiClient().create(ApiService.class);
                final retrofit2.Call<ParticipantsTypeStatus> call=apiService.getParticipantsIds();
                call.enqueue(new Callback<ParticipantsTypeStatus>() {
                    @Override
                    public void onResponse(retrofit2.Call<ParticipantsTypeStatus> call, Response<ParticipantsTypeStatus> response) {
                        if (response.body().getMessage().size()!=0) {
                            gettingParticipantsList.dismiss();
                            participantsTypeIds = response.body().getMessage();
                            spinnerParticipaintsList.setPrompt("Select Location");
                            participantsTypeAdapter = new ParticipantsTypeAdapter(getActivity(), (ArrayList<ParticipantsTypeIds>) participantsTypeIds);
                            spinnerParticipaintsList.setAdapter(participantsTypeAdapter);
                        }
                        else {
                            gettingParticipantsList.dismiss();
                            Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ParticipantsTypeStatus> call, Throwable t) {
                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        gettingParticipantsList.dismiss();
                    }
                });




                participaintDate.setOnClickListener(new View.OnClickListener() {
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
                                        participaintDate.setText(day + "/" + (month + 1)+ "/" + year);
                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });

                selectedNumOfMen.add(participaints_num_men);
                selectedNumOfWomen.add(participaints_num_women);
                selectedNumOfChild.add(participaints_num_child);
                selectedPar_names.add(participaintsName);
                selectedPar_phone.add(participaintsPhone);
                selectedPar_des.add(participaintsDescription);
                participantDates.add(participaintDate);

                delete_button_participaints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VbsParentLinearLayout2.removeView((View) v.getParent());

                        selectedNumOfMen.remove(participaints_num_men);
                        selectedNumOfWomen.remove(participaints_num_women);
                        selectedNumOfChild.remove(participaints_num_child);
                        selectedPar_names.remove(participaintsName);
                        selectedPar_phone.remove(participaintsPhone);
                        selectedPar_des.remove(participaintsDescription);
                        participantDates.remove(participaintDate);
                    }
                });

            }
        });

        VbsApplicationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(VbsProgramAim.getText().toString())){
                    VbsProgramAim.setError("please add Program aim");
                    Toast.makeText(getActivity(),"please add Program aim",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (VbsEventparentLinearLayout.getChildCount()==0){
                    Toast.makeText(getActivity(),"please add event dates",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectDateTxt.getText().toString().contains("Select Date")){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectStartTime.getText().toString().contains("Click")){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectEndTime.getText().toString().contains("Click")){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (VbsParentLinearLayout2.getChildCount()==0){
                    Toast.makeText(getActivity(),"please add participant Lists",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(participaints_num_men.getText().toString())){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    participaints_num_men.setError("field cannot be blank");
                    return;
                }
                if (TextUtils.isEmpty(participaints_num_women.getText().toString())){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    participaints_num_women.setError("field cannot be blank");
                    return;
                }
                if (TextUtils.isEmpty(participaints_num_child.getText().toString())){
                    Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_SHORT).show();
                    participaints_num_child.setError("field cannot be blank");
                    return;
                }

                progressDialog.show();

                ListselectedEventDates.clear();
                ListselectedEventStartTime.clear();
                ListselectedEventEndTime.clear();

                ListparticipantDates.clear();
                ListselectedPar_ids.clear();
                ListselectedPar_names.clear();
                ListselectedPar_phone.clear();
                ListselectedPar_des.clear();
                ListselectedNumOfMen.clear();
                ListselectedNumOfWomen.clear();
                ListselectedNumOfChild.clear();



                Log.e("Aim",VbsProgramAim.getText().toString());
                if (selectedEventDates.size()!=0){
                    for (int i=0;i<selectedEventDates.size();i++){

//                        Log.e("dates",selectedEventDates.get(i).getText().toString());
//                        Log.e("startTime",selectedEventStartTime.get(i).getText().toString()+selectedItemStartEvent);
//                        Log.e("endTime",selectedEventEndTime.get(i).getText().toString()+selectedItemEndEvent);
                        ListselectedEventDates.add(selectedEventDates.get(i).getText().toString());
                        ListselectedEventStartTime.add(selectedEventStartTime.get(i).getText().toString()+selectedItemStartEvent);
                        ListselectedEventEndTime.add(selectedEventEndTime.get(i).getText().toString()+selectedItemEndEvent);

//                        Log.e("dates",selectedEventDates.get(i).getText().toString());
//                        Log.e("startTime",ListselectedEventStartTime.toString());
//                        Log.e("endTime",ListselectedEventEndTime.toString());
                    }
                }
                if (participantDates.size()!=0){
                    for (int i=0;i<participantDates.size();i++){
//                        Log.e("dates",selectedEventDates.get(i).getText().toString());
//                        Log.e("name",selectedPar_names.get(i).getText().toString());
//                        Log.e("phone",selectedPar_phone.get(i).getText().toString());
//                        Log.e("desc",selectedPar_des.get(i).getText().toString());
//                        Log.e("men",selectedNumOfMen.get(i).getText().toString());
//                        Log.e("Women",selectedNumOfWomen.get(i).getText().toString());
                      // Log.e("ListselectedPar_ids",ListselectedPar_ids.get(i).toString());

                        ListparticipantDates.add(participantDates.get(i).getText().toString());
                        ListselectedPar_ids.add(participantsTypeAdapter.getItem(spinnerParticipaintsList.getSelectedItemPosition()).getParticipant_type_id());
                        ListselectedPar_names.add(selectedPar_names.get(i).getText().toString());
                        ListselectedPar_phone.add(selectedPar_phone.get(i).getText().toString());
                        ListselectedPar_des.add(selectedPar_des.get(i).getText().toString());
                        ListselectedNumOfMen.add(selectedNumOfMen.get(i).getText().toString());
                        ListselectedNumOfWomen.add(selectedNumOfWomen.get(i).getText().toString());
                        ListselectedNumOfChild.add(selectedNumOfChild.get(i).getText().toString());


                    }
                }

                ParticipantsList participantsList =new ParticipantsList(
                        ListparticipantDates,
                        ListselectedPar_ids,
                        ListselectedPar_names,
                        ListselectedPar_phone,
                        ListselectedNumOfMen,
                        ListselectedNumOfWomen,
                        ListselectedNumOfChild,
                        ListselectedPar_des
                );

                final ProgramEventDates programEventDates=new ProgramEventDates(
                        ListselectedEventDates,
                        ListselectedEventStartTime,
                        ListselectedEventEndTime);
                Gson gson = new Gson();
                String participants = gson.toJson(participantsList);
                String events = gson.toJson(programEventDates);
                Log.e("events ",events);
                Log.e("participants ",participants);


                VbsApplicationSubmit.setClickable(false);
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> formStatusCall=apiService.FormSubmission(
                        VbsProgramAim.getText().toString(),
                        locationId,
                        events,
                        participants);
                formStatusCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body().getStatus()==null){
                            progressDialog.dismiss();
                            VbsApplicationSubmit.setClickable(true);
                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment fragment=new HomeFragment();
                        transaction.replace(R.id.frameContainer, fragment);
                        transaction.commit();

                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        progressDialog.dismiss();
                        VbsApplicationSubmit.setClickable(true);
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return view;
    }
}
