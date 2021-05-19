package com.gachon.dawaga;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        //declare notificationManager and builder
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        String appointment_title = intent.getStringExtra("title");

        String channelName ="DAWAGA";
        String description = "There is an upcoming appointment soon.";

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        // when above OREO API 26, channel is needed
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); // when using minimap where API>oreo environment ,occurs error!
            int importance = NotificationManager.IMPORTANCE_HIGH; // notification with alarm and sound

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // register notification channel into system
                notificationManager.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.dawaga_logo)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{0,2000,1000,2000})
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("DAWAGA")
                .setContentText("It's time for "+ appointment_title+ ". click to check!")
                .setContentInfo("INFO")
                .setContentIntent(pendingIntent);

        Notification notification= builder.build();
        notificationManager.notify(1,notification);

//        if (notificationManager != null) {
//
//            // Run notificationManager
//            notificationManager.notify(1234, builder.build());
//
//            Calendar nextNotifyTime = Calendar.getInstance();
//
//            // 내일 같은 시간으로 알람시간 결정
//            nextNotifyTime.add(Calendar.DATE, 1);
//
//            //  Preference에 설정한 값 저장
//            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
//            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
//            editor.apply();
//
//            Date currentDateTime = nextNotifyTime.getTime();
//            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
//            Toast.makeText(context.getApplicationContext(),"다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
//        }
    }
}