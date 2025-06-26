package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "memodatabase.db"; // データベース名
    private static final int DATABASE_VERSION = 1; //データベース更新時にバージョンの値を上げていくこと
    private static final String TABLE_NAME_Memo = "memo"; //生成するテーブル名
    private static final String MEMO_COLUMN_ID = "id"; // テーブル内の属性1
    private static final String MEMO_COLUMN_Title = "title";// テーブル内の属性2
    private static final String MEMO_COLUMN_Content = "content";// テーブル内の属性3


    //コンストラクタを定義
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // データベースが初めて作成されたときに呼び出される
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME_Memo + " (" +
                MEMO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEMO_COLUMN_Title + " TEXT, " +
                MEMO_COLUMN_Content + " TEXT)";
        db.execSQL(createTableQuery);
    }


    //データベースのバージョンが変更されたときに呼び出されるテーブルをアップグレードするメソッド
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Memo);
        onCreate(db);
    }

    //データベースからすべてのユーザーを取得するメソッド
    public List<Memo> getAllMemoList() {
        List<Memo> memoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_Memo, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MEMO_COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN_Title));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN_Content));
            memoList.add(new Memo(id, title, content));
        }
        cursor.close();
        db.close();
        return memoList;
    }

    //データを追加するメソッド
    public void addMemo(EditText titleEditText, EditText contentEditText){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        values.put(MEMO_COLUMN_Title, title);
        values.put(MEMO_COLUMN_Content, content);
        db.insert(TABLE_NAME_Memo, null, values);
    }

}
