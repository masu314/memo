package com.example.memo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        dbHelper = new DatabaseHelper(this);

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
}