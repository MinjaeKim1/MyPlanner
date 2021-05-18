package com.gachon.dawaga;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Friend_list extends AppCompatActivity implements MyRecyclerAdapter.MyRecyclerViewClickListener {

    ArrayList<ItemData> dataList = new ArrayList<>();
    int[] sample = {R.drawable.ic_launcher_foreground};

    Toolbar toolbar;

    final MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        toolbar = findViewById(R.id.toolbar_friend);
        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dataList.add(new ItemData(sample[0],"김상욱",""));
        dataList.add(new ItemData(sample[0],"이동훈",""));
        dataList.add(new ItemData(sample[0],"조승환",""));
        dataList.add(new ItemData(sample[0],"박태현",""));

        for (int i=0; i<10; i++) {
            dataList.add(new ItemData(sample[0], "친구 "+i, ""));
        }

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);

    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), "Item : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onTitleClicked(int position) {
        Toast.makeText(getApplicationContext(), "Title : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onContentClicked(int position) {
        Toast.makeText(getApplicationContext(), "Content : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageViewClicked(int position) {
        Toast.makeText(getApplicationContext(), "Image : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onItemLongClicked(int position) {
        adapter.remove(position);
        Toast.makeText(getApplicationContext(),
                dataList.get(position).getTitle()+" is removed",Toast.LENGTH_SHORT).show();
    }
}