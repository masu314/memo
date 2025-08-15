package com.example.memo;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private MemoListAdapter adapter;
    private ArrayList<Memo> memoList;
    private FirebaseHelper firebaseHelper;
    private ValueEventListener serverTimeListener;
    private DatabaseReference offsetRef;
    private RecyclerView recyclerView;
    private boolean isSelectionMode = false;
    private Toolbar toolbar;
    private MenuItem addItem, selectItem, deleteItem, cancelItem;
    private TextView cancelTextView;


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
        // ツールバーを設定
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("メモ");
        toolbar.inflateMenu(R.menu.main_menu);
        // ツールバーのキャンセルボタンの取得
        cancelTextView = findViewById(R.id.select_mode_cancel);

        // メニュークリック処理
        toolbar.setOnMenuItemClickListener(this::onToolbarMenuItemClick);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitSelectionMode();
            }
        });

        // FirebaseHelper をインスタンス化
        firebaseHelper = new FirebaseHelper();
        // Firebase のオフセット参照取得
        offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");

        // RecyclerView の取得
        recyclerView = findViewById(R.id.memoRecyclerView);
        // レイアウトマネージャーを設定（縦スクロール）
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // アニメーションを無効化して高さ変動を防ぐ
        recyclerView.setItemAnimator(null);
    }

    // 選択モードに入る
    private void enterSelectionMode() {
        isSelectionMode = true;
        toolbar.getMenu().clear();
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.main_menu_selection);
        cancelTextView.setVisibility(View.VISIBLE);
    }

    // 選択モードを終了
    private void exitSelectionMode() {
        isSelectionMode = false;
        toolbar.getMenu().clear();
        toolbar.setTitle("メモ");
        toolbar.inflateMenu(R.menu.main_menu);
        cancelTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 現在時刻を監視する非同期処理を開始
        ServerTime.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // データを読み込み
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 現在時刻を監視する非同期処理を停止
        if (serverTimeListener != null) {
            // リスナーを削除
            offsetRef.removeEventListener(serverTimeListener);
            // リスナーの参照をしないようにしてメモリを開放する
            serverTimeListener = null;
        }
    }

    // データの読み込み
    private void loadData() {
        // 登録されているメモ一覧を全件取得（非同期）
        firebaseHelper.getAllMemoList(new FirebaseHelper.MemoListCallback() {
            // メモ一覧を表示
            @Override
            public void onCallback(ArrayList<Memo> memoList) {
                // Adapter を作成し、クリックリスナーを設定
                adapter = new MemoListAdapter(memoList, new MemoListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Memo memo, int position) {
                        // 詳細画面に遷移させる準備
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        // 遷移先にデータを渡す
                        intent.putExtra("memo_id", memo.getId());
                        intent.putExtra("memo_title", memo.getTitle());
                        intent.putExtra("memo_note", memo.getNote());
                        // 詳細画面に遷移
                        startActivity(intent);
                    }
                });
                // RecyclerView に Adapter を設定
                recyclerView.setAdapter(adapter);
            }
        });
    }

    // ツールバーのメニューを押したときの処理
    private boolean onToolbarMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_button) {
            startActivity(new Intent(this, CreateActivity.class));
            return true;
        } else if (id == R.id.select_button) {
            enterSelectionMode();
            return true;
        } else if (id == R.id.delete_button) {
            return true;
        }
        return false;
    }
}