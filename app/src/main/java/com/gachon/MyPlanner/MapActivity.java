package com.gachon.MyPlanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.gachon.MyPlanner.util.Auth;
import com.gachon.MyPlanner.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;

    ArrayList<Long> latList = new ArrayList<Long>();
    ArrayList<Long> lonList = new ArrayList<Long>();
    ArrayList<String> emailList = new ArrayList<String>();

    private static final String TAG = "MapActivity";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationSource mLocationSource;
    private static NaverMap mNaverMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        toolbar = findViewById(R.id.toolbar_map);
        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색



        // 위치를 반환하는 구현체인 FusedLocationSource 생성
        mLocationSource =
                new FusedLocationSource(this, PERMISSION_REQUEST_CODE);


        Button button = findViewById(R.id.sharelocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double a = mLocationSource.getLastLocation().getLongitude();
                    Double b = mLocationSource.getLastLocation().getLatitude();

                    String q = Double.toString(a);
                    String w = Double.toString(b);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();

                    Map<String, Object> newFieldList = new HashMap<>();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference productRef = db.collection("user");
                    productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String uid2 = (String) document.getId();
                                    if (uid.equals(uid2)) {
                                        Map<String, Object> tempFriendList = document.getData();
                                        for (String key : tempFriendList.keySet()) {
                                            Object value = tempFriendList.get(key);
                                            newFieldList.put(key, value);
                                        }
                                        newFieldList.put("long", a);
                                        newFieldList.put("lat", b);
                                        db.collection("user").document(user.getUid()).set(newFieldList)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "추가완료.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        return;
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button2 = findViewById(R.id.loadFriend);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자의 약속 정보를 가져온다.
                try {
                    emailList.clear();
                    latList.clear();
                    lonList.clear();
                    Firestore.getAllInfo(Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<myAppointment> infoList = new ArrayList<>();
                            if (task.isSuccessful()) {
                                if(task.getResult().size() > 0) {
                                    for (DocumentSnapshot doc : task.getResult()) {
                                        myAppointment info = doc.toObject(myAppointment.class);
                                        infoList.add(info);
                                    }
                                    try {
                                        ArrayList<String> TempList = infoList.get(0).friendsList;
                                        for (String EachUser : TempList) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            String uid = EachUser;
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            CollectionReference productRef = db.collection("user");
                                            productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String uid2 = (String) document.getId();
                                                            if (uid.equals(uid2)) {
                                                                String email = document.getString("userEmail");
                                                                try {
                                                                    Long lat = document.getLong("lat");
                                                                    Long lon = document.getLong("long");
                                                                    latList.add(lat);
                                                                    lonList.add(lon);
                                                                    emailList.add(email);
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }
                                                        Toast.makeText(getApplicationContext(),
                                                                "완료", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(),
                                                "실패", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "약속이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button3 = findViewById(R.id.appearFriend);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Marker> markList = new ArrayList<Marker>();

                if (emailList.size() > 0) {
                    for (int i = 0; i < emailList.size(); i++) {
                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(latList.get(i), lonList.get(i)));
                        markList.add(marker);
                    }
                    for (Marker m : markList) {
                        m.setMap(mNaverMap);
                    }
                }
            }
        });

        // 지도 객체 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);

    }

    private void setMarker(Marker marker,  double lat, double lng)
    {
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //마커의 투명도
        marker.setAlpha(0.8f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 표시
        marker.setMap(mNaverMap);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d( TAG, "onMapReady");

        // 지도상에 마커 표시
        //Marker marker = new Marker();
        //marker.setPosition(new LatLng(37.5670135, 126.9783740));
        //marker.setMap(naverMap);

        // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);


        // 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // request code와 권한획득 여부 확인
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }
}