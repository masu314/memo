package com.example.memo;

public class Memo {

    private int id;
    private String title;
    private String content;

    public Memo(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    //idを取得するメソッド
    public int getId() {
        return id;
    }

    //titleを取得するメソッド
    public String getTitle() {
        return title;
    }

    //contentを取得するメソッド
    public String getContent() {
        return content;
    }

    //idを更新するメソッド
    public void setId(int id) {
        this.id = id;
    }

    //titleを更新するメソッド
    public void setTitle(String title) {
        this.title = title;
    }

    //contentを更新するメソッド
    public void setContent(String content) {
        this.content = content;
    }

}
