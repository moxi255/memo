package com.test.memo.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.memo.R;
import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;

import java.util.ArrayList;

public class MutiLayoutAdapter extends BaseAdapter {
    //定义两个类别标志

    private EditText typeInput;
    private static final int TYPE_MEMO= 0;
    private static final int TYPE_OTHER= 1;
    private Context mContext;
    private ArrayList<Object> mData = null;
    @Override
    public int getCount() {
        return mData.size();
    }
    public MutiLayoutAdapter(Context mContext,ArrayList<Object> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int type = getItemViewType(position);
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        if(convertView == null){
            switch (type){
                case TYPE_MEMO:
                    holder1 = new ViewHolder1();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_type, parent, false);
                    holder1.img_icon =  convertView.findViewById(R.id.img_icon);
                    holder1.txt_name =  convertView.findViewById(R.id.txt_content_type);
                    holder1.txt_count = convertView.findViewById(R.id.memo_count);
                    convertView.setTag(R.id.Tag_Type,holder1);
                    break;
                default:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_other, parent, false);
                    typeInput=convertView.findViewById(R.id.typeEdit);
                    holder2.editText=convertView.findViewById(R.id.typeEdit);
                    holder2.button=convertView.findViewById(R.id.typeSave);
                    convertView.setTag(R.id.Tag_Other,holder2);

                    break;
            }
        }else{
            switch (type){
                case TYPE_MEMO:
                    holder1 = (ViewHolder1) convertView.getTag(R.id.Tag_Type);
                    break;
                default:
                    holder2=(ViewHolder2) convertView.getTag(R.id.Tag_Other);
                    break;
            }
        }

        Object obj = mData.get(position);
        //设置下控件的值
        switch (type){
            case TYPE_MEMO:
                MemoType memoType = (MemoType) obj;
                if(memoType != null){
                    holder1.img_icon.setImageResource(memoType.getIconId());
                    holder1.txt_name.setText(memoType.getName());


                    holder1.txt_count.setText(String.valueOf(memoType.getCount()));
                }
                break;
            default:
                holder2.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(typeInput.getText()!=null){
                            MemoType memoType=new MemoType();
                            if((int)(1+Math.random()*(4-1+1))==1){
                                memoType.setIconId(R.mipmap.one);
                            }else if((int)(1+Math.random()*(4-1+1))==2){
                                memoType.setIconId(R.mipmap.two);
                            }else if((int)(1+Math.random()*(4-1+1))==3){
                                memoType.setIconId(R.mipmap.three);
                            }else{
                                memoType.setIconId(R.mipmap.four);
                            }
                            Log.i("name",typeInput.getText().toString());
                            memoType.setCount(0);
                            memoType.setName(typeInput.getText().toString());
                            memoType.save();
                            remove( getCount()-1);
                            add(memoType);

                        }
                    }
                });
                break;
        }
        return convertView;
    }
    //多布局的核心，通过这个判断类别
    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof MemoType) {
            return TYPE_MEMO;
        } else {
            return TYPE_OTHER;
        }
    }
    public void add(Object data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }
    //类别数目
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    private static class ViewHolder1{
        ImageView img_icon;
        TextView txt_name;
        TextView txt_count;
    }

    private static class ViewHolder2{
        EditText editText;
        Button button;

    }
    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }
    public void remove(MemoType memoType) {
        if (mData != null) {
            mData.remove(memoType);
        }
        notifyDataSetChanged();
    }
    public void remove(int position) {
        if(mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }
}
