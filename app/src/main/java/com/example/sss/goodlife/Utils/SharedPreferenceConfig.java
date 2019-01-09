package com.example.sss.goodlife.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.sss.goodlife.R;


public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context){
        this.context = context;
    }
    public void writeLogin(String login){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.login_preference), login);
        Log.i("SharedPreferanceWrite: ",""+login);
        editor.commit();
    }

    public String readLogin(){
        String login;
        login = sharedPreferences.getString(context.getResources().getString(R.string.login_preference),"no");
        Log.i("SharedPreferanceRead: ",""+login);
        return login;
    }

}
