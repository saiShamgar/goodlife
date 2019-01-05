package com.example.sss.goodlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginPageActivity extends AppCompatActivity {
    private TextView txt_agent_signUp;
    private EditText agent_login_name,agent_login_pass;
    private Button agent_login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        txt_agent_signUp=findViewById(R.id.txt_agent_signUp);
        agent_login_name=findViewById(R.id.agent_login_name);
        agent_login_pass=findViewById(R.id.agent_login_pass);
        agent_login_button=findViewById(R.id.agent_login_button);


        agent_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent agentLogin=new Intent(LoginPageActivity.this, MainActivity.class);
                        startActivity(agentLogin);
                        finish();
            }
        });

    }
}
