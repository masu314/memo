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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9:00&rewriteBatchedStatements=true","root","password");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM actor;");

            while(rs.next()){
                int actor_id = rs.getInt("actor_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                text1 += actor_id + " " + first_name + " " + last_name + " " + " " + "\r\n";
            }

            rs.close();
            stmt.close();
            con.close();

        }catch(Exception e){
            text1 = e.getMessage();
        }

        return text1;
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        TextView tv = (TextView)activity.findViewById(R.id.textview1);
        tv.setText(result);
    }
}
