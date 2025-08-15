package com.example.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {

    private ArrayList<Memo> memoList;
    private OnItemClickListener listener;

    // コンストラクタ
    public MemoListAdapter(ArrayList<Memo> memoList, OnItemClickListener listener) {
        this.memoList = memoList;
        this.listener = listener;
    }

    /*
     * クリックイベント用インターフェース
     * MainActivityなど外部からクリックを受け取れるようにする
     */
    public interface OnItemClickListener {
        void onItemClick(Memo memo, int position);
    }

    // ViewHolder の生成
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // list_item.xml を inflate してビューに変換
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // 変換したビューを ViewHolder に渡して、ViewHolderを取得
        return new ViewHolder(v);
    }

    // ViewHolder にデータをバインドする
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 表示するデータを取得
        Memo memo = memoList.get(position);

        // タイトルと本文をholderに設定
        holder.title.setText(memo.getTitle());
        holder.note.setText(memo.getNote());

        // 更新日時をDate型に変換
        Date date = memo.getUpdatedAtDate();

        // formatUpdatedAtで条件分岐させ、フォーマットを指定し、String型に格納
        String formattedDate = DateUtils.formatUpdatedAt(date);

        // 更新日時をholderに設定
        if (formattedDate != null) {
            holder.updatedAt.setText(formattedDate);
        } else {
            holder.updatedAt.setText("");
        }

        // メモリストの項目がクリックされたとき、listenerを通じてMainActivityに通知
        holder.itemView.setOnClickListener(v -> listener.onItemClick(memo, position));
    }

    // 表示するアイテム数を取得
    @Override
    public int getItemCount() {
        return memoList.size();
    }

    /*
     * RecyclerView の各行を保持する ViewHolder
     *
     * ViewHolder を使うことで、スクロール時にビューの再利用が効率的になり、
     * findViewById の呼び出し回数を減らしてパフォーマンスが向上する
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, note, updatedAt;
        CheckBox checkBox;

        // コンストラクタ
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // xmlビューの値を変数と紐づける
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            updatedAt = itemView.findViewById(R.id.updatedAt);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
