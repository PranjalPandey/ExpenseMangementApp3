package com.example.expense.expensemangementapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

public class MyList extends AppCompatActivity {
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        myList();
    }


    public void myList(){
        // Array of strings...
      // String[] categoriesArray = {"Bills","EMI","Entertainment","Food & Drink","Fuel","Groceries","Health","Investment","Shopping","Travel"};
       db = new DatabaseHandler(this);
       // MyList myList = new MyList();
        List<Expense> expenses = db.getAllExpense();

        MyListAdapter ca = new MyListAdapter(this,expenses);
      //  Log.d("sent",""+ca.getCount());
        ListView lv = (ListView) findViewById(R.id.myList);
        lv.setAdapter(ca);

    }
}