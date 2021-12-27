package com.gachon.MyPlanner;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.gachon.MyPlanner.base.BaseActivity;
import com.gachon.MyPlanner.databinding.ActivityMainBinding;
import com.gachon.MyPlanner.util.Auth;
import com.gachon.MyPlanner.util.Firestore;
import com.gachon.MyPlanner.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity<ActivityMainBinding> {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton makeNewAppo;
    TextView tv_name;
    private static final String TAG = "MainActivity";

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
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        makeNewAppo = (FloatingActionButton) findViewById(R.id.btnMakeNewAppointment);
        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        toolbar = findViewById(R.id.toolbar);

        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 툴바 메뉴버튼 생성
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // 메뉴 버튼 모양 설정
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색

        // 로그인 후 프로필 표시
        setProfile();

        // mainFragment 연결
        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(binding.flInfo.getId(), mainFragment).commit();

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
                        FirebaseAuth.getInstance().signOut();
                        Log.d(TAG,"sign-out success");
                        Intent intent_signOut = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent_signOut);
                        Toast.makeText(getApplicationContext(),"Logged out.",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
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


    // #SH notifychanged 프래그먼트 변화 감지시 발생
    @Override
    protected void onResume(){
        super.onResume();
    }
}