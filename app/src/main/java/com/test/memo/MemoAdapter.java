package com.test.memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.memo.db.Memo;


import java.util.List;

public class MemoAdapter extends BaseAdapter {
    private List<Memo> mData;
    private Context mContext;

    public MemoAdapter(List<Memo> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void remove(Memo data) {
        if(mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.date = convertView.findViewById(R.id.time);
            holder.title = convertView.findViewById(R.id.title);
            holder.body = convertView.findViewById(R.id.body);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.date.setText(mData.get(position).getData().toString());
        holder.title.setText(mData.get(position).getTitle());
        holder.body.setText(mData.get(position).getContent());
        return convertView;

    }

    static class ViewHolder {
        TextView date;
        TextView title;
        TextView body;
    }
}
