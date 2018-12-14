package com.test.memo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class LongRunningService extends Service {
    private  final  IBinder mBinder=new LocalBinder();
    public class LocalBinder extends Binder {
        public LongRunningService getService(){
            return  LongRunningService.this;
        }
    }

    public LongRunningService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    public static void addAlarm(Context context, int alarmId, Bundle bundle, Long second){
        Log.i("1234","1234");
        Log.i("s",second.toString());
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);

        intent.putExtra("id",alarmId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0);
        //注册新提醒
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,second,pendingIntent);
    }
    public static void cancelAlarm(Context context, int alarmId, Bundle bundle){

        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0);
        //注册新提醒
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
