package com.example.memo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<Memo> memoList;
    private LayoutInflater inflater;
    private Context context;
    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;

    // リスナーのインターフェースを定義
    public interface  OnDeleteClickListener {
        void onDeleteClick(Memo memo);
    }
    public interface  OnEditClickListener {
        void onEditClick(Memo memo);
    }

    // リスナーをセット
    public void setOnDeleteClickListener(OnDeleteClickListener listener){
        this.deleteListener = listener;
    }
    public void setOnEditClickListener(OnEditClickListener listener){
        this.editListener = listener;
    }

    // 初期化
    public ListViewAdapter(Context context, ArrayList<Memo> memoList) {
        this.memoList = memoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    // データの件数を取得
    @Override
    public int getCount() {
        return memoList.size();
    }

    // 指定位置のデータを取得
    @Override
    public Memo getItem(int position){
        return memoList.get(position);
    }

    // データのIDを取得
    @Override
    public long getItemId(int position){
        return memoList.get(position).getId();
    }

    // 各行のレイアウトを生成して取得
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Memo memo = memoList.get(position);

        // ビューが再利用可能でない場合（新しくビューを作成する必要がある場合）
        if (convertView == null) {
            //レイアウトのビューをインフレート
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        // テキストビューを取得
        TextView title = convertView.findViewById(R.id.title);
        TextView content = convertView.findViewById(R.id.content);

        // データ取得してビューにバインドする
        title.setText(memo.getTitle());
        content.setText(memo.getContent());

        // ボタンビューを取得
        Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
        Button btnEdit = (Button) convertView.findViewById(R.id.btnEdit);

        // 削除ボタンがクリックされたときの処理を定義
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(deleteListener != null){
                    // Activityに通知
                    deleteListener.onDeleteClick(memo);
                }
            }
        });

        // 編集ボタンがクリックされたときの処理を定義
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(editListener != null){
                    // Activityに通知
                    editListener.onEditClick(memo);
                }
            }
        });

        return convertView;
    }
}
