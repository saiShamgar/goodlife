package com.example.sss.goodlife.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessStories extends Fragment {


    public SuccessStories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Success Stories");
        View view= inflater.inflate(R.layout.fragment_success_stories, container, false);

        return view;
    }

}
