package com.example.expense.expensemangementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    private static final String DATABASE_NAME = "ExpenseManager";

    // Expenses table name
    private static final String TABLE_EXPENSES = "expenses";

    // Budget table name
    private static final String TABLE_BUDGET = "budget";
    private static final String TABLE_CATEGORYLIST = "categorylist";

    private static final String KEY_CATEGORYLIST_ID = "categorylist_id";
    private static final String KEY_CATEGORYLIST_NAME = "categorylist_name";


    // Contacts Table Columns names
    private static final String KEY_CATEGORYID = "category_id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_OTHERINFORMATION = "otherInformation";
    private static final String KEY_CREDITAMOUNT = "creditAmount";


    private static final String KEY_BUDGET = "budget";
    private static final String KEY_Date = "date";
    private static final String KEY_Amount ="amount";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_CATEGORYID + " INTEGER ," + KEY_CATEGORY + " TEXT,"
                +  KEY_Date + " TEXT,"+ KEY_Amount + " INTEGER,"+KEY_OTHERINFORMATION + " TEXT,"+KEY_CREDITAMOUNT + " INTEGER"+")";


        String CREATE_CATEGORYLIST_TABLE = "CREATE TABLE " + TABLE_CATEGORYLIST + "("
                + KEY_CATEGORYLIST_ID + " TEXT," +KEY_CATEGORYLIST_NAME + " TEXT"+")";


        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + "("
                + KEY_BUDGET + " TEXT" +")";
        db.execSQL(CREATE_BUDGET_TABLE);
        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_CATEGORYLIST_TABLE);

    }

    public void creatExpenseslistTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_CATEGORYID + " INTEGER ," + KEY_CATEGORY + " TEXT,"
                +  KEY_Date + " TEXT,"+ KEY_Amount + " INTEGER,"+KEY_OTHERINFORMATION + " TEXT,"+KEY_CREDITAMOUNT + " INTEGER"+")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }




    public void createCategorylistTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORYLIST);

        String CREATE_CATEGORYLIST_TABLE = "CREATE TABLE " + TABLE_CATEGORYLIST + "("
                + KEY_CATEGORYLIST_ID + " TEXT," +KEY_CATEGORYLIST_NAME + " TEXT"+")";
        db.execSQL(CREATE_CATEGORYLIST_TABLE);
    }


    public void createBudgetTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);

        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + "("
                + KEY_BUDGET + " TEXT" +")";
        db.execSQL(CREATE_BUDGET_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);



        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new expense
    void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORYID, expense.get_id());
        values.put(KEY_CATEGORY, expense.get_category());
        //  values.put(KEY_Title, expense.get_title());
        values.put(KEY_Date, expense.get_date().toString());
        values.put(KEY_Amount, expense.get_amount());
        values.put(KEY_OTHERINFORMATION, expense.get_otherInformation());
        values.put(KEY_CREDITAMOUNT, 0);
            db.insert(TABLE_EXPENSES, null, values);

        // Inserting Row
        db.close(); // Closing database connection
    }

    void updateExpense(int value, String categoryName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE expenses SET amount='"+value+"' WHERE category='"+categoryName+"'");
    }
    public String  checkExpenseExists(String categoryName){
        SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);


        String getCategory = "SELECT * FROM expenses" + " WHERE category= '" + categoryName + "' COLLATE NOCASE" ;
        Cursor cursor = db.rawQuery(getCategory, null);
        if( cursor != null && cursor.moveToFirst() ){

            cursor.close();
            return "Success";
        }
        else {
            return "Failure";

        }
    }
    public int get_amount(String categoryName){
        //  SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES+" WHERE category='"+categoryName+"' COLLATE NOCASE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

int value = 0;
        if (cursor.moveToFirst()) {
            do {

                value = cursor.getInt(3);
            } while (cursor.moveToNext());
        }

        return value;
    }





    void addSyncedExpense(int category_id,String category_name,String expense_date,int debit_amount,String otherinformation,int credit_amount,int i ) {
        SQLiteDatabase db = this.getWritableDatabase();
if(i == 0){
    creatExpenseslistTable(db);
}
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORYID, category_id);
        values.put(KEY_CATEGORY, category_name);
        //  values.put(KEY_Title, expense.get_title());
        values.put(KEY_Date, expense_date);
        values.put(KEY_Amount, debit_amount);
        values.put(KEY_OTHERINFORMATION, otherinformation);
        values.put(KEY_CREDITAMOUNT, credit_amount);
        // Inserting Row
        db.insert(TABLE_EXPENSES, null, values);
        db.close(); // Closing database connection
    }














    // Adding new categorylist
    void addCategoryList(String  category_id,String category_name,int i) {
        SQLiteDatabase db = this.getWritableDatabase();
if(i == 0){
    createCategorylistTable(db);
}


        Log.d("inside table",category_name);
//        db.execSQL("VACUUM " + TABLE_BUDGET);
        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORYLIST_ID, category_id);
        values.put(KEY_CATEGORYLIST_NAME, category_name);

        // Inserting Row
        db.insert(TABLE_CATEGORYLIST, null, values);
        db.close(); // Closing database connection
    }


    public String  checkCategoryExists(String categoryName){
        SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);


        String getCategory = "SELECT * FROM categorylist" + " WHERE categorylist_name = '" + categoryName + "' COLLATE NOCASE" ;
        Cursor cursor = db.rawQuery(getCategory, null);
        if( cursor != null && cursor.moveToFirst() ){

            cursor.close();
            return "Success";
        }
        else {
            return "Failure";

        }
    }







    // Adding new budget
    void addBudget(String  budget) {

        SQLiteDatabase db = this.getWritableDatabase();
        createBudgetTable(db);
//        db.execSQL("VACUUM " + TABLE_BUDGET);
        ContentValues values = new ContentValues();

        values.put(KEY_BUDGET, budget);

        // Inserting Row
        db.insert(TABLE_BUDGET, null, values);
        db.close(); // Closing database connection
    }

    public String getBudget() {
        SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);
        String selectQuery = "SELECT  * FROM " + TABLE_BUDGET;


        String budget="0";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if( cursor != null && cursor.moveToFirst() ){
            budget = cursor.getString(0);
            cursor.close();
        }


        return budget;
    }



    ArrayList<String> categories =new ArrayList<String>() ;
    public ArrayList<String> getCategories() {
        //  SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                String category = cursor.getString(1);

                categories.add(category);
            } while (cursor.moveToNext());
        }

        return categories;
    }


    ArrayList<String> categories2 =new ArrayList<String>() ;

    public ArrayList<String> getCategories_CategoriesList() {
        //  SQLiteDatabase db = this.getReadableDatabase();
        // createBudgetTable(db);
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORYLIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                String category = cursor.getString(1);

                categories2.add(category);
            } while (cursor.moveToNext());
        }

        return categories2;
    }





    // Getting single contact
  /*  Expense getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPENSES, new String[] { KEY_ID,
                        KEY_CATEGORY, KEY_Title,KEY_Date,KEY_Amount }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }
*/



















    // Getting All Expenses
    public List<Expense> getAllExpense() {
        List<Expense> expenseList = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                //  expense.setID(Integer.parseInt(cursor.getString(0)));
                expense.set_id(cursor.getInt(0));
                expense.set_category(cursor.getString(1));

                expense.set_date(cursor.getString(2));
                expense.set_amount(cursor.getInt(3));
                expense.set_otherInformation(cursor.getString(4));
                expense.setCreditAmount(cursor.getInt(5));
                // Adding contact to list
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        // return contact list
        return expenseList;
    }


    // Getting All Expenses
    public List<Expense> getAmount() {
        List amountList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.d("amount",""+cursor.getInt(3));
                amountList.add(cursor.getInt(3));

            } while (cursor.moveToNext());
        }

        // return contact list
        return amountList;
    }




  /*  // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/
  public ArrayList<Cursor> getData(String Query){
      //get writable database
      SQLiteDatabase sqlDB = this.getWritableDatabase();
      String[] columns = new String[] { "mesage" };
      //an array list of cursor to save two cursors one has results from the query
      //other cursor stores error message if any errors are triggered
      ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
      MatrixCursor Cursor2= new MatrixCursor(columns);
      alc.add(null);
      alc.add(null);


      try{
          String maxQuery = Query ;
          //execute the query results will be save in Cursor c
          Cursor c = sqlDB.rawQuery(maxQuery, null);


          //add value to cursor2
          Cursor2.addRow(new Object[] { "Success" });

          alc.set(1,Cursor2);
          if (null != c && c.getCount() > 0) {


              alc.set(0,c);
              c.moveToFirst();

              return alc ;
          }
          return alc;
      } catch(SQLException sqlEx){
          Log.d("printing exception", sqlEx.getMessage());
          //if any exceptions are triggered save the error message to cursor an return the arraylist
          Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
          alc.set(1,Cursor2);
          return alc;
      } catch(Exception ex){

          Log.d("printing exception", ex.getMessage());

          //if any exceptions are triggered save the error message to cursor an return the arraylist
          Cursor2.addRow(new Object[] { ""+ex.getMessage() });
          alc.set(1,Cursor2);
          return alc;
      }


  }

}