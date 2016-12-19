package com.ymq.badminton_rank.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.swissrule.Member;

import java.util.ArrayList;

/**
 * Created by chenlixiong on 2016/11/25.
 */

public class GridViewAdapter extends BaseAdapter {
    private final ArrayList<? extends Member> list;

    public GridViewAdapter(ArrayList<? extends Member> list) {
        this.list = list ;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null ){
            convertView = View.inflate(parent.getContext(), R.layout.gridview_item,null);
        }
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
        Member member = list.get(position);
        tv_username.setText(member.username);
        return convertView;
    }
}
