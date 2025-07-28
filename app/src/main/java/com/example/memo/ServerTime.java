package com.example.memo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServerTime {
    private static long offset = 0;  // サーバーと端末の時刻差（ミリ秒）

    public static void startListening() {
        // Firebaseの特別ノード .info/serverTimeOffset を参照
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        // .info/serverTimeOffsetにリスナーをセット
        offsetRef.addValueEventListener(new ValueEventListener() {
            // データが変更された時（最初にも呼ばれる）
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // .info/serverTimeOffset からLong型で値を取得
                Long serverTimeOffset = snapshot.getValue(Long.class);
                if (serverTimeOffset != null) {
                    offset = serverTimeOffset;
                }
            }

            // エラーが発生した時
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ログにエラーメッセージを出力
                Log.e("ServerTime", "取得失敗: " + error.getMessage());
            }
        });
    }

    // 現在のFirebaseサーバー時刻を取得する
    public static long getCurrentServerTimeMillis() {
        return System.currentTimeMillis() + offset;
    }
}
