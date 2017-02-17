package com.example.expense.expensemangementapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncCategoryData {

    public void syncCategory(final Context context, final ProgressDialog Dialog) {



        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String user_id = pref.getString("User_Id", null);

        String CATEGORY_URL = "http://phpprojects.checkyourproject.com/ECAPI/web-service/category/get_categories.php?user_id=" + user_id;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, CATEGORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject myJson = null;

                        try {
                            myJson = new JSONObject(response);
                            String name = myJson.optString("category");
                            myJson = new JSONObject(name);


                            DatabaseHandler dbHandler = new DatabaseHandler(context);
                            JSONArray jsonarray = myJson.optJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonChildNode = jsonarray.getJSONObject(i);
                                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/

                                //******* Fetch node values **********//*
                                String category_Id = jsonChildNode.optString("category_id");
                                String category_Name = jsonChildNode.optString("category_name");

                                dbHandler.addCategoryList(category_Id, category_Name, i);
                            }
                            Dialog.dismiss();
                            Toast.makeText(context, "Categories Synced Successfully", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //  Log.d("category",data);
                        //int profileIconId = myJson.optInt("profileIconId");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
//                map.put(KEY_MOBILE,mobile);
//                map.put(KEY_PASSWORD,password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    public void postCategoryData(final Context context, final ProgressDialog Dialog, final String categoriesName, final String otherinfo2, final String date) {

        final String categoryname = "";
        final String otherinfo = "" ;
        final String mobile_datetime = "";
        final String user_id = "";
        Dialog.setMessage("Please wait..");
        Dialog.show();

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        final String user_id2 = pref.getString("User_Id", null);
String date3 = "0,0";
String replaceCategoriesName  = replace(categoriesName);
        String POST_CATEGORYLIST_URL = "http://phpprojects.checkyourproject.com/ECAPI/web-service/category/add_categories.php?sync=true&user_id="+user_id2+"&categoryname="+replaceCategoriesName+"&otherinfo=&mobile_datetime="+date3;



        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_CATEGORYLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        syncCategory(context,Dialog);
                        Log.d("responseString",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog.dismiss();
                        Log.d("errorString",error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
          /*  @Override
            protected Map<String, String> getParams() {
                Log.d("user_id",user_id2);
                Log.d("categoryname",categoriesName);
                Log.d("otherinfo",otherinfo2);
                Log.d("mobile_datetime",date);
                Map<String, String> params = new HashMap<String, String>();
                params.put(user_id,user_id2);
                params.put(categoryname, categoriesName);
                params.put(otherinfo, "empty");
                params.put(mobile_datetime, "0,0");
                return params;
            }*/


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    public String replace(String str) {
        String[] words = str.split(" ");
        StringBuilder sentence = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; ++i) {
            sentence.append("%20");
            sentence.append(words[i]);
        }

        return sentence.toString();
    }

}
