package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MemoAddActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText contentEditText;
    Context activityContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleEditText = findViewById(R.id.etTitle);
        contentEditText = findViewById(R.id.etContent);
        Button saveButton = findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new ButtonListener());
        Button deleteButton = findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new ButtonListener());
    }

    //アプリバーにメニューを作成する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //インフレーターを使ってメニューを表示させる
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    //メニューボタンを押したときの反応を定義
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    //ボタンを押した時の反応を定義
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //保存ボタンを押した場合
            if(view.getId() == R.id.btnSave) {
                DatabaseHelper dbHelper = new DatabaseHelper(activityContext);
                dbHelper.addMemo(titleEditText, contentEditText);
            //削除ボタンを押した場合
            }else if(view.getId() == R.id.btnDelete){

            }
            //メイン画面に遷移させる
            finish();
        }
    }
}