package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;


//단순히 날짜를 받아서 약속 날짜에 넣어주는 activity입니다
public class datePickerActivity extends AppCompatActivity {
    private int year=0, month=0, day=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.date_picker);

        Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = findViewById(R.id.appoDatePicker);
        datePicker.init(year, month, day, onDateChangedListener);

        Button button = (Button) findViewById(R.id.appoDateEnter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("Year", year);
                intent.putExtra("Month", month+1);
                intent.putExtra("Day", day);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }




    DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int yy, int mm, int dd) {
            year = yy;
            month = mm;
            day = dd;
        }
    };
}
