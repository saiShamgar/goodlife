package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.Locations;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.Status;
import com.example.sss.goodlife.Models.TransPortApplication;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VbsTransportApplication extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    //widgets
    private ImageButton VbsTransportLayout;
    private LinearLayout dynamicVbsTransport,addlayoutImageTransport;
    private Button submitVbsTransportApplication,VbsTransportAddMoreVendorDetails;
    private Spinner transportProgramIdSpinner;
    private EditText transportTypeOfVehical,transportRegNum,transportDriverName,transportLicenseId,transportNumPeople,transportDistanceManual;
    private AutoCompleteTextView transportFromLocation,transportToLocation;
    private TextView transportDistanceFromGoogle,TransportDeleteVbs,clickToGetDistance;

    //location variables
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136));
    private PlaceAutocompleteAdapter autocompleteAdapter,autocompleteAdapter1;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUESTED_CODE = 1234;
    private GoogleApiClient googleApiClient;
    private Boolean mlocation_permission_granted = false;


    private ArrayList<EditText> selectedTransportRegNum=new ArrayList<>();
    private ArrayList<EditText> selectedTransportDriverName=new ArrayList<>();
    private ArrayList<EditText> selectedTransportLicenceId=new ArrayList<>();
    private ArrayList<EditText> selectedTransportNUmOfPeople=new ArrayList<>();
    private ArrayList<EditText> selectedTransportDistanceManual=new ArrayList<>();
    private ArrayList<AutoCompleteTextView> selectedTransportFromLocation=new ArrayList<>();
    private ArrayList<AutoCompleteTextView> selectedTransportToLocation=new ArrayList<>();
    private ArrayList<EditText> selectedTransportTypeOfVehicals=new ArrayList<>();
    private ArrayList<TextView> selectedTransportGoogleDistance=new ArrayList<>();


    private ArrayList<String> ListselectedTransportRegNum=new ArrayList<>();
    private ArrayList<String> ListselectedTransportDriverName=new ArrayList<>();
    private ArrayList<String> ListselectedTransportLicenceId=new ArrayList<>();
    private ArrayList<String> ListselectedTransportNUmOfPeople=new ArrayList<>();
    private ArrayList<String> ListselectedTransportDistanceManual=new ArrayList<>();
    private ArrayList<String> ListselectedTransportFromLocation=new ArrayList<>();
    private ArrayList<String> ListselectedTransportToLocation=new ArrayList<>();
    private ArrayList<String> ListselectedTransportTypeOfVehicals=new ArrayList<>();
    private ArrayList<String> ListselectedTransportGoogleDistance=new ArrayList<>();

    //Latitude and Longitudes
    private double latitudeFromLocation,longitudeFromLocation,latitudeToLocation,longitudeToLocation;
    private Location startPoint,endPoint;
    private boolean locationListener=false;

    //Api calls
    private ApiService apiService;
    private List<ProgramIds> programIds;
    private String programId,locationId;
    private ProgramsIdAdapter programidsAdapter;
    private ProgressDialog progressDialog,transportProgressDialog;

    public VbsTransportApplication() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Transport Enroll");
        View view= inflater.inflate(R.layout.fragment_vbs_transport_application, container, false);


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        transportProgressDialog=new ProgressDialog(getActivity());
        transportProgressDialog.setTitle("Transport Enroll");
        transportProgressDialog.setMessage("Please wait, while we are submitting your details ");
        transportProgressDialog.setCanceledOnTouchOutside(false);


        //Initializing google api client
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        VbsTransportLayout=view.findViewById(R.id.VbsTransportLayout);
        dynamicVbsTransport=view.findViewById(R.id.dynamicVbsTransport);
        submitVbsTransportApplication=view.findViewById(R.id.submitVbsTransportApplication);
        VbsTransportAddMoreVendorDetails=view.findViewById(R.id.VbsTransportAddMoreVendorDetails);
        transportProgramIdSpinner=view.findViewById(R.id.transportProgramIdSpinner);
        addlayoutImageTransport=view.findViewById(R.id.addlayoutImageTransport);


        //Spinner Dropdown for location
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
                    transportProgramIdSpinner.setPrompt("Select Location");
                    programidsAdapter = new ProgramsIdAdapter(getActivity(), (ArrayList<ProgramIds>) programIds);
                    transportProgramIdSpinner.setAdapter(programidsAdapter);
                    progressDialog.dismiss();

                    transportProgramIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            locationId= String.valueOf(programidsAdapter.getItem(position).getLocation_id());
                            programId= String.valueOf(programidsAdapter.getItem(position).getProgram_id());
                            VbsTransportAddMoreVendorDetails.setVisibility(View.VISIBLE);
                            addlayoutImageTransport.setVisibility(View.VISIBLE);
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


        //imageButton dynamic list
        VbsTransportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.dynamic_transport_layout, null);
                // Add the new row before the add field button.

                if (dynamicVbsTransport.getChildCount()==0){
                    dynamicVbsTransport.addView(rowView);
                }else {
                    if (TextUtils.isEmpty(transportTypeOfVehical.getText().toString())){
                        transportTypeOfVehical.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportRegNum.getText().toString())){
                        transportRegNum.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportDriverName.getText().toString())){
                        transportDriverName.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportLicenseId.getText().toString())){
                        transportLicenseId.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportNumPeople.getText().toString())){
                        transportNumPeople.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportDistanceManual.getText().toString())){
                        transportDistanceManual.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportFromLocation.getText().toString())){
                        transportFromLocation.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportToLocation.getText().toString())){
                        transportToLocation.setError("field cannot be empty");
                        return;
                    }
                    if (transportDistanceFromGoogle.getVisibility()==View.GONE){
                        Toast.makeText(getActivity(),"Please get google distance",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dynamicVbsTransport.addView(rowView);
                }

                transportTypeOfVehical=rowView.findViewById(R.id.transportTypeOfVehical);
                transportRegNum=rowView.findViewById(R.id.transportRegNum);
                transportDriverName=rowView.findViewById(R.id.transportDriverName);
                transportLicenseId=rowView.findViewById(R.id.transportLicenseId);
                transportNumPeople=rowView.findViewById(R.id.transportNumPeople);
                transportDistanceManual=rowView.findViewById(R.id.transportDistanceManual);
                transportFromLocation=rowView.findViewById(R.id.transportFromLocation);
                transportToLocation=rowView.findViewById(R.id.transportToLocation);
                transportDistanceFromGoogle=rowView.findViewById(R.id.transportDistanceFromGoogle);
                TransportDeleteVbs=rowView.findViewById(R.id.TransportDeleteVbs);
                clickToGetDistance=rowView.findViewById(R.id.clickToGetDistance);


                transportFromLocation.setOnItemClickListener(mAutoCompleteListenerFrom);
                autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                transportFromLocation.setAdapter(autocompleteAdapter);

                transportToLocation.setOnItemClickListener(mAutoCompleteListenerTo);
                autocompleteAdapter1 = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                transportToLocation.setAdapter(autocompleteAdapter1);

                clickToGetDistance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (latitudeFromLocation!=0 && latitudeToLocation!=0){
                            startPoint=new Location("locationA");
                            startPoint.setLatitude(latitudeFromLocation);
                            startPoint.setLongitude(longitudeFromLocation);

                            endPoint=new Location("locationA");
                            endPoint.setLatitude(latitudeToLocation);
                            endPoint.setLongitude(longitudeToLocation);
                            double distance= startPoint.distanceTo(endPoint);
                            String dist = String.valueOf(distance/1000);
                            transportDistanceFromGoogle.setText(dist+"Km");
                        }else {
                            Toast.makeText(getActivity(),"Bad Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                selectedTransportRegNum.add(transportRegNum);
                selectedTransportDriverName.add(transportDriverName);
                selectedTransportLicenceId.add(transportLicenseId);
                selectedTransportNUmOfPeople.add(transportNumPeople);
                selectedTransportDistanceManual.add(transportDistanceManual);
                selectedTransportFromLocation.add(transportFromLocation);
                selectedTransportToLocation.add(transportToLocation);
                selectedTransportTypeOfVehicals.add(transportTypeOfVehical);
                selectedTransportGoogleDistance.add(transportDistanceFromGoogle);



                TransportDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicVbsTransport.removeView((View) v.getParent());

                        selectedTransportRegNum.remove(transportRegNum);
                        selectedTransportDriverName.remove(transportDriverName);
                        selectedTransportLicenceId.remove(transportLicenseId);
                        selectedTransportNUmOfPeople.remove(transportNumPeople);
                        selectedTransportDistanceManual.remove(transportDistanceManual);
                        selectedTransportFromLocation.remove(transportFromLocation);
                        selectedTransportToLocation.remove(transportToLocation);
                        selectedTransportTypeOfVehicals.remove(transportTypeOfVehical);
                        selectedTransportGoogleDistance.remove(transportDistanceFromGoogle);
                    }
                });



            }
        });

        //dynamicView by Add more Button
        VbsTransportAddMoreVendorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.dynamic_transport_layout, null);
                // Add the new row before the add field button.

                if (dynamicVbsTransport.getChildCount()==0){
                    dynamicVbsTransport.addView(rowView);
                }else {
                    if (TextUtils.isEmpty(transportTypeOfVehical.getText().toString())){
                        transportTypeOfVehical.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportRegNum.getText().toString())){
                        transportRegNum.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportDriverName.getText().toString())){
                        transportDriverName.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportLicenseId.getText().toString())){
                        transportLicenseId.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportNumPeople.getText().toString())){
                        transportNumPeople.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportDistanceManual.getText().toString())){
                        transportDistanceManual.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportFromLocation.getText().toString())){
                        transportFromLocation.setError("field cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(transportToLocation.getText().toString())){
                        transportToLocation.setError("field cannot be empty");
                        return;
                    }
                    if (transportDistanceFromGoogle.getVisibility()==View.GONE){
                        Toast.makeText(getActivity(),"Please get google distance",Toast.LENGTH_SHORT).show();
                        return;
                    }


                    dynamicVbsTransport.addView(rowView);
                }

                transportTypeOfVehical=rowView.findViewById(R.id.transportTypeOfVehical);
                transportRegNum=rowView.findViewById(R.id.transportRegNum);
                transportDriverName=rowView.findViewById(R.id.transportDriverName);
                transportLicenseId=rowView.findViewById(R.id.transportLicenseId);
                transportNumPeople=rowView.findViewById(R.id.transportNumPeople);
                transportDistanceManual=rowView.findViewById(R.id.transportDistanceManual);
                transportFromLocation=rowView.findViewById(R.id.transportFromLocation);
                transportToLocation=rowView.findViewById(R.id.transportToLocation);
                transportDistanceFromGoogle=rowView.findViewById(R.id.transportDistanceFromGoogle);
                TransportDeleteVbs=rowView.findViewById(R.id.TransportDeleteVbs);
                clickToGetDistance=rowView.findViewById(R.id.clickToGetDistance);



                transportFromLocation.setOnItemClickListener(mAutoCompleteListenerFrom);
                autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                transportFromLocation.setAdapter(autocompleteAdapter);

                transportToLocation.setOnItemClickListener(mAutoCompleteListenerTo);
                autocompleteAdapter1 = new PlaceAutocompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
                transportToLocation.setAdapter(autocompleteAdapter1);

                clickToGetDistance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (latitudeFromLocation!=0 && latitudeToLocation!=0){
                            startPoint=new Location("locationA");
                            startPoint.setLatitude(latitudeFromLocation);
                            startPoint.setLongitude(longitudeFromLocation);

                            endPoint=new Location("locationA");
                            endPoint.setLatitude(latitudeToLocation);
                            endPoint.setLongitude(longitudeToLocation);
                            double distance=startPoint.distanceTo(endPoint);
                            Log.e("distance", String.valueOf(distance));

                            double C = Math.sin(latitudeFromLocation/57.3) * Math.sin(latitudeToLocation/57.3) +
                                    Math.cos(latitudeFromLocation/57.3) * Math.cos(latitudeToLocation/57.3) *
                                            Math.cos(longitudeToLocation/57.3 - longitudeFromLocation/57.3);
                            String  distd = String.valueOf(3959 * Math.acos(C));

                            Log.e("distance", distd);
                            transportDistanceFromGoogle.setText(distd+"Km");
                        }else {
                            Toast.makeText(getActivity(),"Bad Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                selectedTransportRegNum.add(transportRegNum);
                selectedTransportDriverName.add(transportDriverName);
                selectedTransportLicenceId.add(transportLicenseId);
                selectedTransportNUmOfPeople.add(transportNumPeople);
                selectedTransportDistanceManual.add(transportDistanceManual);
                selectedTransportFromLocation.add(transportFromLocation);
                selectedTransportToLocation.add(transportToLocation);
                selectedTransportTypeOfVehicals.add(transportTypeOfVehical);
                selectedTransportGoogleDistance.add(transportDistanceFromGoogle);


                TransportDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicVbsTransport.removeView((View) v.getParent());
                        selectedTransportRegNum.remove(transportRegNum);
                        selectedTransportDriverName.remove(transportDriverName);
                        selectedTransportLicenceId.remove(transportLicenseId);
                        selectedTransportNUmOfPeople.remove(transportNumPeople);
                        selectedTransportDistanceManual.remove(transportDistanceManual);
                        selectedTransportFromLocation.remove(transportFromLocation);
                        selectedTransportToLocation.remove(transportToLocation);
                        selectedTransportTypeOfVehicals.remove(transportTypeOfVehical);
                        selectedTransportGoogleDistance.remove(transportDistanceFromGoogle);
                    }
                });
            }
        });

        submitVbsTransportApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicVbsTransport.getChildCount()==0) {
                    Toast.makeText(getActivity(),"Please Add Transport Details",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(transportTypeOfVehical.getText().toString())){
                    transportTypeOfVehical.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportRegNum.getText().toString())){
                    transportRegNum.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportDriverName.getText().toString())){
                    transportDriverName.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportLicenseId.getText().toString())){
                    transportLicenseId.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportNumPeople.getText().toString())){
                    transportNumPeople.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportDistanceManual.getText().toString())){
                    transportDistanceManual.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportFromLocation.getText().toString())){
                    transportFromLocation.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(transportToLocation.getText().toString())){
                    transportToLocation.setError("field cannot be empty");
                    return;
                }
                if (transportDistanceFromGoogle.getVisibility()==View.GONE){
                    Toast.makeText(getActivity(),"Please get google distance",Toast.LENGTH_SHORT).show();
                    return;
                }


                ListselectedTransportRegNum.clear();
                ListselectedTransportDriverName.clear();
                ListselectedTransportLicenceId.clear();
                ListselectedTransportNUmOfPeople.clear();
                ListselectedTransportDistanceManual.clear();
                ListselectedTransportFromLocation.clear();
                ListselectedTransportToLocation.clear();
                ListselectedTransportTypeOfVehicals.clear();
                ListselectedTransportGoogleDistance.clear();


                    for (int i=0;i<selectedTransportRegNum.size();i++){
                        ListselectedTransportRegNum.add(selectedTransportRegNum.get(i).getText().toString());
                        ListselectedTransportDriverName.add(selectedTransportDriverName.get(i).getText().toString());
                        ListselectedTransportLicenceId.add(selectedTransportLicenceId.get(i).getText().toString());
                        ListselectedTransportNUmOfPeople.add(selectedTransportNUmOfPeople.get(i).getText().toString());
                        ListselectedTransportDistanceManual.add(selectedTransportDistanceManual.get(i).getText().toString());
                        ListselectedTransportFromLocation.add(selectedTransportFromLocation.get(i).getText().toString());
                        ListselectedTransportToLocation.add(selectedTransportToLocation.get(i).getText().toString());
                        ListselectedTransportTypeOfVehicals.add(selectedTransportTypeOfVehicals.get(i).getText().toString());
                        ListselectedTransportGoogleDistance.add(selectedTransportGoogleDistance.get(i).getText().toString());

                        Log.e("vehical name",ListselectedTransportTypeOfVehicals.toString());
                        Log.e("RegNum",ListselectedTransportRegNum.toString());
                        Log.e("DriverName",ListselectedTransportDriverName.toString());
                        Log.e("LicenceId",ListselectedTransportLicenceId.toString());
                        Log.e("NumOfPeople",ListselectedTransportNUmOfPeople.toString());
                        Log.e("manual distance",ListselectedTransportDistanceManual.toString());
                        Log.e("fromLocation",ListselectedTransportFromLocation.toString());
                        Log.e("toLocation",ListselectedTransportToLocation.toString());
                        Log.e("GoogleDistance",ListselectedTransportGoogleDistance.toString());

                    }

                TransPortApplication application=new TransPortApplication(
                        ListselectedTransportTypeOfVehicals,
                        ListselectedTransportRegNum,
                        ListselectedTransportDriverName,
                        ListselectedTransportLicenceId,
                        ListselectedTransportNUmOfPeople,
                        ListselectedTransportFromLocation,
                        ListselectedTransportToLocation,
                        ListselectedTransportGoogleDistance,
                        ListselectedTransportDistanceManual);


                Gson gson = new Gson();
                String tranport_list = gson.toJson(application);
                Log.e("program id ",programId);
                Log.e("location id ",locationId);
                Log.e("tranport_list ",tranport_list);


                submitVbsTransportApplication.setClickable(false);
                transportProgressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> transportCall=apiService.transportSubmission(
                        programId,
                        locationId,
                        tranport_list);
                transportCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            transportProgressDialog.dismiss();
                            submitVbsTransportApplication.setClickable(true);
                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        transportProgressDialog.dismiss();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment fragment=new HomeFragment();
                        transaction.replace(R.id.frameContainer, fragment);
                        transaction.commit();

                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        transportProgressDialog.dismiss();
                        submitVbsTransportApplication.setClickable(true);
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    //setting location places to editText
    private AdapterView.OnItemClickListener mAutoCompleteListenerFrom=new AdapterView.OnItemClickListener() {
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
            longitudeFromLocation=qLoc.longitude;
            latitudeFromLocation=qLoc.longitude;
            Toast.makeText(getActivity(),"location "+qLoc,Toast.LENGTH_LONG).show();
            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //setting location places to editText
    private AdapterView.OnItemClickListener mAutoCompleteListenerTo=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final AutocompletePrediction item=autocompleteAdapter1.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi
                    .getPlaceById(googleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallBack2);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallBack2=new ResultCallback<PlaceBuffer>() {
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
            longitudeToLocation=qLoc.longitude;
            latitudeToLocation=qLoc.longitude;
            Toast.makeText(getActivity(),"location "+qLoc,Toast.LENGTH_LONG).show();
            places.release();
        }
    };


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
