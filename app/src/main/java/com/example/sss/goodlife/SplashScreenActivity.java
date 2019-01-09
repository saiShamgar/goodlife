package com.example.sss.goodlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.sss.goodlife.Utils.SharedPreferenceConfig;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferenceConfig preferenceConfig;

    private String user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferenceConfig=new SharedPreferenceConfig(this);
        SharedPreferences pref=getApplicationContext().getSharedPreferences("my_pref",MODE_PRIVATE);
        user=pref.getString("useremail",null);
        pass=pref.getString("password",null);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(user!=null&& pass!=null)
                {
                    Intent loggedin = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(loggedin);
                    finish();
                }
                else
                {
                    Intent loginpanalActivity = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                    startActivity(loginpanalActivity);
                    finish();


                }
            }
        },2000);
    }
}
