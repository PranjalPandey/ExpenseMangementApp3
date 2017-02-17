package com.example.expense.expensemangementapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.expense.expensemangementapp.R.id.mobile;
import static com.example.expense.expensemangementapp.R.id.password;

public class UserLogin extends AppCompatActivity {
    Button signup,login;
    ProgressDialog Dialog;
public static String STATUS="true";
    // public static String LOGIN_URL = "http://cobaltqa.daxima.com/api/InitiationSurvey/SurveySession/SessionSurveyLogin?username=testbal1&password=Test1234\"";
//   public static final String KEY_MOBILE="?mobile=";
//      public static final String KEY_PASSWORD="&password=";

    private EditText editTextMobile;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Dialog = new ProgressDialog(UserLogin.this);

        editTextMobile= (EditText) findViewById(mobile);
        editTextPassword = (EditText) findViewById(password);

        signup = (Button) findViewById(R.id.btn_signup);
        login = (Button) findViewById(R.id.btn_login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }


    private void userLogin() {
        Dialog.setMessage("Please wait..");
        Dialog.show();


      String LOGIN_URL="http://phpprojects.checkyourproject.com/ECAPI/web-service/user/login.php?mobile="+editTextMobile.getText().toString().trim()+"&password="+editTextPassword.getText().toString().trim()+"";
//        String mobile = editTextMobile.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject myJson = null;
                        try {

                            myJson = new JSONObject(response);
                           String loginString = myJson.optString("login");

                            Log.d("login",loginString);
                            if( loginString.length() == 0){
                                Dialog.dismiss();
                                Toast.makeText(UserLogin.this,"Invalid Username/Password", Toast.LENGTH_LONG).show();



                            }
                          else {
                                myJson = new JSONObject(loginString);
                                String user_id = myJson.optString("user_id");

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("UserLogInStatus", "Logged In");
                                editor.putString("User_Id", user_id);
                                editor.commit();
                                Dialog.dismiss();

                               Intent homeActivity = new Intent(getApplicationContext(),Home.class);
                               startActivity(homeActivity);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }








                       /* if(response.contains(STATUS)){
                            Intent i=new Intent(getApplicationContext(),Home.class);
                            startActivity(i);
                            openProfile();
                            finish();
                        }else{
                            Toast.makeText(UserLogin.this,response, Toast.LENGTH_LONG).show();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog.dismiss();
                        Toast.makeText(UserLogin.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
//                map.put(KEY_MOBILE,mobile);
//                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void openProfile(){
        Toast.makeText(UserLogin.this, "Welcome USer!!", Toast.LENGTH_SHORT).show();
    }




}
