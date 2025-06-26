package com.example.memo;

import java.util.List;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private MemoAdapter memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // データ取得
        databaseHelper = new DatabaseHelper(this);
        List<Memo> memoList= databaseHelper.getAllMemoList();

        // リストビューにデータ表示
        memoAdapter = new MemoAdapter(this, memoList);
        ListView memoListView = findViewById(R.id.memoListView);
        memoListView.setAdapter(memoAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // データを再取得して画面を更新
        loadData();
    }

    private void loadData() {
        // データの取得
        List<Memo> memoList= databaseHelper.getAllMemoList();
        memoAdapter = new MemoAdapter(this, memoList);
        ListView memoListView = findViewById(R.id.memoListView);
        memoListView.setAdapter(memoAdapter);
    }

    //アプリバーにメニューを作成する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //インフレーターを使ってメニューを表示させる
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    //メニューボタンを押したときの反応を定義する
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_button){
            //登録画面に遷移
            Intent intent = new Intent(MainActivity.this, MemoAddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}