package com.example.expense.expensemangementapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends BaseAdapter {
    Activity mContext;
    List<Expense> expenses;
public MyListAdapter(Activity mContext,List<Expense> expenses){
    this.expenses = expenses;
    this.mContext = mContext;

}

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDate;
        TextView txtViewCategory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.d("reached1","sdfds");
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  mContext.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.mylistview, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.Title);
            holder.txtViewDate = (TextView) convertView.findViewById(R.id.Date);
            holder.txtViewCategory = (TextView) convertView.findViewById(R.id.Category);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
  /*     for (Expense ex : expenses) {
            Log.d("Name: ", "yeah");
            String log = "Id: "+ex.get_amount()+" ,Name: " + ex.get_date() + " ,Phone: " + ex.get_title();
            holder.txtViewTitle.setText(ex.get_title());
            holder.txtViewDate.setText(ex.get_date());
//            holder.txtViewAmount.setText(ex.get_amount());
            // Writing Contacts to log

        }*/


        holder.txtViewDate.setText(expenses.get(position).get_date());
        holder.txtViewCategory.setText(expenses.get(position).get_category());

        return convertView;
    }
    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public int getViewTypeCount() {
        return 3;
    }
}