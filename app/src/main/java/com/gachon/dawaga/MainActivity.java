package com.gachon.dawaga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.rd.PageIndicatorView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(myPagerAdapter);

        PageIndicatorView pageIndicatorView = findViewById(R.id.page_indicator_view);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setSelection(0);

        Button loginBtn = (Button) findViewById(R.id.btn_goToLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
