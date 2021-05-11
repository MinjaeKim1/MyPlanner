package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;


//단순히 시간을 받아서 약속시간에 넣어주는 activity입니다
public class timePickerActivity extends AppCompatActivity {
    private int hour=0, min=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.time_picker);

        Calendar calendar = new GregorianCalendar();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        TimePicker timePicker = findViewById(R.id.appoTimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setOnTimeChangedListener(onTimeChangedListener);

        Button button = (Button) findViewById(R.id.appoTimeEnter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("Hour", hour);
                intent.putExtra("Minute", min);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    TimePicker.OnTimeChangedListener onTimeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hh, int mm) {
            hour = hh;
            min = mm;
        }
    };

}
