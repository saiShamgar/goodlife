package com.example.sss.goodlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.BankAccountAdapter;
import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.Fragments.HomeFragment;
import com.example.sss.goodlife.Models.BankAccountIds;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Utils.SharedPreferenceConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPageActivity extends AppCompatActivity {
    private EditText agent_login_name,agent_login_pass;
    private Button agent_login_button;

    //Api calls
    private ApiService apiService;
    private ProgressDialog progressDialog;
    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Verifying Details");
        progressDialog.setMessage("Please wait, while we are submitting your details ");
        progressDialog.setCanceledOnTouchOutside(false);

        preferenceConfig=new SharedPreferenceConfig(this);


        agent_login_name=findViewById(R.id.agent_login_name);
        agent_login_pass=findViewById(R.id.agent_login_pass);
        agent_login_button=findViewById(R.id.agent_login_button);


        agent_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(agent_login_name.getText())){
                    agent_login_name.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(agent_login_pass.getText())){
                    agent_login_pass.setError("field cannot be empty");
                    return;
                }
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> financeCall=apiService.goodLifeLogin(
                        agent_login_name.getText().toString(),
                        agent_login_pass.getText().toString());
                financeCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        SharedPreferences pref=getApplicationContext().getSharedPreferences("my_pref",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();

                        String useremail=agent_login_name.getText().toString();
                        String pass=agent_login_pass.getText().toString();
                        editor.putString("useremail",useremail);
                        editor.putString("password",pass);
                        editor.commit();
                        progressDialog.dismiss();
                        Intent agentLogin=new Intent(LoginPageActivity.this, MainActivity.class);
                        startActivity(agentLogin);
                        finish();

                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
