package com.example.sss.goodlife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
               Intent loginpanalActivity = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
               startActivity(loginpanalActivity);
               finish();
            }
        },2000);
    }
}
