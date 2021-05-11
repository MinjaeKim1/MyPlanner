package com.gachon.dawaga;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();

        String[] names = {"이동훈", "김상욱", "조승환", "박태현"};
        ListView listView = (ListView) findViewById(R.id.userlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
    }

    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(false);

        naverMap.setMapType(NaverMap.MapType.Basic);

        Marker marker = new Marker();
        marker.setIcon(MarkerIcons.BLACK);
        marker.setIconTintColor(Color.BLACK);
        marker.setCaptionText("김상욱");
        marker.setWidth(50);
        marker.setHeight(80);
        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);

        Marker marker2 = new Marker();
        marker2.setIcon(MarkerIcons.BLACK);
        marker2.setIconTintColor(Color.RED);
        marker2.setCaptionText("이동훈");
        marker2.setWidth(50);
        marker2.setHeight(80);
        marker2.setPosition(new LatLng(37.5690135, 126.9783740));
        marker2.setMap(naverMap);

        Marker marker3 = new Marker();
        marker3.setIcon(MarkerIcons.BLACK);
        marker3.setIconTintColor(Color.BLUE);
        marker3.setCaptionText("조승환");
        marker3.setWidth(50);
        marker3.setHeight(80);
        marker3.setPosition(new LatLng(37.5650135, 126.9783740));
        marker3.setMap(naverMap);

        Marker marker4 = new Marker();
        marker4.setIcon(MarkerIcons.BLACK);
        marker4.setIconTintColor(Color.YELLOW);
        marker4.setCaptionText("박태현");
        marker4.setWidth(50);
        marker4.setHeight(80);
        marker4.setPosition(new LatLng(37.5670135, 126.9813740));
        marker4.setMap(naverMap);
    }
}