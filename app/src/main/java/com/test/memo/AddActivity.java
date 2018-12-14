package com.test.memo;

import android.content.ComponentName;
import android.content.Context;
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

import com.test.memo.db.Memo;
import com.test.memo.service.LongRunningService;
import com.test.memo.widget.CustomDatePicker;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout selectTime;
    LongRunningService longRunningService;
    private TextView editTitle,edit_body,edit_headText;
    private TextView currentTime;
    private Button saveBtn,backBtn;
    private CustomDatePicker customDatePicker1;
    Context context;
    Bundle bundle;
    Memo memo;
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
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        context=AddActivity.this;
        bundle=new Bundle();
        Intent serviceIntent =new Intent(AddActivity.this,LongRunningService.class);
        bindService(serviceIntent,mConnection, Context.BIND_AUTO_CREATE);
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
                        longRunningService.cancelAlarm(context,memo.getId(),bundle);
                        longRunningService.addAlarm(context,memo.getId(),bundle,time);
                        Toast.makeText(this, "提醒修改成功", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(check(title,body)){

                        Memo memo =new Memo();
                        memo.setContent(body);
                        memo.setData(date);
                        memo.setTitle(title);
                        memo.save();
                        Log.i("aaa",String.valueOf(memo.getId()));
                        longRunningService.addAlarm(context,memo.getId(),bundle,time);
                        Toast.makeText(this, "提醒设置成功", Toast.LENGTH_LONG).show();
                    }

                }


                break;
            case R.id.button_back:
                startActivity(new Intent(AddActivity.this,MainActivity.class));
                break;
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
    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            longRunningService=((LongRunningService.LocalBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            longRunningService=null;
        }
    };
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

}
