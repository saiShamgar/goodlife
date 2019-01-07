package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.PlaceAutocompleteAdapter;
import com.example.sss.goodlife.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class VbsTransportApplication extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    //widgets
    private ImageButton VbsTransportLayout;
    private LinearLayout dynamicVbsTransport;
    private Button submitVbsTransportApplication,VbsTransportAddMoreVendorDetails;
    private Spinner transportTypeOfVehical;
    private EditText transprotOthers,transportRegNum,transportDriverName,transportLicenseId,transportNumPeople,transportDistanceManual;
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

    private ArrayList<EditText> selectedTransportOthers=new ArrayList<>();
    private ArrayList<EditText> selectedTransportRegNum=new ArrayList<>();
    private ArrayList<EditText> selectedTransportDriverName=new ArrayList<>();
    private ArrayList<EditText> selectedTransportLicenceId=new ArrayList<>();
    private ArrayList<EditText> selectedTransportNUmOfPeople=new ArrayList<>();
    private ArrayList<EditText> selectedTransportDistanceManual=new ArrayList<>();
    private ArrayList<AutoCompleteTextView> selectedTransportFromLocation=new ArrayList<>();
    private ArrayList<AutoCompleteTextView> selectedTransportToLocation=new ArrayList<>();

    //Latitude and Longitudes
    private double latitudeFromLocation,longitudeFromLocation,latitudeToLocation,longitudeToLocation;
    private Location startPoint,endPoint;
    private boolean locationListener=false;


    public VbsTransportApplication() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Transport Enroll");
        View view= inflater.inflate(R.layout.fragment_vbs_transport_application, container, false);

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

                    dynamicVbsTransport.addView(rowView);
                }

                transportTypeOfVehical=rowView.findViewById(R.id.transportTypeOfVehical);
                transprotOthers=rowView.findViewById(R.id.transprotOthers);
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

                selectedTransportOthers.add(transprotOthers);
                selectedTransportRegNum.add(transportRegNum);
                selectedTransportDriverName.add(transportDriverName);
                selectedTransportLicenceId.add(transportLicenseId);
                selectedTransportNUmOfPeople.add(transportNumPeople);
                selectedTransportDistanceManual.add(transportDistanceManual);
                selectedTransportFromLocation.add(transportFromLocation);
                selectedTransportToLocation.add(transportToLocation);



                TransportDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicVbsTransport.removeView((View) v.getParent());
                        selectedTransportOthers.remove(transprotOthers);
                        selectedTransportRegNum.remove(transportRegNum);
                        selectedTransportDriverName.remove(transportDriverName);
                        selectedTransportLicenceId.remove(transportLicenseId);
                        selectedTransportNUmOfPeople.remove(transportNumPeople);
                        selectedTransportDistanceManual.remove(transportDistanceManual);
                        selectedTransportFromLocation.remove(transportFromLocation);
                        selectedTransportToLocation.remove(transportToLocation);
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

                    dynamicVbsTransport.addView(rowView);
                }

                transportTypeOfVehical=rowView.findViewById(R.id.transportTypeOfVehical);
                transprotOthers=rowView.findViewById(R.id.transprotOthers);
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
                            String dist = String.valueOf(distance/1000);
                            transportDistanceFromGoogle.setText(dist+"Km");
                        }else {
                            Toast.makeText(getActivity(),"Bad Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                selectedTransportOthers.add(transprotOthers);
                selectedTransportRegNum.add(transportRegNum);
                selectedTransportDriverName.add(transportDriverName);
                selectedTransportLicenceId.add(transportLicenseId);
                selectedTransportNUmOfPeople.add(transportNumPeople);
                selectedTransportDistanceManual.add(transportDistanceManual);
                selectedTransportFromLocation.add(transportFromLocation);
                selectedTransportToLocation.add(transportToLocation);


                TransportDeleteVbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dynamicVbsTransport.removeView((View) v.getParent());
                        selectedTransportOthers.remove(transprotOthers);
                        selectedTransportRegNum.remove(transportRegNum);
                        selectedTransportDriverName.remove(transportDriverName);
                        selectedTransportLicenceId.remove(transportLicenseId);
                        selectedTransportNUmOfPeople.remove(transportNumPeople);
                        selectedTransportDistanceManual.remove(transportDistanceManual);
                        selectedTransportFromLocation.remove(transportFromLocation);
                        selectedTransportToLocation.remove(transportToLocation);
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
