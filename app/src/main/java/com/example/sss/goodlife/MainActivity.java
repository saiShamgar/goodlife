package com.example.sss.goodlife;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sss.goodlife.Fragments.EventFormFragment;
import com.example.sss.goodlife.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private TextView EventFormTxt,ReportFormTxt;
    private FrameLayout framContainer;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventFormTxt=findViewById(R.id.EventFormTxt);
        ReportFormTxt=findViewById(R.id.ReportFormTxt);
        framContainer=findViewById(R.id.frameContainer);
        drawer_layout=findViewById(R.id.drawer_layout);
        toolbarDrawer=findViewById(R.id.toolbarDrawer);
        mToggle=new ActionBarDrawerToggle(this,drawer_layout,R.string.open,R.string.close);
        drawer_layout.addDrawerListener(mToggle);
        setSupportActionBar(toolbarDrawer);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        EventFormTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFormTxt.setTextColor(Color.CYAN);
                ReportFormTxt.setTextColor(Color.WHITE);

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                EventFormFragment fragment=new EventFormFragment();
                transaction.replace(R.id.frameContainer, fragment);
                transaction.commit();

            }
        });
        ReportFormTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFormTxt.setTextColor(Color.WHITE);
                ReportFormTxt.setTextColor(Color.CYAN);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                HomeFragment fragment=new HomeFragment();
                transaction.replace(R.id.frameContainer, fragment);
                transaction.commit();
            }
        });
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
