package com.example.memo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ボタンにリスナーを設定
        Button saveButton = findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new ButtonListener());
        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new ButtonListener());

        // FirebaseHelperをインスタンス化
        firebaseHelper = new FirebaseHelper();
    }

    // メニューボタンを押したときの反応を定義
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    // ボタンを押した時の処理
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            // 保存ボタンを押した場合
            if(view.getId() == R.id.btnSave) {
                // 入力した文字列を取得
                String inputTitle = ((EditText)findViewById(R.id.addTitle)).getText().toString();
                String inputContent = ((EditText)findViewById(R.id.addContent)).getText().toString();
                // データを保存
                firebaseHelper.addMemo(inputTitle,inputContent);
            }
            // メイン画面に遷移させる
            finish();
        }
    }
}