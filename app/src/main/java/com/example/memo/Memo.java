package com.example.memo;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;

public class Memo {

    private String id;
    private String title;
    private String note;
    public Long updatedAt;

    // Firebaseがデータ取得時に必要とする空のコンストラクタ
    public Memo() {}

    public Memo(String id, String title, String note, Long updatedAt) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.updatedAt = updatedAt;
    }

    // idを取得するメソッド
    public String getId() {
        return id;
    }

    // タイトルを取得するメソッド
    public String getTitle() {
        return title;
    }

    // 本文を取得するメソッド
    public String getNote() {
        return note;
    }

    // 更新日を取得するメソッド
    public Long getUpdatedAt() {
        return updatedAt;
    }

    // 更新日の変換用メソッド
    public Date getUpdatedAtDate() {
        if (updatedAt == null) return null;
        return new Date(updatedAt);
    }

    // idを更新するメソッド
    public void setId(String id) {
        this.id = id;
    }

    // タイトルを更新するメソッド
    public void setTitle(String title) {
        this.title = title;
    }

    // 本文を更新するメソッド
    public void setNote(String note) {
        this.note = note;
    }

    // 更新日を更新するメソッド
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
