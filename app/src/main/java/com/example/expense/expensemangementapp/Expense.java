package com.example.expense.expensemangementapp;


public class Expense {

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    //private variables
    int _id;
    String _category;

    String _date;
    float _amount;
    String _budget;
    String _otherInformation;

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    int creditAmount;
    public String get_otherInformation() {
        return _otherInformation;
    }

    public void set_otherInformation(String _otherInformation) {
        this._otherInformation = _otherInformation;
    }


    public String getBudget() {
        return _budget;
    }

    public void setBudget(String _budget) {
        this._budget = _budget;
    }



    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }



    public String get_date() {
        return _date.toString();
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public float get_amount() {
        return _amount;
    }

    public void set_amount(float _amount) {
        this._amount = _amount;
    }



    // Empty constructor
    public Expense(){

    }
    // constructor
    public Expense(int id, String category, String date,int amount,String otherInformation,int creditAmount){
        this.creditAmount = creditAmount;
        this._otherInformation = otherInformation;
        this._id = id;
        this._category = category;

        this._date = date;
        this._amount = amount;
    }

    public Expense(String _budget){
        this._budget = _budget;
    }



    public Expense( String category,String date,float amount,String otherInformation,int creditAmount){
        this.creditAmount = creditAmount;
        this._otherInformation = otherInformation;
        this._category = category;
        this._date = date;
        this._amount = amount;
    }

}