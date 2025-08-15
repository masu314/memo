package com.example.memo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class CreateActivity extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;
    private EditText titleEditView, noteEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // タイトルと内容のEditTextビューを取得
        titleEditView = findViewById(R.id.ipTitle);
        noteEditView = findViewById(R.id.ipNote);

        // FirebaseHelperをインスタンス化
        firebaseHelper = new FirebaseHelper();

        // ツールバーをアクションバーとして設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 戻るボタンを表示
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 現在フォームに表示されている情報を取得
        String inputTitle = titleEditView.getText().toString();
        String inputNote = noteEditView.getText().toString();

        // タイトルが空の場合
        if(inputTitle.isEmpty()){
            Toast.makeText(this, "タイトルが空だと保存できません", Toast.LENGTH_SHORT).show();
        } else {
            // データを保存
            firebaseHelper.insertMemo(inputTitle, inputNote, new FirebaseHelper.ResultCallback() {
                // 保存に成功した場合
                @Override
                public void onSuccess() {
                    Log.i("CreateActivity", "保存成功");
                }
                // 保存に失敗した場合
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(CreateActivity.this, "保存に失敗しました：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CreateActivity", "保存失敗", e);
                }
            });
        }
    }

    // アクションバーのボタンを押したときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 戻るボタンを押したときの処理
        if (item.getItemId() == android.R.id.home) {
            // メイン画面に遷移
            finish();
            return true;
        } else {
            // デフォルトの処理を実行
            return super.onOptionsItemSelected(item);
        }
    }
}