package com.example.expense.expensemangementapp;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText datePickerEdit;int sum;
    Button saveButton;String label;
    View popupView;PopupWindow popupWindow;Button budgetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //  SharedPreferences   pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        //  SharedPreferences.Editor editor = pref.edit();

        //  int getBudget = pref.getInt("budget", 0);

        //  Toast.makeText(getApplicationContext(),""+getBudget,Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        budgetButton = (Button)findViewById(R.id.addBudgetButton);

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
       String budget =dbHandler.getBudget();
        if(budget != null || budget!= "");{
            Log.d("budget","abc");
        budgetButton.setText("Budget Rs "+budget);}

        List amountsList = dbHandler.getAmount();

        if(amountsList.size() > 0) {
            for (int i = 0; i < amountsList.size(); i++) {

                sum = sum+ Integer.parseInt(amountsList.get(i).toString());
                Log.d("amounts", amountsList.get(i).toString());
            }
            TextView spendsValue = (TextView)findViewById(R.id.spendsValue);
            spendsValue.setText("Rs "+sum);
        }




        budgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        }); }
     /*   Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
      //  String[] items = new String[]{"1", "2", "three"};
        String[] items = {"Bills","EMI","Entertainment","Food & Drink","Fuel","Groceries","Health","Investment","Shopping","Travel"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        saveButton = (Button) findViewById(R.id.saveButton1);
        datePickerEdit = (EditText) findViewById(R.id.datePicker1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });



        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner1);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // On selecting a spinner item
                label = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
             //   Toast.makeText(parent.getContext(), "You selected: " + label,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
*/
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
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 10, -40);


        Button popup_Save = (Button)popupView.findViewById(R.id.saveButton2);
        popup_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText budgetEdit = (EditText)popupView.findViewById(R.id.monthlybudget);
                int budget = Integer.parseInt(budgetEdit.getText().toString());
             //   SharedPreferences.Editor editor = pref.edit();
             //   editor.putInt("budget", budget);
             //   editor.commit();


                DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                dbHandler.addBudget(""+budget);
                budgetButton.setText("Rs "+budget);
                Toast.makeText(getApplicationContext(),"Your Budget Saved Successfully",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });


    }
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void openDatePicker(View v) {
        new DatePickerDialog(MainActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    public void saveExpense() {

        EditText titleEdit = (EditText) findViewById(R.id.titleEdit1);
        EditText datePickerEdit = (EditText) findViewById(R.id.datePicker1);
        EditText valueEdit = (EditText) findViewById(R.id.editValueEdit1);

     //   Bundle gt = getIntent().getExtras();
     //   String category = gt.getString("message");
     //   Log.d("ItemClicked", category);



        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String title = titleEdit.getText().toString();
        String date = datePickerEdit.getText().toString();
        int value = Integer.parseInt(valueEdit.getText().toString());
        Expense exp = new Expense(0,label,  date, value,"other info",0);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.addExpense(exp);

        List amountsList = dbHandler.getAmount();
sum=0;
        if(amountsList.size() > 0) {
            for (int i = 0; i < amountsList.size(); i++) {

                sum = sum+ Integer.parseInt(amountsList.get(i).toString());

            }
            TextView spendsValue = (TextView)findViewById(R.id.spendsValue);
            spendsValue.setText("Rs "+sum);
        }



        Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
        titleEdit.setText("");
        datePickerEdit.setText("");
        valueEdit.setText("");

        // Intent intent = new Intent(getApplication(), MainActivity.class);
        // startActivity(intent);

    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePickerEdit.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_categories) {
            Intent intent = new Intent(MainActivity.this, Categories.class);
            startActivity(intent);
        } else if (id == R.id.nav_myList) {
            Intent intent = new Intent(MainActivity.this, MyList.class);
            startActivity(intent);
        } else if (id == R.id.nav_graph) {
            Intent intent = new Intent(MainActivity.this, Graphical_Representation_Expenses.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
