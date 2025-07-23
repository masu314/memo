package com.example.memo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int memoId;
    private TextView titleView, contentView;

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

        // UIコンポーネントの取得
        titleView = findViewById(R.id.etTitle);
        contentView = findViewById(R.id.etContent);

        // Intentからメモの情報を取得
        Intent intent = getIntent();
        memoId = intent.getIntExtra("memo_id", -1);
        String title = intent.getStringExtra("memo_title");
        String content = intent.getStringExtra("memo_content");

        // メモの情報を表示
        titleView.setText(title);
        contentView.setText(content);
    }

    // 戻るボタンを押したときの反応を定義
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // この画面を閉じて前の画面に戻る
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}