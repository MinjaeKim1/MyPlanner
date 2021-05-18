package com.gachon.dawaga;


import java.sql.Timestamp;

//약속에 대한 metadata를 정의하는 object입니다
public class myAppointment {
    String writer;
    String title;
    Timestamp dateTime;
    int lateMoney;
    int meetingMoney;
    String readyTime;
    String marginTime;
    boolean alarm;
    boolean location;
    boolean timeLeft;

    public myAppointment(String writer, String title, Timestamp dt, int lateMoney, int meetingMoney, String readyTime, String marginTime, boolean alarm, boolean location, boolean timeLeft){
        this.writer = writer;
        this.title = title;
        this.dateTime = dt;
        this.lateMoney = lateMoney;
        this.meetingMoney = meetingMoney;
        this.readyTime = readyTime;
        this.marginTime = marginTime;
        this.alarm = alarm;
        this.location = location;
        this.timeLeft = timeLeft;
    }

    public myAppointment(){}

    public String getWriter(){return this.writer;}
    public String getTitle(){return this.title;}
    public Timestamp getDateTime(){return this.dateTime;}
    public int getLateMoney(){return this.lateMoney;}
    public int getMeetingMoney(){return this.meetingMoney;}
    public String getReadyTime(){return this.readyTime;}
    public String getMarginTime(){return this.marginTime;}
    public boolean getAlarm(){return this.alarm;}
    public boolean getLocation(){return this.location;}
    public boolean getTimeLeft(){return this.timeLeft;}

    public void setWriter(String writer){this.writer = writer;}
    public void setTitle(String title){this.title = title;}
    public void setDateTime(Timestamp dateTime){this.dateTime = dateTime;}
    public void setLateMoney(int lateMoney){this.lateMoney = lateMoney;}
    public void setMeetingMoney(int meetingMoney){this.meetingMoney = meetingMoney;}
    public void setReadyTime(String readyTime){this.readyTime = readyTime;}
    public void setMarginTime(String marginTime){this.marginTime = marginTime;}
    public void setAlarm(boolean alarm){this.alarm = alarm;}
    public void setLocation(boolean location){this.location = location;}
    public void setTimeLeft(boolean timeLeft){this.timeLeft = timeLeft;}

}
