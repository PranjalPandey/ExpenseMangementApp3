package com.example.expense.expensemangementapp;

/*
* Developed By : Nitin
* Date : 19-July-2016
* Time : 13:00
*/

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateCategory extends AppCompatActivity {
    EditText datePickerEdit;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        saveButton = (Button) findViewById(R.id.saveButton);
        datePickerEdit = (EditText) findViewById(R.id.datePicker);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
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
        new DatePickerDialog(CreateCategory.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    public void saveExpense() {
        Log.d("saveExpenses", "saved");
        DatabaseHandler db = new DatabaseHandler(this);
        EditText titleEdit = (EditText) findViewById(R.id.titleEdit);
        EditText datePickerEdit = (EditText) findViewById(R.id.datePicker);
        EditText valueEdit = (EditText) findViewById(R.id.editValueEdit);

        Bundle gt = getIntent().getExtras();
        String category = gt.getString("message");
        Log.d("ItemClicked", category);


        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String title = titleEdit.getText().toString();
        String date = datePickerEdit.getText().toString();
        int value = Integer.parseInt(valueEdit.getText().toString());
        Expense exp = new Expense(0,category, date, value,"otherinformation",0);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.addExpense(exp);

        Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);

    }


/*  datePickerEdit.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    });*/

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePickerEdit.setText(sdf.format(myCalendar.getTime()));
    }


}
