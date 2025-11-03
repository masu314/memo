```mermaid
sequenceDiagram
    title メモ一覧画面の表示処理
    
    participant User as ユーザー
    participant Main as MainActivity
    participant Firebase as FirebaseHelper
    participant Adapter as MemoListAdapter
    participant Recycler as RecyclerView

    %% --- 画面起動 ---
    User ->> Main: アプリを起動 / メイン画面を開く
    Main ->> Main: onCreate() でUI初期化（Toolbar, RecyclerViewなど）

    %% --- Firebaseからデータ取得 ---
    Main->>Firebase: getAllMemoList(MemoListCallback)
    Note right of Main: メモ一覧の取得処理を開始

    alt データ取得成功
        Firebase-->>Main: callback.onCallback(memoList)

        Note right of Main: メモ一覧を返す
        Main->>Adapter: new MemoListAdapter(memoList, MainActivity.this::openDetail)
        Main->>Recycler: setAdapter(adapter)
        Main->>Adapter: setOnCheckBoxSelectedListener(MainActivity.this::switchMenuVisibility)
        Main -->> User: メモ一覧が表示される

    else データ取得失敗
        Firebase-->>Main: callback.onCallback(emptyList)
        Note right of Main: 空のメモ一覧を返す
        Main->>Adapter: new MemoListAdapter(emptyList, MainActivity.this::openDetail)
        Main->>Recycler: setAdapter(adapter)
        Main->>Adapter: setOnCheckBoxSelectedListener(MainActivity.this::switchMenuVisibility)
        Main -->> User: メモ一覧が表示されない
    end
```

```mermaid
sequenceDiagram
    title メモ詳細画面遷移と処理の流れ

    participant User as ユーザー
    participant Main as MainActivity
    participant Intent as Intent
    participant Detail as DetailActivity
    participant Firebase as FirebaseHelper
    participant DB as Firebase Database

    %% --- 画面遷移 ---
    User->>Main: メモをタップ（openDetail(memo)）
    Main->>Intent: putExtra(memo_id, title, note)
    Main->>Detail: startActivity(intent)
    Note right of Detail: onCreate() 呼び出し<br>レイアウト・ツールバー設定<br>Intentからメモ情報取得
    Detail->>Detail: titleEditView.setText(originalTitle)<br>noteEditView.setText(originalNote)
    Main -->> User: メモ詳細画面が表示される

    %% --- 編集して戻る時（onPause） ---
    User->>Detail: メモの内容を編集
    Detail->>Detail: onPause() 呼び出し
    alt 内容が変更されている AND タイトルまたは本文が空でない
        Detail->>Firebase: updateMemo(memoId, currentTitle, currentNote, callback)
        Firebase->>DB: データ更新
        DB-->>Firebase: 更新結果返却
        Firebase-->>Detail: callback.onSuccess(id)
        Note right of Detail: originalTitle / originalNote 更新
    else 内容が空 または 変更なし
        Detail->>Detail: 更新せず終了
    end

    %% --- 削除処理 ---
    User->>Detail: ゴミ箱アイコンをタップ
    Detail->>Firebase: deleteMemo(memoId, callback)
    Firebase->>DB: データ削除
    DB-->>Firebase: 結果返却
    Firebase-->>Detail: callback.onSuccess(id)
    Detail->>Main: finish()（メイン画面に戻る）
