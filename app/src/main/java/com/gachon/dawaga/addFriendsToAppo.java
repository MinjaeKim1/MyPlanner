package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gachon.dawaga.util.model.User;
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

public class addFriendsToAppo extends AppCompatActivity {
    ArrayList<String> friendsToAdd;
    ArrayList<String> friendsList;
    ArrayAdapter<String> friendsToAddAdapter;
    ArrayAdapter<String> friendsListAdapter;
    ListView listView1;
    ListView listView2;

    ArrayList<String> friendsUid;
    String friendsUID;

    EditText friendEmail;
    Button addEmail;
    Button addFriendsToList;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_to_appo);

        setTitle("약속 인원 추가");

        friendEmail = findViewById(R.id.editFriendEmail);
        addEmail = findViewById(R.id.btnAddFriend);
        button = findViewById(R.id.btnAddFriendCom);
        listView1 = findViewById(R.id.friendsToAdd);
        listView2 = findViewById(R.id.friendsList);

        friendsToAdd = new ArrayList<String>();
        friendsList = new ArrayList<String>();
        friendsUid = new ArrayList<String>();
        friendsUID = "";

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
                                friendsList.add(value);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        friendsListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, friendsList);
        addFriendsToList = findViewById(R.id.btnCallFriends);
        addFriendsToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView2.setAdapter(friendsListAdapter);
            }
        });


        friendsToAddAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, friendsToAdd);
        listView1.setAdapter(friendsToAddAdapter);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!friendsToAdd.contains(friendsList.get(position).toString())) {
                    friendsToAdd.add(friendsList.get(position).toString());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference productRef = db.collection("user");
                    productRef
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = user.getUid();
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String uid2 = (String) document.getId();
                                            User isFriend = document.toObject(User.class);
                                            if(friendsToAdd.contains(isFriend.getUserEmail())){
                                                friendsUid.add(uid2);
                                                friendsUID = friendsUID + "/" + uid2;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    friendsToAddAdapter.notifyDataSetChanged();
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friendsToAdd.remove(position);
                friendsUid.remove(position);
                friendsToAddAdapter.notifyDataSetChanged();
            }
        });


        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = friendEmail.getText().toString();
                if (email != null && !friendsToAdd.contains(email)) {
                    friendsToAdd.add(email);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference productRef = db.collection("user");
                    productRef
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = user.getUid();
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String uid2 = (String) document.getId();
                                            User isFriend = document.toObject(User.class);
                                            if(friendsToAdd.contains(isFriend.getUserEmail())){
                                                friendsUid.add(uid2);
                                                friendsUID = friendsUID + "/" + uid2;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "파이어베이스 연동 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    friendsToAddAdapter.notifyDataSetChanged();
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Friends", friendsUID);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        return;
    }
}
