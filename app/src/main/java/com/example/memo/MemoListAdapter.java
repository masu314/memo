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
import java.util.HashSet;
import java.util.Set;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {

    private ArrayList<Memo> memoList;
    private OnItemClickListener itemListener;
    private OnCheckBoxSelectedListener checkBoxListener;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedPositions = new HashSet<>();

    // コンストラクタ
    public MemoListAdapter(ArrayList<Memo> memoList, OnItemClickListener itemListener) {
        this.memoList = memoList;
        this.itemListener = itemListener;
    }

    //　メモがクリックされたときの処理を定義するためのインターフェース
    public interface OnItemClickListener {
        void onItemClick(Memo memo, int position);
    }

    // チェックボックスがチェックされたときの処理を定義するためのインターフェース
    public interface OnCheckBoxSelectedListener {
        void onCheckBoxSelected(int selectedCount);
    }

    // メインアクティビティでリスナーを登録するためのメソッド
    public void setOnCheckBoxSelectedListener(OnCheckBoxSelectedListener listener) {
        this.checkBoxListener = listener;
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

        // 選択モードの場合チェックボックスを表示
        if(isSelectionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            // 選択状態をリセットする
            holder.checkBox.setChecked(false);
        // 選択モードではない場合チェックボックスを非表示
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        // 通常モードでメモリストの項目がクリックされたときの処理
        holder.itemView.setOnClickListener(v -> {
            // 通常モードの場合
            if(!isSelectionMode) {
                 /*
                  * クリックされたメモの情報とメモの位置をlistenerを通じてMainActivityに通知
                  *
                  * MainActivityのonItemClick（openDetail)を呼び出す
                  * 自作のインターフェースのため手動で呼び出す必要がある
                  */
                itemListener.onItemClick(memo, position);
            }
        });

        // チェックが変更されたときの処理
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 選択されている項目を更新
            if (isChecked) {
                selectedPositions.add(position);
            } else {
                selectedPositions.remove(position);
            }
            // メインアクティビティにチェックされた数を通知
            checkBoxListener.onCheckBoxSelected(selectedPositions.size());
        });
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


    // チェックボックスを表示
    public void enableCheckboxSelection() {
        isSelectionMode = true;
        notifyDataSetChanged();
    }

    // チェックボックスを非表示
    public void disableCheckboxSelection() {
        isSelectionMode = false;
        notifyDataSetChanged();
    }

    // チェックボックスのカウントをリセット
    public void clearCheckedItems() {
        selectedPositions.clear();
    }

    // 選択モードで選択されたメモリストを取得
    public ArrayList<Memo> getSelectedMemoList() {
        ArrayList<Memo> selectedMemoList = new ArrayList<>();
        for (int i : selectedPositions) {
            selectedMemoList.add(memoList.get(i));
        }
        return selectedMemoList;
    }
}
