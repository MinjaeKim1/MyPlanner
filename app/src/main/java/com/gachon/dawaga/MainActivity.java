package com.gachon.dawaga;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.gachon.dawaga.util.Auth;
import com.gachon.dawaga.util.Firestore;
import com.gachon.dawaga.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rd.PageIndicatorView;
import java.util.ArrayList;

import static com.gachon.dawaga.util.Util.calculateTime;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton makeNewAppo;
    TextView tv_name;

    public ArrayList<String> date;
    public ArrayList<String> title;
    public ArrayList<Integer> calLeftTime;


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
        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        date = new ArrayList<>(); // 약속 date
        title = new ArrayList<>(); // 약속 title
        calLeftTime = new ArrayList<>(); // 약속 남은 시간
        toolbar = findViewById(R.id.toolbar);

        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 툴바 메뉴버튼 생성
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // 메뉴 버튼 모양 설정
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색

        // 로그인 후 프로필 표시
        setProfile();

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
                        finish();
                        break;

                    case R.id.item_calendar:
                        Intent intent_calendar = new Intent (MainActivity.this, CalendarActivity.class);
                        startActivity(intent_calendar);
                        break;

                    case R.id.item_map:
                        Intent intent_map = new Intent (MainActivity.this, MapActivity.class);
                        startActivity(intent_map);
                        break;

                    case R.id.item_friend:
                        Intent intent_friend = new Intent (MainActivity.this, Friend_list.class);
                        startActivity(intent_friend);
                        break;

                    case R.id.item_logout:
                        Toast.makeText(getApplicationContext(),"로그아웃 예정입니다.",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }

        });

        // Set adapter (뷰페이저)
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(myPagerAdapter);
        // Set PageIndicator
        PageIndicatorView pageIndicatorView = findViewById(R.id.page_indicator_view);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setSelection(0);

        // 약속 시간이 많이 남은 순서대로 정렬하여 약속 정보를 최대 4개까지 가져온다.
        Firestore.getInfoFour(Auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        for(DocumentSnapshot doc : task.getResult()){
                            myAppointment info = doc.toObject(myAppointment.class);
                            date.add(info.getDate());
                            title.add(info.getTitle());
                            calLeftTime.add(calculateTime(info.getDateTime()));
                            Log.d("MainActivity_date",info.getDate());
                            Log.d("MainActivity_title",info.getTitle());
                            Log.d("MainActivity_calLeftTime",Integer.toString(calculateTime(info.getDateTime())));
                        }
                    }
                    // 약속이 1개밖에 없다면 fragment1만 활성화시켜야함, 남은 시간, 약속 날짜(년/월/일), 약속 제목을 넘겨준다.
                    if(calLeftTime.size() == 1){
                        Main_fragment1 mainFragment1 = Main_fragment1.getInstance(calLeftTime.get(0), date.get(0), title.get(0));
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.viewPager, mainFragment1).commit();
                    }
                    // 약속이 2개일 경우에는 fragment1, fragment2 두 곳을 활성화
                    else if(calLeftTime.size() == 2){
                        Main_fragment1 mainFragment1 = Main_fragment1.getInstance(calLeftTime.get(0), date.get(0),title.get(0));
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.viewPager, mainFragment1).commit();

                        Main_fragment2 mainFragment2 = Main_fragment2.getInstance(calLeftTime.get(1), date.get(1),title.get(1));
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.viewPager, mainFragment2).commit();
                    }
                    // 약속이 3개일 경우에는 fragment1, fragment2, fragment3 세 곳을 활성화
                    else if(calLeftTime.size() == 3){
                        Main_fragment1 mainFragment1 = Main_fragment1.getInstance(calLeftTime.get(0), date.get(0), title.get(0));
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.viewPager, mainFragment1).commit();

                        Main_fragment2 mainFragment2 = Main_fragment2.getInstance(calLeftTime.get(1), date.get(1), title.get(1));
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.viewPager, mainFragment2).commit();

                        Main_fragment3 mainFragment3 = Main_fragment3.getInstance(calLeftTime.get(2), date.get(2), title.get(2));
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        fragmentManager3.beginTransaction().replace(R.id.viewPager, mainFragment3).commit();
                    }else{ // 약속이 4개일 경우
                        Main_fragment1 mainFragment1 = Main_fragment1.getInstance(calLeftTime.get(0), date.get(0), title.get(0));
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.viewPager, mainFragment1).commit();

                        Main_fragment2 mainFragment2 = Main_fragment2.getInstance(calLeftTime.get(1), date.get(1), title.get(1));
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.viewPager, mainFragment2).commit();

                        Main_fragment3 mainFragment3 = Main_fragment3.getInstance(calLeftTime.get(2), date.get(2), title.get(2));
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        fragmentManager3.beginTransaction().replace(R.id.viewPager, mainFragment3).commit();

                        Main_fragment4 mainFragment4 = Main_fragment4.getInstance(calLeftTime.get(3), date.get(3), title.get(3));
                        FragmentManager fragmentManager4 = getSupportFragmentManager();
                        fragmentManager4.beginTransaction().replace(R.id.viewPager, mainFragment4).commit();
                    }
                    date.clear();
                    title.clear();
                    calLeftTime.clear();
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "get failed with ", task.getException());
                }
            }
        });

        makeNewAppo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makeAppoIntent = new Intent(getApplicationContext(), makeAppointment.class);
                startActivity(makeAppoIntent);
            }
        });
    }

    private void setProfile(){
        Firestore.getUserData(Auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                if(user != null){
                    tv_name.setText(user.getUserNickName() + "님");
                }else {
                    // failed.
                    Log.d("MainActivity.this", "user object is NULL.");
                }
            }
        });
    }
}
