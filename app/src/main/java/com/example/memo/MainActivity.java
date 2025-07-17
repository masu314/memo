package com.example.memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private ListViewAdapter adapter;
    private ArrayList<Memo> memoList;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // データを再取得して画面を更新
        loadData();
    }


    //データの読み込み
    private void loadData() {
        // データの取得
        dbHelper = new DatabaseHelper(this);
        memoList = dbHelper.getAllMemoList();

        // アダプターにデータを渡す
        adapter = new ListViewAdapter(this, memoList);

        // ListViewにアダプターを設定する
        listView = (ListView) findViewById(R.id.memoListView);
        listView.setAdapter(adapter);

        // 削除ボタンが押されたことがアダプターから通知されたときの処理
        adapter.setOnDeleteClickListener(new ListViewAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Memo memo) {
                // DBから削除
                dbHelper.deleteMemo(memo.getId());
                memoList.clear();
                //最新データ取得
                memoList.addAll(dbHelper.getAllMemoList());
                //画面更新
                adapter.notifyDataSetChanged();
            }
        });

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