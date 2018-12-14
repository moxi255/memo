package com.test.memo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.test.memo.db.Memo;
import com.test.memo.service.LongRunningService;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    Button addBtn;
    private MemoAdapter mdapter = null;
    ListView listView;
    Bundle bundle;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        bindView();

    }
    public void bindView(){
        addBtn=findViewById(R.id.add_view);
        addBtn.setOnClickListener(this);
        listView=findViewById(R.id.content);
        List<Memo> memos=DataSupport.findAll(Memo.class);
        for(Memo memo:memos){
            Log.i("123",memo.getContent());

        }
        mdapter = new MemoAdapter(memos, mContext);
        listView.setAdapter(mdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        bundle=new Bundle();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_view:
                startActivity(new Intent(MainActivity.this,AddActivity.class));
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Memo memo=(Memo)mdapter.getItem(position);
        Intent intent=new Intent(this,AddActivity.class);
        intent.putExtra("memo",memo);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("确认删除吗");
        builder.setTitle("提示");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Memo memo=(Memo)mdapter.getItem(position);
                LongRunningService.cancelAlarm(mContext,memo.getId(),bundle);
                DataSupport.deleteAll(Memo.class,"id=?",String.valueOf(memo.getId()));
                mdapter.remove(position);
                arg0.dismiss();
            }
        });
        builder.create().show();
        return true;
    }
}
