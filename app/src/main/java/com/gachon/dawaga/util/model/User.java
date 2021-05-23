package com.gachon.dawaga.util.model;

import com.google.firebase.Timestamp;

public class User {
    private String userEmail; // 유저 이메일
    private String userNickName; // 유저 닉네임
    private Timestamp registerTime; // 가입한 시간

    public User(){}

    public User(String userEmail, String userNickName, Timestamp registerTime){
        this.userEmail = userEmail;
        this.userNickName = userNickName;
        this.registerTime = registerTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }
}
