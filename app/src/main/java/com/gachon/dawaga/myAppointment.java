package com.gachon.dawaga;


import java.util.ArrayList;

//약속에 대한 metadata를 정의하는 object입니다
public class myAppointment {
    String writer;
    String title;
    String date;
    String dateTime;
    int lateMoney;
    int meetingMoney;
    String readyTime;
    String marginTime;
    boolean alarm;
    boolean location;
    boolean timeLeft;
    ArrayList<String> friendsList;
    //alarm
    int a_day;
    int a_hour;
    int a_minute;


    public myAppointment(String writer, String title, String date, String dt, int lateMoney, int meetingMoney, String readyTime, String marginTime, boolean alarm, boolean location, boolean timeLeft, int day, int hour, int minute, ArrayList<String> friendsList){
        this.writer = writer;
        this.title = title;
        this.date = date;
        this.dateTime = dt;
        this.lateMoney = lateMoney;
        this.meetingMoney = meetingMoney;
        this.readyTime = readyTime;
        this.marginTime = marginTime;
        this.alarm = alarm;
        this.location = location;
        this.timeLeft = timeLeft;
        this.a_day = day;
        this.a_hour = hour;
        this.a_minute = minute;
        this.friendsList = friendsList;
    }

    public myAppointment(){}

    public String getWriter(){return this.writer;}
    public String getTitle(){return this.title;}
    public String getDate(){return this.date;}
    public String getDateTime(){return this.dateTime;}
    public int getLateMoney(){return this.lateMoney;}
    public int getMeetingMoney(){return this.meetingMoney;}
    public String getReadyTime(){return this.readyTime;}
    public String getMarginTime(){return this.marginTime;}
    public boolean getAlarm(){return this.alarm;}
    public boolean getLocation(){return this.location;}
    public boolean getTimeLeft(){return this.timeLeft;}
    public ArrayList<String> getFriendsList(){return this.friendsList;}

    public void setWriter(String writer){this.writer = writer;}
    public void setTitle(String title){this.title = title;}
    public void setDate(){this.date = date;}
    public void setDateTime(String dateTime){this.dateTime = dateTime;}
    public void setLateMoney(int lateMoney){this.lateMoney = lateMoney;}
    public void setMeetingMoney(int meetingMoney){this.meetingMoney = meetingMoney;}
    public void setReadyTime(String readyTime){this.readyTime = readyTime;}
    public void setMarginTime(String marginTime){this.marginTime = marginTime;}
    public void setAlarm(boolean alarm){this.alarm = alarm;}
    public void setLocation(boolean location){this.location = location;}
    public void setTimeLeft(boolean timeLeft){this.timeLeft = timeLeft;}
    public void setFriendsList(ArrayList<String> friendsList){this.friendsList = friendsList;}

}
