package com.example.expense.expensemangementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


//    public static final String KEY_FIRSTNAME = "firstname";
//    public static final String KEY_LASTNAME = "lastname";
//    public static final String KEY_EMAIL = "email";
//    public static final String KEY_MOBILE = "mobile";
//    public static final String KEY_PASSWORD = "password";
//    public static final String KEY_PIN = "pin";

    private EditText editTextFirstname,editTextLastname,editTextPin,editTextMobile,editTextEmail,editTextPassword;

    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFirstname=(EditText)findViewById(R.id.firstName);
        editTextLastname=(EditText)findViewById(R.id.lastName);
        editTextPin=(EditText)findViewById(R.id.pincode);
        editTextMobile=(EditText)findViewById(R.id.mobile);
        editTextEmail=(EditText)findViewById(R.id.emailReg);
        editTextPassword=(EditText)findViewById(R.id.passwordReg);
                register=(Button)findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Intent i=new Intent(getApplicationContext(),Home.class);
//                startActivity(i);
//                Toast.makeText(Register.this, "You are Registered successfully!", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void registerUser() throws JSONException {
        final String REGISTER_URL = "http://phpprojects.checkyourproject.com/ECAPI/web-service/user/register.php?firstname="+editTextFirstname.getText().toString().trim()+"&lastname="+editTextLastname.getText().toString().trim()+"&email+"+editTextEmail.getText().toString().trim()+"&mobile="+editTextMobile.getText().toString().trim()+"&password="+editTextPassword.getText().toString().trim()+"&pin="+editTextPin.getText().toString().trim()+"";
      final String firstname = editTextFirstname.getText().toString().trim();
//        final String lastname = editTextLastname.getText().toString().trim();
//        final String pin = editTextPin.getText().toString().trim();
//        final String mobile = editTextMobile.getText().toString().trim();
//        final String email = editTextEmail.getText().toString().trim();
//        final String password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i=new Intent(getApplicationContext(),Home.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),""+firstname.toUpperCase()+"! You are successfully Registered with us", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put(KEY_FIRSTNAME, firstname);
//                params.put(KEY_LASTNAME, lastname);
//                params.put(KEY_EMAIL, email);
//                params.put(KEY_MOBILE,mobile);
//                params.put(KEY_PASSWORD, password);
//                params.put(KEY_PIN, pin);
//

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }



}
