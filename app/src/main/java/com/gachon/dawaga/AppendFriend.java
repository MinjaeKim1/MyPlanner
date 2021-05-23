package com.gachon.dawaga;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gachon.dawaga.R;
import com.gachon.dawaga.util.Auth;
import com.gachon.dawaga.util.Firestore;
import com.gachon.dawaga.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AppendFriend extends AppCompatActivity {

    Toolbar toolbar;
    int friendCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_append);

        toolbar = findViewById(R.id.toolbar_friend);
        //상단 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true); // 커스터마이징 하기 위해 필요
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(
                "#55e6c3"))); // 툴바 배경색

        Button button = findViewById(R.id.appendquery);
        EditText emailtext = findViewById(R.id.friendEmail);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtext.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference productRef = db.collection("user");
                productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (email.equals(user.getEmail())) {
                            Toast.makeText(getApplicationContext(),
                                    "자신의 이메일은 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String str = (String) document.getData().get("userEmail");
                                if (email.equals(str)) {
                                    count++;
                                }
                            }
                            if (count > 0) {
                                addFriend(email);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "일치하는 친구가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            //그렇지 않을때
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void addFriend(String email) {
        Map<String, Object> newFriendList = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("Friend");
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
                                String value = document.getString(key);
                                if (email.equals(value)) {
                                    Toast.makeText(getApplicationContext(),
                                            "이미 존재하는 친구입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    newFriendList.put(key,value);
                                }
                            }
                            friendCount = document.getData().size();
                            String strFriendCount = Integer.toString(friendCount);
                            newFriendList.put("email"+strFriendCount, email);
                            db.collection("Friend").document(user.getUid()).set(newFriendList)
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
                    newFriendList.put("email",email);
                    db.collection("Friend").document(user.getUid()).set(newFriendList)
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
                } else {
                    Toast.makeText(getApplicationContext(),
                            "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
