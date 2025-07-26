package com.example.memo;

public class Memo {

    private String id;
    private String title;
    private String content;

    // Firebaseがデータ取得時に必要とする空のコンストラクタ
    public Memo() {}

    public Memo(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // idを取得するメソッド
    public String getId() {
        return id;
    }

    // titleを取得するメソッド
    public String getTitle() {
        return title;
    }

    // contentを取得するメソッド
    public String getContent() {
        return content;
    }

    // idを更新するメソッド
    public void setId(String id) {
        this.id = id;
    }

    // titleを更新するメソッド
    public void setTitle(String title) {
        this.title = title;
    }

    // contentを更新するメソッド
    public void setContent(String content) {
        this.content = content;
    }

}
