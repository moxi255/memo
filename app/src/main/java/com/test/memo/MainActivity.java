package com.test.memo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.test.memo.dao.dao;
import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;
import com.test.memo.functions.Function;
import com.test.memo.observable.ObservableManager;
import com.test.memo.service.LongRunningService;
import com.test.memo.util.MemoAdapter;
import com.test.memo.util.MyApplication;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, LeftFragment.OnFragmentInteractionListener {
    private MemoAdapter mdapter = null;
    ListView listView;
    Bundle bundle;
    Button addBtn;
    Context mContext;
    public static final String FUNCTION_WITH_PARAM_AND_RESULT = "FUNCTION_WITH_PARAM_AND_RESULT_FRAGMENT";

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
        mdapter = new MemoAdapter<Memo>(memos, R.layout.list_item) {
            @Override
            public void bindView(ViewHolder holder, Memo obj) {
                holder.setText(R.id.title,obj.getTitle());
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(obj.getData());
                holder.setText(R.id.time,dateStr);
                holder.setText(R.id.body,obj.getContent());

            }
        };
        listView.setAdapter(mdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        bundle=new Bundle();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_view:
               /*MemoType book = new MemoType();
                book.setToDefault("count");
                book.updateAll();*/
                startActivity(new Intent(MainActivity.this,AddActivity.class));
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Memo memo=(Memo)mdapter.getItem(position);
        Intent intent=new Intent(MainActivity.this,AddActivity.class);
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
                MemoType memoType = DataSupport.find(MemoType.class, memo.getMemotype_id());

                System.out.print(memoType.getName());

                memoType.setCount(memoType.getCount()-1);
                memoType.save();
                LongRunningService.cancelAlarm(mContext,memo.getId(),bundle);
                DataSupport.deleteAll(Memo.class,"id=?",String.valueOf(memo.getId()));
                mdapter.remove(position);

                arg0.dismiss();
            }
        });
        builder.create().show();
        return true;
    }
    public void a(){}

    @Override
    public void onFragmentInteraction(Uri uri) {
        mdapter.clear();
        List<Memo> memos=DataSupport.findAll(Memo.class);
        for(Memo memo:memos){
            mdapter.add(memo);
        }

    }

    Listener linstenr;
    public interface Listener {
        void listener(int position);
    }
    public void setLinstenr(Listener linstenr) {
        this.linstenr = linstenr;
    }
}
