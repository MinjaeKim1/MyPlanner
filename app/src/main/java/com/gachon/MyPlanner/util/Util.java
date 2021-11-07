package com.gachon.MyPlanner.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    /*
     * Firebase DateTime 정보를 Format에 맞춰 Split
     * @author Taehyun Park
     * @param dateTime
     * @return 시간 정보 String
     */
    public static String splitDateTime(String dateTime, int type) {
        // 공백 기준으로 split
        String Array[] = dateTime.split("\\s");
        return Array[type]; // 0 : Date, 1 : Time
    }

    /*
     * date 정보를 Format에 맞춰 Split
     * @author Taehyun Park
     * @param date
     * @return String[] 배열
     */
    public static String[] splitDate(String date) {
        // 공백 기준으로 split
        String Array[] = date.split("-");
        return Array;
    }

    /*
     * 남은 시간 = 약속 시간 - 현재시간
     * @author Taehyun Park
     * @param  약속 시간 infoDateTime
     * @return 시간 초 값 Int
     */
    public static int calculateTime(String infoDateTime){
        int year = 0; int day = 0; int hour = 0; int min = 0; int sec = 0;
        int saveMillis = 0;
        final long daySeconds = 86400L; // 하루를 초로 환산
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 시간 형식
        Date infoDT = null;

        try{
            infoDT = format.parse(infoDateTime);
        }catch(ParseException e){}

        long infoDTMillis = infoDT.getTime();
        long now = System.currentTimeMillis();
        long passed = ((infoDTMillis/1000L) - (now/1000L)); // 1000으로 나누어서 초 단위로 바꿈
        System.out.println("남은 시간(초) : "+passed);

        saveMillis = (int) passed;
        // 년 단위로 나눌 경우
        if(passed >= 31536000){
            year = (int) (passed / 31536000);
            passed = passed % 31536000;   // 남은 초 계산
        }// 하루 단위로 나눌 경우
        if(passed >= daySeconds){
            day = (int) (passed / daySeconds);
            passed = passed % daySeconds; // 남은 초 계산
        }// 시간 단위로 나눌 경우
        if(passed >= 3600){
            hour = (int) (passed / 3600);
            passed = passed % 3600;       // 남은 초 계산
        }// 분 단위로 나눌 경우
        if(passed >= 60){
            min = (int) (passed / 60);
            passed = passed % 60;       // 남은 초 계산
        }// 남은 초가 1분도 안된다면
        if(passed < 60){
            sec = (int) passed;
        }
        // Run에서 Test 확인용 (원하는 값 잘 나옴.)
        System.out.println(year+"year"+day+"day"+hour+"hour"+min+"min"+sec+"sec left!");
        return (int) saveMillis;
    }
}
