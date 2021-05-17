package com.gachon.dawaga;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rd.PageIndicatorView;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton makeNewAppo;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        makeNewAppo = (FloatingActionButton) findViewById(R.id.btnMakeNewAppointment);


        toolbar = findViewById(R.id.toolbar);
        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 툴바 메뉴버튼 생성
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // 메뉴 버튼 모양 설정
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색

        // 네비게이션 뷰 아이템 클릭시 이뤄지는 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id){
                    case R.id.item_main:
                        Intent intent_main = new Intent (MainActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        break;

                    case R.id.item_calendar:
                        Intent intent_calendar = new Intent (MainActivity.this, CalendarActivity.class);
                        startActivity(intent_calendar);
                        break;

                    case R.id.item_map:
                        Intent intent_map = new Intent (MainActivity.this, MapActivity.class);
                        startActivity(intent_map);
                        break;

                    case R.id.item_logout:
                        Toast.makeText(getApplicationContext(),"로그아웃 예정입니다.",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        // set adapter

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(myPagerAdapter);

        PageIndicatorView pageIndicatorView = findViewById(R.id.page_indicator_view);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setSelection(0);


        makeNewAppo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makeAppoIntent = new Intent(getApplicationContext(), makeAppointment.class);
                startActivity(makeAppoIntent);
            }
        });

        /*
        Button loginBtn = (Button) findViewById(R.id.btn_goToLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        */
    }

}
