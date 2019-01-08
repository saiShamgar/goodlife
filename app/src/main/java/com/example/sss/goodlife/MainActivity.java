package com.example.sss.goodlife;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Fragments.DayWiseReportFragment;
import com.example.sss.goodlife.Fragments.EventFormFragment;
import com.example.sss.goodlife.Fragments.HomeFragment;
import com.example.sss.goodlife.Fragments.ReviewsFragment;
import com.example.sss.goodlife.Fragments.VBSProgramApplication;
import com.example.sss.goodlife.Fragments.VbsFinanceApplication;
import com.example.sss.goodlife.Fragments.VbsFinanceReport;
import com.example.sss.goodlife.Fragments.VbsTransportApplication;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView EventFormTxt,ReportFormTxt;
    private FrameLayout framContainer;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EventFormTxt=findViewById(R.id.EventFormTxt);
//        ReportFormTxt=findViewById(R.id.ReportFormTxt);
        framContainer=findViewById(R.id.frameContainer);
        drawer_layout=findViewById(R.id.drawer_layout);

        nav_view=findViewById(R.id.nav_view);
        nav_view.bringToFront();
        mToggle=new ActionBarDrawerToggle(this,drawer_layout,R.string.open,R.string.close);
        drawer_layout.addDrawerListener(mToggle);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HomeFragment fragment=new HomeFragment();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();



        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                item.setChecked(true);
                drawer_layout.closeDrawers();
                if (id==R.id.menu_finance_application)
                {
                    Toast.makeText(getApplicationContext(),"dfdf",Toast.LENGTH_SHORT).show();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    VbsFinanceApplication fragment=new VbsFinanceApplication();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_event_form)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    VBSProgramApplication fragment=new VBSProgramApplication();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_event_Home)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    HomeFragment fragment=new HomeFragment();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_transport_application)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    VbsTransportApplication fragment=new VbsTransportApplication();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_report_form)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    DayWiseReportFragment fragment=new DayWiseReportFragment();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_finance_reprot_form)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    VbsFinanceReport fragment=new VbsFinanceReport();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                if (id==R.id.menu_review_submit)
                {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    ReviewsFragment fragment=new ReviewsFragment();
                    transaction.replace(R.id.frameContainer, fragment);
                    transaction.commit();
                }
                return true;
            }
        });

//        EventFormTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventFormTxt.setTextColor(Color.CYAN);
//                ReportFormTxt.setTextColor(Color.WHITE);
//
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                VBSProgramApplication fragment=new VBSProgramApplication();
//                transaction.replace(R.id.frameContainer, fragment);
//                transaction.commit();
//
//            }
//        });
//        ReportFormTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventFormTxt.setTextColor(Color.WHITE);
//                ReportFormTxt.setTextColor(Color.CYAN);
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                HomeFragment fragment=new HomeFragment();
//                transaction.replace(R.id.frameContainer, fragment);
//                transaction.commit();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu_items, menu);
        return  super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }


}
