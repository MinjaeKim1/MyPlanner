package com.gachon.dawaga;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class Friend_list extends AppCompatActivity implements MyRecyclerAdapter.MyRecyclerViewClickListener {

    ArrayList<ItemData> dataList = new ArrayList<>();
    int[] sample = {R.drawable.ic_launcher_foreground};

    Toolbar toolbar;

    final MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("Friend");
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid2 = (String) document.getId();
                        if (uid.equals(uid2)) {
                            Map<String, Object> tempFriendList = document.getData();
                            for (String key : tempFriendList.keySet()) {
                                String value = document.getString(key);
                                dataList.add(new ItemData(sample[0],value,""));
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar_friend);
        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55e6c3"))); // 툴바 배경색

        Button button = findViewById(R.id.appendFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent (Friend_list.this, AppendFriend.class);
                startActivity(intent_main);
                finish();
            }
        });

        Button button1 = findViewById(R.id.loadFriend);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    recyclerView.setAdapter(adapter);
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