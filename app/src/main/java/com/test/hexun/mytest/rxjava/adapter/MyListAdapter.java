package com.test.hexun.mytest.rxjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 何勋 on 2018/8/30.
 */

public class MyListAdapter extends BaseAdapter {
    Context context;
    int layoutId;
    int textId;
    List<String> list;

    public MyListAdapter(Context context, int layoutId, int textId, List<String> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.textId = textId;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(layoutId,null,false);
            viewHolder.text = view.findViewById(textId);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.text.setText(list.get(i));
        return view;
    }

    class ViewHolder {
        TextView text;
    }
}
