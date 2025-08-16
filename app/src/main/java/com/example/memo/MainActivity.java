package com.example.memo;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // ツールバーのメニュー項目クリック処理を実施するため、リスナーインターフェースを登録
        toolbar.setOnMenuItemClickListener(this::onToolbarMenuItemClick);

        // ツールバーのキャンセルを押したときの処理を定義するため、リスナーインターフェースを登録
        cancelTextView.setOnClickListener(this::onToolbarCancelClick);

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

    @Override
    protected void onStart() {
        super.onStart();
        // 現在時刻を監視する非同期処理を開始
        ServerTime.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // メモリストを読み込み
        loadMemoList();
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

    // メモリストの読み込み
    private void loadMemoList() {
        // 登録されているメモ一覧を全件取得（コールバックで取得が終わったら結果を受け取り、表示させる）
        firebaseHelper.getAllMemoList(new FirebaseHelper.MemoListCallback() {
            // メモ一覧を表示
            @Override
            public void onCallback(ArrayList<Memo> memoList) {
                // アダプターを作成し、クリックリスナーを登録（onItemClickを呼ぶが実際にはopenDetailが実行されるようにする）
                adapter = new MemoListAdapter(memoList, MainActivity.this::openDetail);
                // RecyclerViewにアダプターを設定
                recyclerView.setAdapter(adapter);
            }
        });
    }

    // 詳細画面に遷移させる処理（Adapter側でメモリストの項目をクリックした際に呼び出される）
    private void openDetail(Memo memo, int position) {
        // 画面遷移の準備
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("memo_id", memo.getId());
        intent.putExtra("memo_title", memo.getTitle());
        intent.putExtra("memo_note", memo.getNote());
        // 詳細画面に遷移
        startActivity(intent);
    }

    // ツールバーのメニューを押したときの処理
    private boolean onToolbarMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        // 追加を押した場合
        if (id == R.id.add_button) {
            // 新規登録画面に遷移
            startActivity(new Intent(this, CreateActivity.class));
            return true;
        // 選択を押した場合
        } else if (id == R.id.select_button) {
            // メニュー表示を選択モードに変更
            switchToSelectionMenu();
            // チェックボックスを表示に変更
            adapter.switchCheckboxes(true);
            return true;
        // 選択モードの際に表示される削除を押した場合
        } else if (id == R.id.select_mode_delete) {
            for(Memo memo : adapter.getSelectedIMemos()){
                // idに基づきメモを削除
                firebaseHelper.deleteMemo(memo.getId(), new FirebaseHelper.ResultCallback() {
                    // 削除に成功した場合
                    @Override
                    public void onSuccess() {
                        Log.i("MainActivity", "削除成功");
                    }
                    // 削除に失敗した場合
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "削除に失敗しました：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "削除失敗", e);
                    }
                });
            };
            // メニュー表示を通常モードに変更
            switchToNormalMenu();
            // 選択カウントをリセット
            adapter.resetSelectedPositionsAfterDelete();
            // データを再度読み込みなおす
            loadMemoList();
            return true;
        }
        return false;
    }

    // ツールバーのキャンセルボタンを押したときの処理
    private void onToolbarCancelClick(View v){
        // メニュー表示を通常モードに変更
        switchToNormalMenu();
        // チェックボックスを非表示に変更
        adapter.switchCheckboxes(false);
    }

    // メニューの表示を選択モード用にする
    private void switchToSelectionMenu () {
        toolbar.getMenu().clear();
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.main_menu_selection);
        cancelTextView.setVisibility(View.VISIBLE);
    }

    // メニューの表示を通常モード用にする
    private void switchToNormalMenu() {
        toolbar.getMenu().clear();
        toolbar.setTitle("メモ");
        toolbar.inflateMenu(R.menu.main_menu);
        cancelTextView.setVisibility(View.GONE);
    }
}