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

    //nameを取得するメソッド
    public String getTitle() {
        return title;
    }

    //emailを取得するメソッド
    public String getContent() {
        return content;
    }

    //idを更新するメソッド
    public void setId(int id) {
        this.id = id;
    }

    //setTitle(),setContent()
}
