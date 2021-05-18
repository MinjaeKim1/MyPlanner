package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gachon.dawaga.util.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_appointment);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
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


        //자바와 xml파일의 버튼을 연결해주는 과정
        findViewById(R.id.btnDate).setOnClickListener(onClickListener);
        findViewById(R.id.btnTime).setOnClickListener(onClickListener);
        findViewById(R.id.btnMakeAppointment).setOnClickListener(onClickListener);

    }

    //버튼들의 action listener를 하나의 switch로 구현
    View.OnClickListener onClickListener = (v -> {
        switch (v.getId()){
            case R.id.btnDate:
                Intent intent = new Intent(makeAppointment.this, datePickerActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnTime:
                Intent intent2 = new Intent(makeAppointment.this, timePickerActivity.class);
                startActivityForResult(intent2, 1);
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
        switch (requestCode){
            case 0:
                int year = data.getIntExtra("Year", 2022);
                int month = data.getIntExtra("Month", 1);
                int day = data.getIntExtra("Day", 1);
                String newDate = year+"-"+month+"-"+ day;
                EditAppoDate.setText(newDate);
                break;
            case 1:
                int hour = data.getIntExtra("Hour", 12);
                int minute = data.getIntExtra("Minute", 0);
                String newTime = hour+":"+minute+":"+"00";
                EditAppoTime.setText(newTime);
                break;
        }
    }

    //작성된 약속정보를 바탕으로 약속정보를 불러온 후 새로운 myAppointment object를 DB에 넣는 함수
    private void Upload(){
        String writer = Auth.getCurrentUser().getUid();
        String title = EditAppoName.getText().toString();
        String date = EditAppoDate.getText().toString();
        String time = EditAppoTime.getText().toString();
        String dateTime = date+" "+time;
        int lateMoney;
        if(checkLateMoney.isChecked()){
            lateMoney = Integer.parseInt(EditLateMoney.getText().toString());
        }
        else{
            lateMoney = 0;
        }
        int meetingMoney;
        if(checkMeetingMoney.isChecked()){
            meetingMoney = Integer.parseInt(EditMeetingMoney.getText().toString());
        }
        else{
            meetingMoney = 0;
        }
        String readyTime = EditReadyHour.getText().toString()+":"+EditReadyMin.getText().toString();
        String marginTime = EditMarginHour.getText().toString()+":"+EditMarginMin.getText().toString();
        boolean alarm = false;
        boolean location = false;
        boolean timeLeft = false;
        if(checkAlarm.isChecked()){
            alarm = true;
        }
        if(checkLoc.isChecked()){
            location = true;
        }
        if(checkTimeLeft.isChecked()){
            timeLeft = true;
        }
        //함수 시작부터 여기까지는 작성된 값을 읽어오는 부분입니다
        //아래 선언문은 새로운 약속 object를 생성하고 sampleDatabase에 집어넣는 부분입니다
        myAppointment newAppointment = new myAppointment(writer, title, dateTime, lateMoney, meetingMoney, readyTime,
                marginTime, alarm, location, timeLeft);
        Uploader(newAppointment);
    }

    private void Uploader(myAppointment newAppointment){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Info").add(newAppointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"약속 생성을 성공하였습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"약속 생성을 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
