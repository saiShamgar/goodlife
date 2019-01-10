package com.example.sss.goodlife.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.R;

public class UploadVbsPhotos extends Fragment {


    public UploadVbsPhotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Upload photos");
        View view= inflater.inflate(R.layout.fragment_upload_vbs_photos, container, false);

        return view;
    }


}
