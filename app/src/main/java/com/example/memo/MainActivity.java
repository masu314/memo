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

    private DatabaseHelper databaseHelper;
    public static Map<String, String> data;
    public static List<Map<String, String>> dataList;
    public static ListView listView;
    public static ListViewAdapter adapter;

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
        loadData();
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
        databaseHelper = new DatabaseHelper(this);
        List<Memo> memoList= databaseHelper.getAllMemoList();

        // アダプターにデータを渡せるように,map型のListを作成
        dataList = new ArrayList<Map<String, String>>();
        for (int i=0; i < memoList.size(); i++) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("title", memoList.get(i).getTitle());
            data.put("content", memoList.get(i).getContent());
            dataList.add(data);
        }

        //アダプターにデータを渡す
        adapter = new ListViewAdapter(
                this,
                dataList,
                R.layout.list_item,
                new String[] {"title", "content"},
                new int[] {R.id.title, R.id.content}
        );

        // ListViewにアダプターを設定する
        listView = (ListView) findViewById(R.id.memoListView);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(false);

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