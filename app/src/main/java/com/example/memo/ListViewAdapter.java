package com.example.memo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends SimpleAdapter {

    private LayoutInflater inflater;
    private List<? extends Map<String, ?>> listData;
    Context context;

    public ListViewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ビューの生成と再利用
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        //ビューからデータの取得
        TextView title_tmp = convertView.findViewById(R.id.title);
        TextView content_tmp = convertView.findViewById(R.id.content);
        //MainActivityから渡されたデータを取得
        String title = ((HashMap<?, ?>) listData.get(position)).get("title").toString();
        String content = ((HashMap<?, ?>) listData.get(position)).get("content").toString();
        //ビューのデータにMainActivityから渡されたデータを設定
        title_tmp.setText(title);
        content_tmp.setText(content);

        // セル上にあるボタンの処理
        Button btn = (Button) convertView.findViewById(R.id.btnDelete);
        btn.setTag(position);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("MainActivity", "pushButton");
            }
        });

        return convertView;
    }
}
