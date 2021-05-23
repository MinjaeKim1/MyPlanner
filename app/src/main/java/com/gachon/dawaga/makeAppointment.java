package com.gachon.dawaga;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.gachon.dawaga.util.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Distribution;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


//약속을 생성하기 위해 가장 먼저 불러야 할 activity
//메인이나 다른 activity에서 이 activity를 intent로 부르면 사용가능
public class makeAppointment extends AppCompatActivity {
    private EditText EditAppoName;
    private EditText EditAppoDate;
    private EditText EditAppoTime;
    private CheckBox checkLateMoney;
    private EditText EditLateMoney;
    private CheckBox checkMeetingMoney;
    private EditText EditMeetingMoney;
    private EditText EditReadyHour;
    private EditText EditReadyMin;
    private EditText EditMarginHour;
    private EditText EditMarginMin;
    private CheckBox checkAlarm;
    private CheckBox checkLoc;
    private CheckBox checkTimeLeft;
    private LinearLayout alarmSect;

    // Alarm
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    final static String TAG = "makeAppointmentActivity";
    private EditText EditAppoAlarm;

    public int alarm_day;
    public int alarm_hour;
    public int alarm_minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_appointment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("약속 생성");
        }

        //자바와 xml파일의 editText, checkBox등을 연결해주는 과정
        EditAppoName = findViewById(R.id.appoName);
        EditAppoDate = findViewById(R.id.editDate);
        EditAppoTime = findViewById(R.id.editTime);
        checkLateMoney = findViewById(R.id.lateMoney);
        EditLateMoney = findViewById(R.id.editLateMoney);
        checkMeetingMoney = findViewById(R.id.meetingMoney);
        EditMeetingMoney = findViewById(R.id.editMeeingMoney);
        EditReadyHour = findViewById(R.id.readyHour);
        EditReadyMin = findViewById(R.id.readyMin);
        EditMarginHour = findViewById(R.id.marginHour);
        EditMarginMin = findViewById(R.id.marginMin);
        checkAlarm = findViewById(R.id.checkAlarm);
        checkLoc = findViewById(R.id.checkLocation);
        checkTimeLeft = findViewById(R.id.checkTimeLeft);
        alarmSect = findViewById(R.id.alarmSect);

        //Alarm set
        EditAppoAlarm = findViewById(R.id.editAlarm);


        //자바와 xml파일의 버튼을 연결해주는 과정
        findViewById(R.id.btnDate).setOnClickListener(onClickListener);
        findViewById(R.id.btnTime).setOnClickListener(onClickListener);
        findViewById(R.id.btnMakeAppointment).setOnClickListener(onClickListener);
        findViewById(R.id.btnAlarm).setOnClickListener(onClickListener);


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
        Log.d(TAG, "Calendar check: " + mCalender.getTime().toString());

        checkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmSect.setVisibility(View.VISIBLE);
                } else {
                    alarmSect.setVisibility(View.GONE);
                }
            }
        });

    }

    //버튼들의 action listener를 하나의 switch로 구현
    View.OnClickListener onClickListener = (v -> {
        switch (v.getId()) {
            case R.id.btnDate:
                Intent intent = new Intent(makeAppointment.this, datePickerActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnTime:
                Intent intent2 = new Intent(makeAppointment.this, timePickerActivity.class);
                startActivityForResult(intent2, 1);
                break;
            case R.id.btnAlarm:
                Intent intent3 = new Intent(makeAppointment.this, alarmPickerActivity.class);
                startActivityForResult(intent3, 2);
                break;
            case R.id.btnMakeAppointment:
                Upload();
                finish();
                break;
        }
    });

    //날짜 선택과 시간선택 후 activity result를 받는 함수입니다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                int year = data.getIntExtra("Year", 2022);
                int month = data.getIntExtra("Month", 1);
                int day = data.getIntExtra("Day", 1);
                String newDate = year + "-" + month + "-" + day;
                EditAppoDate.setText(newDate);
                break;
            case 1:
                int hour = data.getIntExtra("Hour", 12);
                int minute = data.getIntExtra("Minute", 0);
                String newTime = hour + ":" + minute + ":" + "00";
                EditAppoTime.setText(newTime);
                break;
            case 2:
                alarm_day = data.getIntExtra("Day", 0);
                alarm_hour = data.getIntExtra("Hour", 0);
                alarm_minute = data.getIntExtra("Minute", 0);
                EditAppoAlarm.setText(alarm_day + "D : " + alarm_hour + "H : " + alarm_minute + "M before");
                break;
        }
    }

    //작성된 약속정보를 바탕으로 약속정보를 불러온 후 새로운 myAppointment object를 DB에 넣는 함수
    private void Upload() {
        String writer = Auth.getCurrentUser().getUid();
        String title = EditAppoName.getText().toString();
        String date = EditAppoDate.getText().toString();
        String time = EditAppoTime.getText().toString();
        String dateTime = date + " " + time;
        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateappo = null; //String을 포맷에 맞게 변경
        try {
            dateappo = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long duration = dateNow.getTime() - dateappo.getTime(); // 약속시간,현재시간비교
        if (duration > 0) {
            Toast.makeText(getApplicationContext(), "약속 생성에 실패하였습니다 현재시간 이후로 일정을 잡아주세요", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "일정 시각: " + dateTime);
            int lateMoney;
            if (checkLateMoney.isChecked()) {
                lateMoney = Integer.parseInt(EditLateMoney.getText().toString());
            } else {
                lateMoney = 0;
            }
            int meetingMoney;
            if (checkMeetingMoney.isChecked()) {
                meetingMoney = Integer.parseInt(EditMeetingMoney.getText().toString());
            } else {
                meetingMoney = 0;
            }
            String readyTime = EditReadyHour.getText().toString() + ":" + EditReadyMin.getText().toString();
            String marginTime = EditMarginHour.getText().toString() + ":" + EditMarginMin.getText().toString();
            boolean alarm = false;
            boolean location = false;
            boolean timeLeft = false;
            if (checkAlarm.isChecked()) {
                alarm = true;
            }

            if (checkLoc.isChecked()) {
                location = true;
            }
            if (checkTimeLeft.isChecked()) {
                timeLeft = true;
            }
            //함수 시작부터 여기까지는 작성된 값을 읽어오는 부분입니다
            //아래 선언문은 새로운 약속 object를 생성하고 sampleDatabase에 집어넣는 부분입니다

            myAppointment newAppointment = new myAppointment(writer, title, date, dateTime, lateMoney, meetingMoney, readyTime,
                    marginTime, alarm, location, timeLeft, alarm_day, alarm_hour, alarm_minute);

            Uploader(newAppointment);

            //setAlarm();
            if (alarm = true) {
                setAlarm(newAppointment);
                Log.d(TAG, "알람이 설정되었습니다.");

            }
        }
    }

    private void Uploader(myAppointment newAppointment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Info").add(newAppointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "약속 생성을 성공하였습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "약속 생성을 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAlarm(myAppointment appointment) {
        //Pass values to Alarm Receiver
        Intent receiverIntent = new Intent(makeAppointment.this, AlarmReceiver.class);
        receiverIntent.putExtra("title",appointment.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(makeAppointment.this, 11, receiverIntent, 0);
        String msg = appointment.getDateTime();
        //날짜 포맷을 바꿔주는 소스코드
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(msg);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        calendar.add(Calendar.DATE,alarm_day*-1);
        calendar.add(Calendar.HOUR_OF_DAY,alarm_hour*-1);
        calendar.add(Calendar.MINUTE,alarm_minute*-1);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
        Log.d(TAG,"알람예정 시각: "+ calendar.getTime().toString());
      
    }

}
