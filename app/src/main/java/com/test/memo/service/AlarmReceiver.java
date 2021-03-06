package com.test.memo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.test.memo.MainActivity;
import com.test.memo.R;
import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;


import org.litepal.crud.DataSupport;

public class AlarmReceiver extends BroadcastReceiver {
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {


        id=intent.getIntExtra("id",-1);
        Memo memo=(Memo)intent.getSerializableExtra("memo");

        showNotification(context,memo);


        }
    public  void showNotification(Context context,Memo memo) {
        Notification notification = new NotificationCompat.Builder(context)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle(memo.getTitle())
                /**设置通知的内容**/
                .setContentText(memo.getContent())
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        /**发起通知**/
        notificationManager.notify(0, notification);
        DataSupport.deleteAll(Memo.class,"id=?",String.valueOf(id));
        Log.i("分组id",String.valueOf(memo.getMemotype_id()));
        MemoType memoType=DataSupport.find(MemoType.class,memo.getMemotype_id());
        memoType.setCount(memoType.getCount()-1);
        memoType.save();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);

    }
}
