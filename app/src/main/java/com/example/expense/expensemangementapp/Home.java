package com.example.expense.expensemangementapp;

/*
* Developed By : Nitin
* Date : 20-July-2016
* Time : 17:00
*/

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements OnChartValueSelectedListener {
    Button budgetButton, saveButton, addMoreBudget;
    String budget, label;
    int sum = 0;
    View popupView;
    PopupWindow popupWindow;
    private PieChart mChart;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    private Typeface tf;

    ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Dialog = new ProgressDialog(Home.this);
        AutoCompleteTextView autoCompleteCategory = (AutoCompleteTextView) findViewById(R.id.autoCompleteCategory);
        autoCompleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCollapsingToolbar();
                setAppBar();
            }
        });
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> categories = dbHandler.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            Log.i(this.toString(), categories.get(i));
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, categories);
        autoCompleteCategory.setAdapter(adapter);

        final AppBarLayout appbar=(AppBarLayout)findViewById(R.id.app_bar);
        budgetButton = (Button) findViewById(R.id.addBudgetButton2);
        saveButton = (Button) findViewById(R.id.saveButton1);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
        addMoreBudget = (Button) findViewById(R.id.addMoreBudget);
        addMoreBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopup2();
            }
        });
        EditText expenseDate = (EditText) findViewById(R.id.datePicker1);


        String ct = DateFormat.getDateInstance().format(new Date());
        expenseDate.setText(ct);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.pieChartLayout);
                if (hiddenPanel.getVisibility() == View.VISIBLE) {

                    Animation bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);


                    hiddenPanel.startAnimation(bottomDown);
                    hiddenPanel.setVisibility(View.INVISIBLE);

                } else {
                    generatePieChart();
                    Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);


                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                    hiddenPanel.bringToFront();
                }
            }
        });


        dbHandler = new DatabaseHandler(getApplicationContext());
        budget = dbHandler.getBudget();


        if (budget != null || budget != "") ;
        {
            budgetButton.setText("Budget  ₹ " + budget);
        }

        List amountsList = dbHandler.getAmount();

        if (amountsList.size() > 0) {
            for (int i = 0; i < amountsList.size(); i++) {

                sum = sum + Integer.parseInt(amountsList.get(i).toString());
                Log.d("amounts", amountsList.get(i).toString());
            }


          /*  if (sum < Integer.parseInt(budget)) {*/
            TextView spendsValue = (TextView) findViewById(R.id.spendsValue);
            spendsValue.setText(" ₹ " + sum);
            if (budget.equals("")) {
            } else {
                int remainingvalue = Integer.parseInt(budget) - sum;
                TextView remainingValue = (TextView) findViewById(R.id.remainingBudget);
                remainingValue.setText(" ₹ " + remainingvalue);
            }

           /* } else {

                Toast.makeText(getApplicationContext(), "Your Expenses Cannot be Greater than Budget.", Toast.LENGTH_SHORT).show();
            }*/

        }

        budgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });


    }
    public void setAppBar() {
        final AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (appbarLayout.getTop() < 0)
        {
            appbarLayout.setExpanded(true);
            toolbar.setTitle("");
        }
        else
            appbarLayout.setExpanded(false);
        toolbar.setTitle("Expense Calculator");
    }

    private void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));


                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }




    public void openPopup() {

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.setbudget_popup_layout, null);
        popupWindow = new PopupWindow(popupView, 700, 700);
        popupWindow.setHeight(450);
        Button popup_Cancel = (Button) popupView.findViewById(R.id.cancelButton2);
        popup_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        Button popup_Save = (Button) popupView.findViewById(R.id.saveButton2);
        popup_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    EditText budgetEdit = (EditText) popupView.findViewById(R.id.monthlybudget);
                    int budgetPopup = Integer.parseInt(budgetEdit.getText().toString());


                    if (budgetPopup < sum) {
                        Toast.makeText(getApplicationContext(), "Your Budget Cannot be Less Than Your Expenses.", Toast.LENGTH_SHORT).show();

                    } else if (budgetPopup < 1000000) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage("Enter amount under 10 lacs!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

                        dbHandler.addBudget("" + budgetPopup);
                        budgetButton.setText("Budget ₹ " + budgetPopup);
                        int remainingvalue = budgetPopup - sum;
                        TextView remainingValue = (TextView) findViewById(R.id.remainingBudget);
                        remainingValue.setText("₹ " + remainingvalue);


                        budget = "" + budgetPopup;
                        Toast.makeText(getApplicationContext(), "Your Budget Saved Successfully", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(Home.this, "Please! Enter a valid Budget Amount!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void openPopup2() {

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        popupView = layoutInflater.inflate(R.layout.setbudget_popup_layout, null);
        popupWindow = new PopupWindow(popupView, 700, 700);

        Button popup_Cancel = (Button) popupView.findViewById(R.id.cancelButton2);
        popup_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setHeight(450);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        Button popup_Save = (Button) popupView.findViewById(R.id.saveButton2);
        popup_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    EditText budgetEdit = (EditText) popupView.findViewById(R.id.monthlybudget);
                    int budgetPopup = Integer.parseInt(budgetEdit.getText().toString());
                    int newBudget = Integer.parseInt(budget) + budgetPopup;


                    DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                    dbHandler.addBudget("" + newBudget);
                    budgetButton.setText("Budget ₹ " + newBudget);
                    int remainingvalue = newBudget - sum;
                    TextView remainingValue = (TextView) findViewById(R.id.remainingBudget);
                    remainingValue.setText("₹ " + remainingvalue);


                    budget = "" + budgetPopup;
                    Toast.makeText(getApplicationContext(), "Your Budget Saved Successfully", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();

                }catch(NumberFormatException e){
                    Toast.makeText(Home.this, "Please! Enter a valid Budget amount!", Toast.LENGTH_SHORT).show();        }     }

        });


    }



    public void saveExpense() {

        EditText autocompleteCategory = (EditText) findViewById(R.id.autoCompleteCategory);
        EditText datePickerEdit = (EditText) findViewById(R.id.datePicker1);
        EditText valueEdit = (EditText) findViewById(R.id.editValueEdit1);
        EditText OtherInformationEdit = (EditText) findViewById(R.id.OtherInformation);
        //   Bundle gt = getIntent().getExtras();
        //   String category = gt.getString("message");
        //   Log.d("ItemClicked", category);


        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String category = autocompleteCategory.getText().toString();
        String date = datePickerEdit.getText().toString();
        String OtherInformation = OtherInformationEdit.getText().toString();
        int value = Integer.parseInt(valueEdit.getText().toString());

        if (value + sum > Integer.parseInt(budget)) {
            Toast.makeText(getApplicationContext(), "Your Expenses Cannot Exceed Your Budget", Toast.LENGTH_SHORT).show();

        } else {
            Expense exp = new Expense(0, category, date, value, OtherInformation, 0);
            DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
           String check= dbHandler.checkExpenseExists(category);
            if (check == "Success") {//Update value
               int newValue=value+ dbHandler.get_amount(category);
            dbHandler.updateExpense(newValue,category);
            } else {
                dbHandler.addExpense(exp);
            }
            // dbHandler.addCategoryList();
            String result = dbHandler.checkCategoryExists(category);

            if (result == "Success") {
                Log.d("inside success", "success");
            } else {
                Log.d("inside failure", "failure");
                dbHandler.addCategoryList("7", category, 1);


            }


            List amountsList = dbHandler.getAmount();
            sum = 0;
            if (amountsList.size() > 0) {
                for (int i = 0; i < amountsList.size(); i++) {

                    sum = sum + Integer.parseInt(amountsList.get(i).toString());

                }
                TextView spendsValue = (TextView) findViewById(R.id.spendsValue);
                spendsValue.setText("₹ " + sum);
            }


            Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            autocompleteCategory.setText("");

            valueEdit.setText("");
            OtherInformationEdit.setText("");

            int remainingvalue = Integer.parseInt(budget) - sum;
            TextView remainingValue = (TextView) findViewById(R.id.remainingBudget);
            remainingValue.setText("₹ " + remainingvalue);
        }
        // Intent intent = new Intent(getApplication(), MainActivity.class);
        // startActivity(intent);

    }


    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

//SyncCategoryData data= new SyncCategoryData();
            try {
                syncCategory();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.action_logout) {

            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // Cameras.super.onBackPressed();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("UserLogInStatus", "Logged Out");

                            editor.commit();

                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            System.exit(0);
                        }
                    }).create().show();
            return true;
        }

        if (id == R.id.action_expenses) {
            String _idList = "", creditList = "", debitAmountList = "", categoryNameList = "";
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            List<Expense> expenses = db.getAllExpense();

            for (Expense cn : expenses) {

                if (_idList == "") {
                    _idList = "" + cn.get_id();
                    creditList = "" + cn.getCreditAmount();
                    debitAmountList = "" + cn.get_amount();
                    categoryNameList = cn.get_category();
                } else {
                    _idList = _idList + "," + cn.get_id();
                    creditList = creditList + "," + cn.getCreditAmount();
                    debitAmountList = debitAmountList + "," + cn.get_amount();
                    categoryNameList = categoryNameList + "," + cn.get_category();


                }
            }
            SyncAmountData data = new SyncAmountData();
            data.postAmountData(getApplicationContext(), Dialog, _idList, debitAmountList, creditList, categoryNameList);


            return true;
        }
        if(id==R.id.action_Database){
            Intent i=new Intent(this,AndroidDatabaseManager.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }


    public void generatePieChart() {

        mChart = (PieChart) findViewById(R.id.chart2);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //    mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> categories = dbHandler.getCategories();
        if (categories.size() == 0) {
            mChart.setCenterText(generateCenterSpannableText("No Data Available"));
        } else {
            mChart.setCenterText(generateCenterSpannableText("Expense Management"));
        }

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setPieChartData(3, 100);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextColor(Color.WHITE);


    }

    private void setPieChartData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> categories = dbHandler.getCategories();

       /* for (int i = 0; i < categories.size(); i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }*/

        List amountsList = dbHandler.getAmount();

        for (int i = 0; i < amountsList.size(); i++) {
            //  yVals1.add(new Entry((float) (Integer.parseInt(amountsList.get(i).toString()) * mult) + mult / 5, i));
            yVals1.add(new Entry((float) (Integer.parseInt(amountsList.get(i).toString()) * mult), i));
            //yVals1.add(new Entry((float)amountsList.get(i)));
            // sum = sum + Integer.parseInt(amountsList.get(i).toString());
            // Log.d("amounts", amountsList.get(i).toString());
        }


        ArrayList<String> xVals = new ArrayList<String>();


        for (int i = 0; i < categories.size(); i++) {
            xVals.add(categories.get(i % categories.size()));
        }

        // for (int i = 0; i < count + 1; i++)
        //  xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        //  for (int c : ColorTemplate.rgb("#CFF8F6"))
        colors.add(ColorTemplate.rgb("#5B9BD5"));


        // for (int c : ColorTemplate.JOYFUL_COLORS)
        colors.add(ColorTemplate.rgb("#A5A5A5"));

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        //   for (int c : ColorTemplate.LIBERTY_COLORS)
        // colors.add(c);
        colors.add(ColorTemplate.rgb("#ED7D31"));

        //  for (int c : ColorTemplate.PASTEL_COLORS)
        colors.add(ColorTemplate.rgb("#ED7D31"));

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String centerText) {

        SpannableString s = new SpannableString(centerText + "\n");
        //  s.setSpan(new RelativeSizeSpan(1.7f), 0, 20, 0);
        //  s.setSpan(new StyleSpan(Typeface.NORMAL), 4, s.length() - 15, 0);
        //  s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length() - 15, 0);
        //  s.setSpan(new RelativeSizeSpan(.8f), 4, s.length() - 15, 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    public void syncExpenses() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // SyncAmountData syncData = new SyncAmountData();
        //   syncData.postAmountData(getApplicationContext(),Dialog);


    }


    String categoriesNameList = "";

    public void syncCategory() throws UnsupportedEncodingException {
        Log.d("inside", "yup");
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        ArrayList<String> categoriesName = db.getCategories_CategoriesList();
        for (int i = 0; i < categoriesName.size(); i++) {
            if (categoriesNameList == "") {
                categoriesNameList = categoriesName.get(i);
            } else {
                categoriesNameList = categoriesNameList + "," + categoriesName.get(i);
            }
        }

       /* List<Expense> expenses = db.getAllExpense();
        for (Expense cn : expenses) {
            String log = "CategoryId: "+cn.get_id()+" ,Credit: " + cn.get_amount() + " ,Date: " + cn.get_date();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        SyncCategoryData syncData = new SyncCategoryData();
        syncData.postCategoryData(getApplicationContext(), Dialog, categoriesNameList, "", currentDateandTime);


    }
}