package com.example.sss.goodlife.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFormFragment extends Fragment {

    private EditText financeExpenditure,financeCompanyname,financeCompanyLocation,financeCompanyPhone
            ,financeTotalBidding,financeCompanyBankNum,financeCompanyBankIfsc;
    private ImageButton addEventDateColumn,addPaticipantsList,delete_button_participaints,delete_button,finance_back_button;
    private LinearLayout parentLinearLayout,parentLinearLayout2,eventdateLayout,participantMain,financeLayout;
    private Spinner spinnerStartTimeEvent,spinnerEndTimeEvent,spinnerParticipaintsList;
    private Button nextButtomToGoToFinance;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private TextView selectDateTxt,selectStartTime,selectEndTime,finance_add_one_more_vendor;
    private EditText participaints_num_men,participaints_num_women,participaints_num_child;

    //ArrayLists
    private ArrayList<TextView> selectedEventDates=new ArrayList<>();
    private ArrayList<TextView> selectedEventStartTime=new ArrayList<>();
    private ArrayList<TextView> selectedEventEndTime=new ArrayList<>();

    private ArrayList<EditText> selectedNumOfMen=new ArrayList<>();
    private ArrayList<EditText> selectedNumOfWomen=new ArrayList<>();
    private ArrayList<EditText> selectedNumOfChild=new ArrayList<>();

    private ArrayList<String> selectedFinanceExpenditure=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyName=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyLocation=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyPhone=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyTotalBidding=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyBankNum=new ArrayList<>();
    private ArrayList<String> selectedFinanceCompanyBankIfsc=new ArrayList<>();

    private String selectedItemStartEvent,selectedItemEndEvent;
    public EventFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event_form, container, false);
        // Date Picker

        addEventDateColumn=view.findViewById(R.id.addEventDateColumn);
        addPaticipantsList=view.findViewById(R.id.addPaticipantsList);
        parentLinearLayout=view.findViewById(R.id.parentLinearLayout);
        parentLinearLayout2=view.findViewById(R.id.parentLinearLayout2);
        nextButtomToGoToFinance=view.findViewById(R.id.nextButtomToGoToFinance);
        eventdateLayout=view.findViewById(R.id.eventdateLayout);
        participantMain=view.findViewById(R.id.participantMain);
        financeLayout=view.findViewById(R.id.financeLayout);
        finance_add_one_more_vendor=view.findViewById(R.id.finance_add_one_more_vendor);
        finance_back_button=view.findViewById(R.id.finance_back_button);

        //editTexts finance
        financeExpenditure=view.findViewById(R.id.financeExpenditure);
        financeCompanyname=view.findViewById(R.id.financeCompanyname);
        financeCompanyLocation=view.findViewById(R.id.financeCompanyLocation);
        financeCompanyPhone=view.findViewById(R.id.financeCompanyPhone);
        financeTotalBidding=view.findViewById(R.id.financeTotalBidding);
        financeCompanyBankNum=view.findViewById(R.id.financeCompanyBankNum);
        financeCompanyBankIfsc=view.findViewById(R.id.financeCompanyBankIfsc);


        eventdateLayout.setVisibility(View.VISIBLE);
        participantMain.setVisibility(View.VISIBLE);

        //addEventDate dynamic
        addEventDateColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                if (parentLinearLayout.getChildCount()==0){
                    parentLinearLayout.addView(rowView);
                }else {
                    if (selectDateTxt.getText().toString().contains("Date")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectStartTime.getText().toString().contains("From Time")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectEndTime.getText().toString().contains("To Time")){
                        Toast.makeText(getActivity(),"please fill details first",Toast.LENGTH_LONG).show();
                        return;
                    }
                    parentLinearLayout.addView(rowView);
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
                        parentLinearLayout.removeView((View) v.getParent());
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

        addPaticipantsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.participants_list_dynamic_field, null);
                // Add the new row before the add field button.

                if (parentLinearLayout2.getChildCount()==0){
                    parentLinearLayout2.addView(rowView);
                }else {
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
                    parentLinearLayout2.addView(rowView);
                }

                //initializing dynamic participaints list
                delete_button_participaints=rowView.findViewById(R.id.delete_button_participaints);
                participaints_num_men=rowView.findViewById(R.id.num_men);
                participaints_num_women=rowView.findViewById(R.id.num_women);
                participaints_num_child=rowView.findViewById(R.id.num_child);
                spinnerParticipaintsList=rowView.findViewById(R.id.participaintsSpinner);

                selectedNumOfMen.add(participaints_num_men);
                selectedNumOfWomen.add(participaints_num_women);
                selectedNumOfChild.add(participaints_num_child);

                delete_button_participaints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentLinearLayout2.removeView((View) v.getParent());
                        selectedNumOfMen.remove(participaints_num_men);
                        selectedNumOfWomen.remove(participaints_num_women);
                        selectedNumOfChild.remove(participaints_num_child);
                    }
                });
            }
        });

        nextButtomToGoToFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantMain.setVisibility(View.GONE);
                eventdateLayout.setVisibility(View.GONE);

                financeLayout.setVisibility(View.VISIBLE);
                nextButtomToGoToFinance.setText("Submit ");

                if (selectedEventDates.size()!=0){
                    for (int i=0;i<selectedEventDates.size();i++){
                        Log.e("dates",selectedEventDates.get(i).getText().toString());
                        Log.e("startTime",selectedEventStartTime.get(i).getText().toString()+selectedItemStartEvent);
                        Log.e("endTime",selectedEventDates.get(i).getText().toString()+selectedEventEndTime);
                    }
                }
                if (selectedNumOfMen.size()!=0){
                    for (int i=0;i<selectedNumOfMen.size();i++){
                        Log.e("dates",selectedNumOfMen.get(i).getText().toString());
                        Log.e("startTime",selectedNumOfWomen.get(i).getText().toString());
                        Log.e("endTime",selectedNumOfChild.get(i).getText().toString());
                    }
                }
                if (nextButtomToGoToFinance.getText().toString().contains("Submit")){
                    if (TextUtils.isEmpty(financeExpenditure.getText().toString())){
                        financeExpenditure.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyname.getText().toString())){
                        financeCompanyname.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyLocation.getText().toString())){
                        financeCompanyLocation.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyPhone.getText().toString())){
                        financeCompanyPhone.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeTotalBidding.getText().toString())){
                        financeTotalBidding.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankNum.getText().toString())){
                        financeCompanyBankNum.setError("fields Cannot be Empty");
                        return;
                    }
                    if (TextUtils.isEmpty(financeCompanyBankIfsc.getText().toString())){
                        financeCompanyBankIfsc.setError("fields Cannot be Empty");
                        return;
                    }
                    selectedFinanceExpenditure.add(financeExpenditure.getText().toString());
                    selectedFinanceCompanyName.add(financeCompanyname.getText().toString());
                    selectedFinanceCompanyLocation.add(financeCompanyLocation.getText().toString());
                    selectedFinanceCompanyPhone.add(financeCompanyPhone.getText().toString());
                    selectedFinanceCompanyTotalBidding.add(financeTotalBidding.getText().toString());
                    selectedFinanceCompanyBankNum.add(financeCompanyBankNum.getText().toString());
                    selectedFinanceCompanyBankIfsc.add(financeCompanyBankIfsc.getText().toString());

                    if (selectedFinanceExpenditure.size()>0){
                        for (int i=0;i<selectedFinanceExpenditure.size();i++){
                            Log.e("FinanceExpenditure",selectedFinanceExpenditure.get(i));
                            Log.e("financeCompanyname",selectedFinanceCompanyName.get(i));
                            Log.e("financeCompanyLocation",selectedFinanceCompanyLocation.get(i));
                            Log.e("financeCompanyPhone",selectedFinanceCompanyPhone.get(i));
                            Log.e("financeTotalBidding",selectedFinanceCompanyTotalBidding.get(i));
                            Log.e("financeCompanyBankNum",selectedFinanceCompanyBankNum.get(i));
                            Log.e("financeCompanyBankIfsc",selectedFinanceCompanyBankIfsc.get(i));
                        }
                    }
                }
            }
        });

        finance_add_one_more_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(financeExpenditure.getText().toString())){
                    financeExpenditure.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyname.getText().toString())){
                    financeCompanyname.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyLocation.getText().toString())){
                    financeCompanyLocation.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyPhone.getText().toString())){
                    financeCompanyPhone.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeTotalBidding.getText().toString())){
                    financeTotalBidding.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyBankNum.getText().toString())){
                    financeCompanyBankNum.setError("fields Cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(financeCompanyBankIfsc.getText().toString())){
                    financeCompanyBankIfsc.setError("fields Cannot be Empty");
                    return;
                }
               // finance_back_button.setVisibility(View.VISIBLE);
                financeExpenditure.setText("");
                financeCompanyname.setText("");
                financeCompanyLocation.setText("");
                financeCompanyPhone.setText("");
                financeTotalBidding.setText("");
                financeCompanyBankNum.setText("");
                financeCompanyBankIfsc.setText("");

            }
        });
        finance_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFinanceExpenditure.size()>0){
                    for (int i=0;i<selectedFinanceExpenditure.size();i++){
                        financeExpenditure.setText(selectedFinanceExpenditure.get(0));
                        financeCompanyname.setText(selectedFinanceCompanyName.get(0));
                        financeCompanyLocation.setText(selectedFinanceCompanyLocation.get(0));
                        financeCompanyPhone.setText(selectedFinanceCompanyPhone.get(0));
                        financeTotalBidding.setText(selectedFinanceCompanyTotalBidding.get(0));
                        financeCompanyBankNum.setText(selectedFinanceCompanyBankNum.get(0));
                        financeCompanyBankIfsc.setText(selectedFinanceCompanyBankIfsc.get(0));
                    }
//                    selectedFinanceExpenditure.remove(financeExpenditure);
//                    selectedFinanceCompanyName.remove(financeCompanyname);
//                    selectedFinanceCompanyLocation.remove(financeCompanyLocation);
//                    selectedFinanceCompanyPhone.remove(financeCompanyPhone);
//                    selectedFinanceCompanyTotalBidding.remove(financeTotalBidding);
//                    selectedFinanceCompanyBankNum.remove(financeCompanyBankNum);
//                    selectedFinanceCompanyBankIfsc.remove(financeCompanyBankIfsc);
                }
            }
        });





        return view;
    }
}
