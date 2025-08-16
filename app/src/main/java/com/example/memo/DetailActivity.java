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
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private String memoId;
    private EditText titleEditView, noteEditView;
    private String originalTitle,originalNote;
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

        // ツールバーをアクションバーとして設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 戻るボタンを表示
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // タイトルと内容のEditTextビューを取得
        titleEditView = findViewById(R.id.etTitle);
        noteEditView = findViewById(R.id.etNote);

        // メイン画面から渡されたIntentからメモの情報を取得
        Intent intent = getIntent();
        memoId = intent.getStringExtra("memo_id");
        originalTitle = intent.getStringExtra("memo_title");
        originalNote = intent.getStringExtra("memo_note");

        // 既存のメモの情報をビューにバインドする
        titleEditView.setText(originalTitle);
        noteEditView.setText(originalNote);

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

    @Override
    protected void onPause() {
        super.onPause();
        // 現在フォームに表示されている情報を取得
        String currentTitle = titleEditView.getText().toString();
        String currentNote = noteEditView.getText().toString();

        // データがあり、内容が変更されている場合
        if (memoId != null && (!Objects.equals(currentTitle, originalTitle) || !Objects.equals(currentNote, originalNote))) {
            //タイトルと本文が空でない場合
            if (!(currentTitle.isEmpty() && currentNote.isEmpty())) {
                // メモの内容を更新
                firebaseHelper.updateMemo(memoId, currentTitle, currentNote, new FirebaseHelper.ResultCallbackWithId(){
                    // 更新に成功した場合
                    @Override
                    public void onSuccess(String id) {
                        Log.i("DetailActivity", "更新成功 " + id);
                        // 更新内容を反映
                        originalTitle = currentTitle;
                        originalNote = currentNote;
                    }
                    // 更新に失敗した場合
                    @Override
                    public void onFailure(String id, Exception e) {
                        Log.e("DetailActivity", "更新失敗 " + id, e);
                    }
                });
            } else {
                Log.e("DetailActivity", "タイトルもしくは本文がないため保存できません");
            }
        // データがない場合
        } else if (memoId == null){
            Log.e("DetailActivity", "メモIDが無効です");
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
        // ゴミ箱アイコンを押したときの処理
        } else if (item.getItemId() == R.id.delete_button) {
            // メモを削除
            firebaseHelper.deleteMemo(memoId, new FirebaseHelper.ResultCallbackWithId() {
                // 削除に成功した場合
                @Override
                public void onSuccess(String id) {
                    Log.i("DetailActivity", "削除成功 " + id);
                }
                // 削除に失敗した場合
                @Override
                public void onFailure(String id, Exception e) {
                    Log.e("DetailActivity", "削除失敗 " + id, e);
                }
            });
            // メイン画面に遷移
            finish();
            return true;
        } else {
            // デフォルトの処理を実行
            return super.onOptionsItemSelected(item);
        }
    }
}