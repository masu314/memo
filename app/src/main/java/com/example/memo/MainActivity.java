package com.example.memo;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
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
    private ListView listView;
    private ListViewAdapter adapter;
    private ArrayList<Memo> memoList;
    private FirebaseHelper firebaseHelper;

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
        // FirebaseHelperをインスタンス化
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // データを読み込み
        loadData();
    }

    // データの読み込み
    private void loadData() {
        // 登録されているメモ一覧を全件取得
        firebaseHelper.getAllMemoList(new FirebaseHelper.MemoListCallback() {
            @Override
            public void onCallback(ArrayList<Memo> memoList) {
                // アダプターにメモ一覧を渡す
                adapter = new ListViewAdapter(MainActivity.this, memoList);
                // ListViewにアダプターを設定する
                listView = (ListView) findViewById(R.id.memoList);
                listView.setAdapter(adapter);

                // リスト項目がクリックされたときの処理
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Memo memo = memoList.get(position);
                        // 詳細画面に遷移させる準備
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        // 遷移先にデータを渡す
                        intent.putExtra("memo_id", memo.getId());
                        intent.putExtra("memo_title", memo.getTitle());
                        intent.putExtra("memo_content", memo.getContent());
                        // 詳細画面に遷移
                        startActivity(intent);
                    }
                });
            }
        });
    }

    // アクションバーにボタンを設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //インフレーターを使ってメニューを表示させる
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // アクションバーのボタンを押したときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // +ボタンが押された場合
        if(id == R.id.add_button){
            // 登録画面に遷移
            Intent intent = new Intent(MainActivity.this, MemoAddActivity.class);
            startActivity(intent);
        }
        // 他のボタンに対してはデフォルトの処理を実行
        return super.onOptionsItemSelected(item);
    }
}