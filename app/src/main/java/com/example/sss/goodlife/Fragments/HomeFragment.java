package com.example.sss.goodlife.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sss.goodlife.Adapters.MySplashScreenAdapter;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private Button btn_home_vbsPro_app,btn_home_vbsVendor_enroll,btn_home_vbsTransport_enroll,btn_home_vbs_Reports,
            btn_home_vbs_program_report,btn_home_vbs_finance_report,btn_home_vbs_reviews;
    private TextView btn_home_vbs_back;
    private ViewPager homePager;
    private CircleIndicator indicator;


    private static final Integer[] slideImages= {R.drawable.goodlife_logo,R.drawable.g,R.drawable.goodlife_logo,R.drawable.g};
    private ArrayList<Integer> slidImagesArray = new ArrayList<Integer>();
    private static int currentPage = 0;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Home");
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        homePager=view.findViewById(R.id.pager);
        btn_home_vbsPro_app=view.findViewById(R.id.btn_home_vbsPro_app);
        btn_home_vbsVendor_enroll=view.findViewById(R.id.btn_home_vbsVendor_enroll);
        btn_home_vbsTransport_enroll=view.findViewById(R.id.btn_home_vbsTransport_enroll);
        btn_home_vbs_Reports=view.findViewById(R.id.btn_home_vbs_Reports);
        btn_home_vbs_program_report=view.findViewById(R.id.btn_home_vbs_program_report);
        btn_home_vbs_finance_report=view.findViewById(R.id.btn_home_vbs_finance_report);
        btn_home_vbs_reviews=view.findViewById(R.id.btn_home_vbs_reviews);
        btn_home_vbs_back=view.findViewById(R.id.btn_home_vbs_back);
         indicator = (CircleIndicator)view.findViewById(R.id.indicator);

        init();

        btn_home_vbsPro_app.setOnClickListener(this);
        btn_home_vbsVendor_enroll.setOnClickListener(this);
        btn_home_vbsTransport_enroll.setOnClickListener(this);
        btn_home_vbs_Reports.setOnClickListener(this);
        btn_home_vbs_program_report.setOnClickListener(this);
        btn_home_vbs_finance_report.setOnClickListener(this);
        btn_home_vbs_reviews.setOnClickListener(this);
        btn_home_vbs_back.setOnClickListener(this);




        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_home_vbsPro_app){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            VBSProgramApplication fragment=new VBSProgramApplication();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbsVendor_enroll){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            VbsFinanceApplication fragment=new VbsFinanceApplication();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbsTransport_enroll){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            VbsTransportApplication fragment=new VbsTransportApplication();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbs_Reports){
            btn_home_vbsPro_app.setVisibility(View.GONE);
            btn_home_vbsVendor_enroll.setVisibility(View.GONE);
            btn_home_vbsTransport_enroll.setVisibility(View.GONE);
            btn_home_vbs_Reports.setVisibility(View.GONE);

            btn_home_vbs_program_report.setVisibility(View.VISIBLE);
            btn_home_vbs_finance_report.setVisibility(View.VISIBLE);
            btn_home_vbs_reviews.setVisibility(View.VISIBLE);
            btn_home_vbs_back.setVisibility(View.VISIBLE);
        }
        if (v.getId()==R.id.btn_home_vbs_program_report){

            //day wise report
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            DayWiseReportFragment fragment=new DayWiseReportFragment();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbs_finance_report){
            //finance report
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            VbsFinanceReport fragment=new VbsFinanceReport();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbs_reviews){

            //Reviews
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ReviewsFragment fragment=new ReviewsFragment();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
        if (v.getId()==R.id.btn_home_vbs_back){
            btn_home_vbsPro_app.setVisibility(View.VISIBLE);
            btn_home_vbsVendor_enroll.setVisibility(View.VISIBLE);
            btn_home_vbsTransport_enroll.setVisibility(View.VISIBLE);
            btn_home_vbs_Reports.setVisibility(View.VISIBLE);

            btn_home_vbs_program_report.setVisibility(View.GONE);
            btn_home_vbs_finance_report.setVisibility(View.GONE);
            btn_home_vbs_reviews.setVisibility(View.GONE);
            btn_home_vbs_back.setVisibility(View.GONE);
        }

    }

    private void init() {
        for(int i=0;i<slideImages.length;i++)
            slidImagesArray.add(slideImages[i]);

        homePager.setAdapter(new MySplashScreenAdapter(getActivity(),slidImagesArray));
        indicator.setViewPager(homePager);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == slideImages.length) {
                    currentPage = 0;
                }
                homePager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 2500);

    }
}
