package com.example.memo;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

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
    private String originalTitle,originalNote;
    private String memoId = null; // 作成後に取得する Firebase　のid

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

        // 入力した情報を取得
        String inputTitle = titleEditView.getText().toString();
        String inputNote = noteEditView.getText().toString();

        //タイトルと本文が空でない場合
        if (!(inputTitle.isEmpty() && inputNote.isEmpty())) {
            // idが無い場合は新規作成
            if (memoId == null) {
                // データを保存
                firebaseHelper.insertMemo(inputTitle, inputNote, new FirebaseHelper.ResultCallbackWithId() {
                    // 保存に成功した場合
                    @Override
                    public void onSuccess(String id) {
                        Log.i("CreateActivity", "保存成功 " + id);
                        memoId = id;   // 作成された ID を保持
                        originalTitle = inputTitle; // 変更前タイトルとして入力内容を設定
                        originalNote = inputNote; // // 変更前本文として入力内容を設定
                    }

                    // 保存に失敗した場合
                    @Override
                    public void onFailure(String id, Exception e) {
                        Log.e("CreateActivity", "保存失敗 " + id, e);
                    }
                });
            // idがあり、内容が変更されている場合は更新
            } else if (!Objects.equals(inputTitle, originalTitle) || !Objects.equals(inputNote, originalNote)){
                // メモの内容を更新
                firebaseHelper.updateMemo(memoId, inputTitle, inputNote, new FirebaseHelper.ResultCallbackWithId() {
                    // 更新に成功した場合
                    @Override
                    public void onSuccess(String id) {
                        Log.i("CreateActivity", "更新成功 " + id);
                        originalTitle = inputTitle;
                        originalNote = inputNote;
                    }

                    // 更新に失敗した場合
                    @Override
                    public void onFailure(String id, Exception e) {
                        Log.e("CreateActivity", "更新失敗 " + id, e);
                    }
                });
            }
        } else {
            Log.e("CreateActivity", "タイトルと本文がないため保存できません");
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