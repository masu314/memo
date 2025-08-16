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
    private OnItemClickListener onItemClickListener;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedPositions = new HashSet<>();

    // コンストラクタ
    public MemoListAdapter(ArrayList<Memo> memoList, OnItemClickListener onItemClickListener) {
        this.memoList = memoList;
        this.onItemClickListener = onItemClickListener;
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

        // 選択モードの場合チェックボックスを表示
        if(isSelectionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
        // 選択モードではない場合チェックボックスを非表示
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        // 通常モードでメモリストの項目がクリックされたとき、listenerを通じてMainActivityに通知
        holder.itemView.setOnClickListener(v -> {
            // 通常モードの場合
            if(!isSelectionMode) {
                // MainActivityのonItemClick（openDetail)を呼び出す（自作にインターフェースのため手動で呼び出し）
                onItemClickListener.onItemClick(memo, position);
            }
        });

        // チェックが変更されたとき、selectedPositionsを更新
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPositions.add(position);
            } else {
                selectedPositions.remove(position);
            }
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

    // 選択モードON/OFF用（手動切り替え）
    public void switchCheckboxes(boolean selectionMode) {
        isSelectionMode = selectionMode;
        // 選択カウントをリセット
        selectedPositions.clear();
        // 画面の再描写
        notifyDataSetChanged();
    }

    // 削除後専用（再描画は不要）
    public void resetSelectedPositionsAfterDelete() {
        // 選択カウントをリセット
        selectedPositions.clear();
    }

    // 選択モードで選択されたメモリストを取得
    public ArrayList<Memo> getSelectedIMemos() {
        ArrayList<Memo> selectedMemos = new ArrayList<>();
        for (int i : selectedPositions) {
            selectedMemos.add(memoList.get(i));
        }
        return selectedMemos;
    }
}
