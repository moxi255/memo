package com.test.memo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.memo.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout selectTime;
    private TextView editTitle,edit_body;
    private TextView currentTime;
    private Button savebtn,backbtn;
    private CustomDatePicker customDatePicker1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        bindView();
        initDatePicker();
    }
    private void bindView(){
        selectTime =  findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);
        currentTime =  findViewById(R.id.currentTime);
        editTitle=findViewById(R.id.edit_title);
        edit_body=findViewById(R.id.edit_body);
        savebtn=findViewById(R.id.button_save);
        backbtn=findViewById(R.id.button_back);
        savebtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker1.show(currentTime.getText().toString());
                break;
            case R.id.button_save:
                break;
            case R.id.button_back:
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
}
