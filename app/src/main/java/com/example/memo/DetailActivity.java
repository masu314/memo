package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private String id;
    private TextView titleView, contentView;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 戻るボタンを表示
        }

        // タイトルと内容のEditTextビューを取得
        titleView = findViewById(R.id.etTitle);
        contentView = findViewById(R.id.etNote);

        // メイン画面から渡されたIntentからメモの情報を取得
        Intent intent = getIntent();
        id = intent.getStringExtra("memo_id");
        String title = intent.getStringExtra("memo_title");
        String content = intent.getStringExtra("memo_note");

        // 既存のメモの情報をビューにバインドする
        titleView.setText(title);
        contentView.setText(content);

        // FirebaseHelperをインスタンス化
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //インフレーターを使ってメニューを表示させる
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    // アクションバーのボタンを押したときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //戻るボタンを押したときの処理
        if (item.getItemId() == android.R.id.home) {
            // 入力した文字列を取得
            String inputTitle = ((EditText)findViewById(R.id.etTitle)).getText().toString();
            String inputNote = ((EditText)findViewById(R.id.etNote)).getText().toString();
            // 既存のデータがあり、タイトルが空ではない場合
            if (id != null && !inputTitle.isEmpty()) {
                //データを更新
                firebaseHelper.updateMemo(id, inputTitle, inputNote);
            // データがない場合
            } else if (id == null){
                Log.e("DetailActivity", "メモIDが無効です");
                Toast.makeText(this, "メモ情報の取得に失敗しました", Toast.LENGTH_SHORT).show();
            }
            finish(); // この画面を閉じて前の画面に戻る
            return true;
        }else if (item.getItemId() == R.id.delete_button) {
            firebaseHelper.deleteMemo(id);
            finish();
            return true;
        }
        // 他のボタンに対してはデフォルトの処理を実行
        return super.onOptionsItemSelected(item);
    }
}