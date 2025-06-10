package com.example.memo;

import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TaskDBConnect extends AsyncTask<Void, Void, String> {

    MainActivity activity;

    public TaskDBConnect(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {
        String text1="";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?autoReconnect=true&useSSL=false","root","password");
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("Select * from actor");

            while(rs.next()){
                int actor_id = rs.getInt(1);
                String first_name = rs.getString(2);
                String last_name = rs.getString(3);
                String last_update = rs.getString(4);
                text1 += actor_id + " " + first_name + " " + last_name + " " + last_update + " " + "\r\n";
            }

        }catch(Exception e){
            text1=e.getMessage();
        }

        return text1;
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        TextView tv = (TextView)activity.findViewById(R.id.textview1);
        tv.setText(result);
    }
}
