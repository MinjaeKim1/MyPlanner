package com.gachon.dawaga;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        Button button = findViewById(R.id.appendFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent (Friend_list.this, AppendFriend.class);
                startActivity(intent_main);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        //Toast.makeText(getApplicationContext(), "Item : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onTitleClicked(int position) {
        //Toast.makeText(getApplicationContext(), "Title : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onContentClicked(int position) {
        //Toast.makeText(getApplicationContext(), "Content : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageViewClicked(int position) {
        //Toast.makeText(getApplicationContext(), "Image : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onItemLongClicked(int position) {
        //adapter.remove(position);
        //Toast.makeText(getApplicationContext(),
                //dataList.get(position).getTitle()+" is removed",Toast.LENGTH_SHORT).show();
    }
}