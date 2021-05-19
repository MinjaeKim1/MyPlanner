package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class alarmPickerActivity extends AppCompatActivity {
    private int  day=0, hour=0, min=0;
    private NumberPicker picker_day;
    private NumberPicker picker_hour;
    private NumberPicker picker_minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_picker);

        picker_day =(NumberPicker)findViewById(R.id.dayPicker);
        picker_hour =(NumberPicker)findViewById(R.id.hourPicker);
        picker_minute =(NumberPicker)findViewById(R.id.minutePicker);

        picker_day.setMinValue(0);
        picker_day.setMaxValue(30);

        picker_hour.setMinValue(0);
        picker_hour.setMaxValue(24);

        picker_minute.setMinValue(0);
        picker_minute.setMaxValue(60);



        Button button = (Button) findViewById(R.id.appoAlarmEnter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("Day",day);
                intent.putExtra("Hour", hour);
                intent.putExtra("Minute", min);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        picker_day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                day = picker_day.getValue();
                Log.d("picker value", day + "");
            }
        });
        picker_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hour = picker_hour.getValue();
                Log.d("picker_hour value", hour + "");
            }
        });
        picker_minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = picker_minute.getValue();
                Log.d("picker_minute value", min + "");
            }
        });
    }
}
