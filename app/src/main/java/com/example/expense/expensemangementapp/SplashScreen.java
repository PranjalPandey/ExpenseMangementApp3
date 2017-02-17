package com.example.expense.expensemangementapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT= 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                String getUserLoginStatus = pref.getString("UserLogInStatus", null);
                if(getUserLoginStatus == null || getUserLoginStatus.equals("Logged Out") ){

                    Intent i = new Intent(SplashScreen.this,Home.class);
                    startActivity(i);
                    finish();
                }
                else {

                    Intent i = new Intent(SplashScreen.this,Home.class);
                    startActivity(i);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);
    }

}
