package com.example.sss.goodlife.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.R;

public class UploadVbsPhotos extends Fragment {
    private TextView gettingMultipleImagesViews;
    private LinearLayout multipleImagesParentLayout;


    public UploadVbsPhotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Upload photos");
        View view= inflater.inflate(R.layout.fragment_upload_vbs_photos, container, false);

        gettingMultipleImagesViews=view.findViewById(R.id.gettingMultipleImagesViews);
        multipleImagesParentLayout=view.findViewById(R.id.multipleImagesParentLayout);

        gettingMultipleImagesViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.multiple_images_layout, null);
                // Add the new row before the add field button.

                multipleImagesParentLayout.addView(rowView);

            }

        });

        return view;
    }


}
