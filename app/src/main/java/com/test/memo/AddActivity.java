package com.test.memo;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.memo.dao.dao;
import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;
import com.test.memo.service.LongRunningService;
import com.test.memo.widget.CustomDatePicker;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout selectTime;
    private int index=-1;

    private String[] strings;
/*    LongRunningService longRunningService;*/
    private TextView editTitle,edit_body,edit_headText;
    private TextView currentTime;
    private Button saveBtn,backBtn,selectBtn;
    private CustomDatePicker customDatePicker1;
    Context context;
    Bundle bundle;
    Memo memo;
    MemoType memoType;
    Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        bindView();
        initDatePicker();

        memo=(Memo)getIntent().getSerializableExtra("memo");
        if(memo!=null){
            edit_body.setText(memo.getContent());
            editTitle.setText(memo.getTitle());
            edit_headText.setText("修改备忘录");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = dateFormat.format(memo.getData());
            currentTime.setText(date);
            flag=true;

        }
    }
    private void bindView(){
        edit_headText=findViewById(R.id.headText);
        selectTime =  findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);
        currentTime =  findViewById(R.id.currentTime);
        editTitle=findViewById(R.id.edit_title);
        edit_body=findViewById(R.id.edit_body);
        saveBtn=findViewById(R.id.button_save);
        backBtn=findViewById(R.id.button_back);
        selectBtn=findViewById(R.id.select_Type);
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        context=AddActivity.this;
        bundle=new Bundle();
       /* Intent serviceIntent =new Intent(AddActivity.this,LongRunningService.class);
        bindService(serviceIntent,mConnection, Context.BIND_AUTO_CREATE);*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker1.show(currentTime.getText().toString());
                break;
            case R.id.button_save:
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                long time=0;
                Date date=new Date();
                String title="",body="";
                try {
                    date = dateFormat.parse(currentTime.getText().toString());
                    calendar.setTime(date);
                    time = calendar.getTimeInMillis();
                    title = editTitle.getText().toString();
                    body = edit_body.getText().toString();
                }catch  (ParseException e)
                {
                    e.printStackTrace();
                }
                if(flag){
                    if(check(title,body)){
                        memo.setTitle(title);
                        memo.setData(date);
                        memo.setContent(body);
                        memo.update(memo.getId());
                        LongRunningService.cancelAlarm(context,memo.getId(),bundle);
                        LongRunningService.addAlarm(context,memo,bundle,time);
                        Toast.makeText(this, "提醒修改成功", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(check(title,body)){
                        if(memoType==null){
                            memoType=findMemoTypeId("weifenzu");
                        }
                        Log.i("查询到的Memotype",memoType.toString());
                        Memo memo =new Memo();
                        memo.setContent(body);
                        memo.setData(date);
                        memo.setTitle(title);
                        memo.setMemotype_id(memoType.getId());
                        memo.save();
                        memoType.setCount(memoType.getCount()+1);
                        memoType.update(memoType.getId());
                        Log.i("查询到的Memotype",memoType.toString());
                        LongRunningService.addAlarm(context,memo,bundle,time);
                        Toast.makeText(this, "提醒设置成功", Toast.LENGTH_LONG).show();
                    }

                }


                break;
            case R.id.button_back:
                startActivity(new Intent(AddActivity.this,MainActivity.class));
                break;
            case R.id.select_Type:
                List<MemoType> memoTypeList=DataSupport.findAll(MemoType.class);
                List<String> items=new ArrayList<String>();

                for(MemoType memoType:memoTypeList){
                    items.add(memoType.getName());
                }
                strings= new String[memoTypeList.size()];
                items.toArray(strings);

                AlertDialog alertDialog4 = new AlertDialog.Builder(this)
                        .setTitle("选择分组")
                        .setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(strings , 0, new DialogInterface.OnClickListener() {//添加单选框
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                index = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(index==-1){
                                    index=0;
                                }
                                memoType=findMemoTypeId(strings[index]);
                                Toast.makeText(AddActivity.this, "这是确定按钮" + "点的是：" + strings[index], Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(AddActivity.this, "这是取消按钮", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog4.show();

        }
    }
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)+604800);
        String shur_time = sdf.format(calendar.getTime());
        currentTime.setText(now);


        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
            }
        }, now, shur_time); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
    }

    private boolean check(String title,String body){
        boolean flag = true;
        if ("".equals(title)){
            Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (title.length()>10){
            Toast.makeText(this,"标题过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (body.length()>200){
            Toast.makeText(this,"内容过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }
    public MemoType findMemoTypeId(String name){
        List<MemoType> memoTypes=DataSupport.select("id","name","iconId" ,"count")
                .where("name= ?", name)
                .find(MemoType.class);
            for(MemoType memoType:memoTypes){
                Log.i("查询到的Memotype",memoType.toString());
                return memoType;
            }



        return null;
    }
}
