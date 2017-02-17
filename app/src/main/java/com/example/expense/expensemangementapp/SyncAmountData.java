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

public class SyncAmountData {


    public void getAmountData(final Context context, final ProgressDialog Dialog) {


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String user_id = pref.getString("User_Id", null);

        String AMOUNT_URL = "http://phpprojects.checkyourproject.com/ECAPI/web-service/transactions/GET.php?user_id=" + user_id;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AMOUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject myJson = null;

                        try {
                            myJson = new JSONObject(response);
                            String name = myJson.optString("transactions");
                            myJson = new JSONObject(name);

                            Log.d("my", myJson.toString());
                            DatabaseHandler dbHandler = new DatabaseHandler(context);
                            JSONArray jsonarray = myJson.optJSONArray("data");
                            //for (int i = 0; i < jsonarray.length(); i++) {
                            if(jsonarray.length()>3){
                            for (int i = 0; i < 4; i++) {
                                JSONObject jsonChildNode = jsonarray.getJSONObject(i);
                                //****** Creates a new JSONObject with name/value mappings from the JSON string. ********//*

                                //******* Fetch node values **********//**//*
                                String category_Id = jsonChildNode.optString("category_id");
                                String category_Name = jsonChildNode.optString("category_name");
                                String debit = jsonChildNode.optString("debit");
                                String credit = jsonChildNode.optString("credit");
                                String transaction_time = jsonChildNode.optString("transaction_time");
                                dbHandler.addSyncedExpense(Integer.parseInt(category_Id), category_Name, transaction_time, Integer.parseInt(debit), "", Integer.parseInt(credit), i);
                            } }

                            else {
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonChildNode = jsonarray.getJSONObject(i);
                                    //****** Creates a new JSONObject with name/value mappings from the JSON string. ********//*

                                    //******* Fetch node values **********//**//*
                                    String category_Id = jsonChildNode.optString("category_id");
                                    String category_Name = jsonChildNode.optString("category_name");
                                    String debit = jsonChildNode.optString("debit");
                                    String credit = jsonChildNode.optString("credit");
                                    String transaction_time = jsonChildNode.optString("transaction_time");
                                    dbHandler.addSyncedExpense(Integer.parseInt(category_Id), category_Name, transaction_time, Integer.parseInt(debit), "", Integer.parseInt(credit), i);
                                }
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


    public void postAmountData(final Context context, final ProgressDialog Dialog, final String categoryId, final String debitamount, final String creditAmount, final String categoryNameList) {

        final String categoryname = "";
        final String otherinfo = "";
        final String mobile_datetime = "";
        final String user_id = "";
        Dialog.setMessage("Please wait..");
        Dialog.show();

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        final String user_id2 = pref.getString("User_Id", null);

        String replaceCategoriesName = replace(categoryNameList);
        String POST_EXPENSESLIST_URL = "http://phpprojects.checkyourproject.com/ECAPI/web-service/transactions/POST.php?user_id=" + user_id2 + "&category_id=" + categoryId + "&debit=" + debitamount + "&credit=" + creditAmount + "&category_name=" + replaceCategoriesName;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_EXPENSESLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //syncCategory(context,Dialog);
                        Log.d("responseString", response);
                        getAmountData(context, Dialog);
                        // Toast.makeText(context, "Expenses Synced Successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog.dismiss();
                        Log.d("errorString", error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {


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
