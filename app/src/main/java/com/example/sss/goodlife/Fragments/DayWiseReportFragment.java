package com.example.sss.goodlife.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayWiseReportFragment extends Fragment {
    private TextView daywise_report_selectDate;

    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


    public DayWiseReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Day Wise Program Report");
        View view= inflater.inflate(R.layout.fragment_day_wise_report, container, false);

        daywise_report_selectDate=view.findViewById(R.id.daywise_report_selectDate);
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

        return view;
    }

}
