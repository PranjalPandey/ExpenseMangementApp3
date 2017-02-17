package com.example.expense.expensemangementapp;

/*
* Developed By : Nitin
* Date : 20-July-2016
* Time : 16:00
*/

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Categories extends Activity {
    FloatingActionButton floatingButton;
    View popupView;PopupWindow popupWindow;
    SharedPreferences pref;
    ArrayAdapter adapter;ListView listView;
    List<String> categoriesArray_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        floatingButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });
        categoriesList();
    }

    public void openPopup(){
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.setbudget_popup_layout, null);
        popupWindow = new PopupWindow(popupView, 700, 600);

        Button popup_Cancel = (Button)popupView.findViewById(R.id.cancelButton2);
        popup_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 10, -30);


        Button popup_Save = (Button)popupView.findViewById(R.id.saveButton2);
        popup_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText budgetEdit = (EditText)popupView.findViewById(R.id.monthlybudget);
                int budget = Integer.parseInt(budgetEdit.getText().toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("budget", budget);
                editor.commit();

                Toast.makeText(getApplicationContext(),"Your Budget Saved Successfully",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });


    }

    public void categoriesList(){
        // Array of strings...
        String[] categoriesArray = {"Bills","EMI","Entertainment","Food & Drink","Fuel","Groceries","Health","Investment","Shopping","Travel"};

         categoriesArray_list = new ArrayList<String>(Arrays.asList(categoriesArray));
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, categoriesArray_list);

         listView = (ListView) findViewById(R.id.categories_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Categories.this,CreateCategory.class);
                String selectedFromList = (String) listView.getItemAtPosition(position);

                intent.putExtra("message", selectedFromList);


                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addcategory, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_category) {
           openCategoryPopup();
        }


        return super.onOptionsItemSelected(item);
    }


    public void openCategoryPopup(){
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_add_category, null);
        popupWindow = new PopupWindow(popupView, 700, 600);

        Button popup_Cancel = (Button)popupView.findViewById(R.id.cancelButton3);
        popup_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 10, -30);


        Button popup_Save = (Button)popupView.findViewById(R.id.saveButton3);
        popup_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText budgetEdit = (EditText)popupView.findViewById(R.id.categoryedit);
                String category = budgetEdit.getText().toString();

                categoriesArray_list.add(category);
                adapter.notifyDataSetChanged();


                Toast.makeText(getApplicationContext(),"New Category Added Successfully",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });


    }





}
