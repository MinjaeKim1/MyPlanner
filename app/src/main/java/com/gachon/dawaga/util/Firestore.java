package com.gachon.dawaga.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gachon.dawaga.util.model.User;

import java.util.Date;

public class Firestore {

    /*
     * Firestore의 Instance를 반환한다
     * @author Taehyun Park
     * @return FirebaseFirestore Instance
     */
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    /*
     * 새로운 유저의 정보를 DB에 추가하도록 요청한다
     * @author Taehyun Park
     * @param userId Firebase UID
     * @param userEmail 유저 이메일
     * @param userNickName 유저 닉네임
     * @return Task<Void>
     */
    public static Task<Void> writeNewUser(String userId, String userEmail, String userNickName) {
        User newUser = new User(userEmail, userNickName, new Timestamp(new Date()));
        return getFirestoreInstance().collection("user").document(userId).set(newUser);
    }

    /*
     * 유저 정보를 가져온다
     * @author Taehyun Park
     * @param userId Firebase UID
     * @return Task<DocumentSnapshot>
     */
    public static Task<DocumentSnapshot> getUserData(String userId) {
        return getFirestoreInstance().collection("user").document(userId).get();
    }


}
