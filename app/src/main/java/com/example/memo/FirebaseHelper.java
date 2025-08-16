package com.example.memo;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private final DatabaseReference databaseRef;

    public FirebaseHelper() {
        // "memos" という名前のノードを取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("memos");
    }

    // 新規メモの作成（pushでID自動生成）
    public void insertMemo(String title, String note, ResultCallback callback) {
        // 新しい一意のキーを生成
        String id = databaseRef.push().getKey();
        // 新規作成内容を取得
        Map<String, Object> inserts = new HashMap<>();
        inserts.put("id", id);
        inserts.put("title", title);
        inserts.put("note", note);
        inserts.put("updatedAt", ServerValue.TIMESTAMP); // サーバー側の現在時刻
        // データを保存
        databaseRef.child(id).setValue(inserts)
                // 保存に成功した場合の処理
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (callback != null) {
                            callback.onSuccess();
                    }
                }
                // 保存に失敗した場合の処理
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    }
                });
    }

    // メモ更新（ID指定で上書き）
    public void updateMemo(String id, String newTitle, String newNote, ResultCallback callback) {
        // 指定のidのノードだけ取得
        DatabaseReference memoRef = databaseRef.child(id);
        // 更新内容を取得
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", newTitle);
        updates.put("note", newNote);
        updates.put("updatedAt", ServerValue.TIMESTAMP); // サーバー側の現在時刻
        // データを更新
        memoRef.updateChildren(updates)
                // 更新に成功した場合の処理
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (callback != null) {
                    callback.onSuccess();
                }
            }
            // 更新に失敗した場合の処理
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    // メモ削除
    public void deleteMemo(String id, ResultCallback callback) {
        databaseRef.child(id).removeValue()
                // 削除に成功した場合
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                // 削除に失敗した場合の処理
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    }
                });;
    }

    // 全件取得（非同期通信のため、コールバックで結果をわたす）
    public void getAllMemoList(MemoListCallback callback) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Firebaseでデータ取得できた場合の処理
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Memo> memoList = new ArrayList<>();
                // snapshot.getChildren() で各メモを取り出し、それぞれMemoクラスに変換し、Listに追加
                for (DataSnapshot child : snapshot.getChildren()) {
                    Memo memo = child.getValue(Memo.class);
                    memoList.add(memo);
                }
                // 更新日順に並び替える
                memoList.sort((m1, m2) -> Long.compare(m2.getUpdatedAt(), m1.getUpdatedAt()));;

                // callbackを呼び出し、取得したメモをMainActivityに渡す
                callback.onCallback(memoList);
            }
            // Firebaseでデータ取得に失敗した場合の処理
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ログにエラーメッセージを出力
                Log.e("FirebaseHelper", "取得失敗: " + error.getMessage());
                // callbackを呼び出し、空のメモをMainActivityに渡す
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    // メモリストを取得する際のコールバック用インターフェースを定義
    public interface MemoListCallback {
        void onCallback(ArrayList<Memo> memoList);
    }

    // 保存ができたことを確認する際のコールバック用インターフェースを定義
    public interface ResultCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
