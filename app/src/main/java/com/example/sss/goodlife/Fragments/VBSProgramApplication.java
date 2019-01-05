package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.PlaceAutocompleteAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class VBSProgramApplication extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

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
    private AutoCompleteTextView mSearchText;

    //Widgents
    private LinearLayout VbsEventparentLinearLayout,VbsParentLinearLayout2;
    private ImageButton VbsAddEventDateColumn,delete_button,VbsAddParticipaintsColumn,delete_button_participaints;
    private TextView selectDateTxt,selectStartTime,selectEndTime,participaintDate;
    private Spinner spinnerStartTimeEvent,spinnerEndTimeEvent,spinnerParticipaintsList;
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

    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private String selectedItemStartEvent,selectedItemEndEvent;



    public VBSProgramApplication() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vbsprogram_application, container, false);

        //Initializing google api client
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mSearchText = (AutoCompleteTextView)view.findViewById(R.id.VbsLocation);
        mSearchText.setOnItemClickListener(mAutoCompleteListener);
        autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(autocompleteAdapter);


        //initializing widgets
        VbsEventparentLinearLayout=(LinearLayout)view.findViewById(R.id.VbsEventparentLinearLayout);
        VbsParentLinearLayout2=(LinearLayout)view.findViewById(R.id.VbsParentLinearLayout2);
        VbsAddEventDateColumn=view.findViewById(R.id.VbsAddEventDateColumn);
        VbsAddParticipaintsColumn=view.findViewById(R.id.VbsAddParticipaintsColumn);
        VbsProgramAim=view.findViewById(R.id.VbsProgramAim);
        VbsApplicationSubmit=view.findViewById(R.id.VbsApplicationSubmit);





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


                selectedEventDates.add(selectDateTxt);
                selectedEventStartTime.add(selectStartTime);
                selectedEventEndTime.add(selectEndTime);


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

                selectedNumOfMen.add(participaints_num_men);
                selectedNumOfWomen.add(participaints_num_women);
                selectedNumOfChild.add(participaints_num_child);
                selectedPar_names.add(participaintsName);
                selectedPar_phone.add(participaintsPhone);
                selectedPar_des.add(participaintsDescription);
                participantDates.add(participaintDate);


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

                delete_button_participaints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VbsParentLinearLayout2.removeView((View) v.getParent());
                        participantDates.remove(participaintDate);
                        selectedNumOfMen.remove(participaints_num_men);
                        selectedNumOfWomen.remove(participaints_num_women);
                        selectedNumOfChild.remove(participaints_num_child);
                    }
                });

            }
        });

        VbsApplicationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSearchText.getText().toString())){
                    mSearchText.setError("please add location");
                    Toast.makeText(getActivity(),"please add location",Toast.LENGTH_SHORT).show();
                    return;
                }
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

                Log.e("Location",mSearchText.getText().toString());
                Log.e("Aim",VbsProgramAim.getText().toString());
                if (selectedEventDates.size()!=0){
                    for (int i=0;i<selectedEventDates.size();i++){
                        Log.e("dates",selectedEventDates.get(i).getText().toString());
                        Log.e("startTime",selectedEventStartTime.get(i).getText().toString()+selectedItemStartEvent);
                        Log.e("endTime",selectedEventEndTime.get(i).getText().toString()+selectedItemEndEvent);
                    }
                }
                if (participantDates.size()!=0){
                    for (int i=0;i<participantDates.size();i++){
                        Log.e("dates",participantDates.get(i).getText().toString());
                        Log.e("name",selectedPar_names.get(i).getText().toString());
                        Log.e("phone",selectedPar_phone.get(i).getText().toString());
                        Log.e("desc",selectedPar_des.get(i).getText().toString());
                        Log.e("men",selectedNumOfMen.get(i).getText().toString());
                        Log.e("Women",selectedNumOfWomen.get(i).getText().toString());
                        Log.e("Child",selectedNumOfChild.get(i).getText().toString());
                    }
                }
            }
        });
        return view;
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

}
