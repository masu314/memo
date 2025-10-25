```mermaid
sequenceDiagram
    title メモ一覧表示処理
    
    participant User as ユーザー
    participant Main as MainActivity
    participant Firebase as FirebaseHelper
    participant DB as Firebase Database
    participant Adapter as MemoListAdapter
    participant Recycler as RecyclerView

    Note right of User: ユーザーがアプリを開く（メモ一覧画面表示）
    Main->>Firebase: getAllMemoList(MemoListCallback)
    Note right of Main: メモ一覧の取得処理を開始

    Firebase->>DB: addListenerForSingleValueEvent()
    Note right of DB: Firebase上のメモデータを非同期で取得

    alt データ取得成功
        DB-->>Firebase: onDataChange(snapshot)
        Firebase->>Firebase: snapshot.getChildren() から Memoリストを生成
        Firebase->>Firebase: 更新日順にソート
        Firebase-->>Main: callback.onCallback(memoList)

        Note over Main: メモリストを受け取りUI更新
        Main->>Adapter: new MemoListAdapter(memoList, MainActivity.this::openDetail)
        Main->>Recycler: setAdapter(adapter)
        Main->>Adapter: setOnCheckBoxSelectedListener(MainActivity.this::switchMenuVisibility)
        Note right of User: メモ一覧が画面に表示される

    else データ取得失敗 または 空データ
        DB-->>Firebase: onCancelled(error) または snapshotが空
        Firebase-->>Main: callback.onCallback(emptyList)
        Note over Main: ② 空のメモリストを受け取りUI更新
        Main->>Adapter: new MemoListAdapter(emptyList, MainActivity.this::openDetail)
        Main->>Recycler: setAdapter(adapter)
        Main->>Adapter: setOnCheckBoxSelectedListener(MainActivity.this::switchMenuVisibility)
        Note right of User: メモ一覧が画面に表示されない
    end
