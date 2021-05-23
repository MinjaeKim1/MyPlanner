package com.gachon.dawaga;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gachon.dawaga.util.Util;

public class Main_fragment1 extends Fragment{
    private String date;
    private String title;
    private String[] dateArr; // 2021-12-25와 같은 문자열에서 2021, 12, 25로 split하여 담기 위한 String배열
    private int leftSec;

    LinearLayout linear1;
    LinearLayout linear2;

    TextView tv_year;
    TextView tv_month;
    TextView tv_day;

    TextView tv_left_year;
    TextView tv_left_day;
    TextView tv_left_hour;
    TextView tv_left_min;
    TextView tv_left_sec;

    TextView tv_info_gone;
    TextView tv_title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_fragment1, container, false);
        linear1 = (LinearLayout) root.findViewById(R.id.fragment1_linear1);
        linear2 = (LinearLayout) root.findViewById(R.id.fragment1_linear2);
        tv_year = (TextView) root.findViewById(R.id.tv_year1);
        tv_month = (TextView) root.findViewById(R.id.tv_month1);
        tv_day = (TextView) root.findViewById(R.id.tv_day1);
        tv_left_year = (TextView) root.findViewById(R.id.tv_leftYear1);
        tv_left_day = (TextView) root.findViewById(R.id.tv_leftDay1);
        tv_left_hour = (TextView) root.findViewById(R.id.tv_leftTime1);
        tv_left_min = (TextView) root.findViewById(R.id.tv_leftMin1);
        tv_left_sec = (TextView) root.findViewById(R.id.tv_leftSec1);
        tv_info_gone = (TextView) root.findViewById(R.id.tv_infoGone1);
        tv_title = (TextView) root.findViewById(R.id.tv_title1);
        return root;
    }

    // 남은 시간과 날짜, 약속 타이틀값을 받아온다 (성공)
    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null){
            leftSec = getArguments().getInt("leftSec");
            date = getArguments().getString("date");
            title = getArguments().getString("title");
            System.out.println("fragment1_leftSec : "+leftSec);
            System.out.println("fragment1_date : "+date);
            System.out.println("fragment1_title : "+title);

            new CountDownTask().execute(); // Starts the CountDownTask
        }else{
            //linear1.setVisibility(getView().INVISIBLE);
            //linear2.setVisibility(getView().INVISIBLE);
            //tv_title.setVisibility(getView().INVISIBLE);
            //tv_info_gone.setVisibility(getView().VISIBLE);
            //tv_info_gone.setText("약속이 없습니다.");
            //tv_title.setText(title + "마감시간까지");

        }
    }

    // 남은 시간과 날짜, 약속 타이틀값을 번들에 담아 보낸다. (성공)
    public static Main_fragment1 getInstance(Integer leftSec, String date, String title) {
        Main_fragment1 main_fragment1 = new Main_fragment1();
        Bundle args = new Bundle();
        args.putInt("leftSec", leftSec.intValue());
        args.putString("date", date);
        args.putString("title", title);
        main_fragment1.setArguments(args);
        return main_fragment1;
    }

    private class CountDownTask extends AsyncTask<Void, Integer, Void>{
        int year = 0; int day = 0; int hour = 0; int min = 0; int sec = 0;
        int saveSec = leftSec;

        @Override
        protected void onPreExecute() {
            dateArr = Util.splitDate(date);
            // 년 단위로 나눌 경우
            if(leftSec >= 31536000){
                year = (int) (leftSec / 31536000);
                leftSec = leftSec % 31536000;   // 남은 초 계산
            }// 하루 단위로 나눌 경우
            if(leftSec >= 86400){
                day = (int) (leftSec / 86400);
                leftSec = leftSec % 86400;      // 남은 초 계산
            }// 시간 단위로 나눌 경우
            if(leftSec >= 3600){
                hour = (int) (leftSec / 3600);
                leftSec = leftSec % 3600;       // 남은 초 계산
            }// 분 단위로 나눌 경우
            if(leftSec >= 60){
                min = (int) (leftSec / 60);
                leftSec = leftSec % 60;         // 남은 초 계산
            }// 남은 초가 1분도 안된다면
            if(leftSec < 60){
                sec = (int) leftSec;
            }
            // 값 잘 넘어오는지 테스트 (성공 확인)
            System.out.println("fragment1_year : "+year);
            System.out.println("fragment1_day : "+day);
            System.out.println("fragment1_hour : "+hour);
            System.out.println("fragment1_min : "+min);
            System.out.println("fragment1_sec : "+sec);

            System.out.println("fragment1_dateArr[0] : "+dateArr[0]);
            System.out.println("fragment1_dateArr[1] : "+dateArr[1]);
            System.out.println("fragment1_dateArr[2] : "+dateArr[2]);

            // ************** 여기서부터 화면에 제대로 참조가 안되는 문제 ****************
            tv_year.setText(dateArr[0]+"년");
            tv_month.setText(dateArr[1]+"월");
            tv_day.setText(dateArr[2]+"일");
            tv_title.setText(title+"마감시간까지");

            if(year == 0){ tv_left_year.setVisibility(getView().INVISIBLE); }
            if(day == 0){ tv_left_day.setVisibility(getView().INVISIBLE); }
            if(hour == 0){ tv_left_hour.setVisibility(getView().INVISIBLE); }
            if(min == 0){ tv_left_min.setVisibility(getView().INVISIBLE); }

            tv_left_year.setText(year+"년");
            tv_left_day.setText(day+"일");
            tv_left_hour.setText(hour+"시간");
            tv_left_min.setText(min+"분");
            tv_left_sec.setText(sec+"초 남았습니다.");
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doInBackground!!!!!!!!!!!");
            // 1초마다 다음과 같은 연산을 수행하며 남은 시간 감소 시키기
            while(year == 0 && sec == 0){
                try { Thread.sleep(1000);
                    if (sec == 0 && min != 0) {
                        min = min - 1;
                        sec = 59;
                    }
                    if (min == 0 && hour != 0) {
                        hour = hour - 1;
                        min = 59;
                    }
                    if (hour == 0 && day != 0) {
                        day = day - 1;
                        hour = 23;
                    }
                    if (day == 0 && year != 0) {
                        year = year - 1;
                        day = 365;
                    }
                    sec = sec - 1;
                    publishProgress(year, day, hour, min, sec);
                }catch (Exception e){}
            }
            return null;
        }

        // 1초마다 year, day, hour, min, sec값을 values배열에 받아와서 setText.. setText에 문제가 있음
        @Override
        protected void onProgressUpdate(Integer... values) {
            System.out.println("called fragment1 onProgressUpdate()");
            Log.d("fragment1","rootView == null");

            tv_left_year.setText(values[0]+"년");
            tv_left_day.setText(values[1]+"일");
            tv_left_hour.setText(values[2]+"시간");
            tv_left_min.setText(values[3]+"분");
            tv_left_sec.setText(values[4]+"초 남았습니다.");
        }

        // 스레드 종료 후 setText와 visible처리, 약속이 종료되었을 때 화면을 어떻게 보여줄 것인지
        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("onPostExecute!!!!!!!!!!!");
            linear1.setVisibility(getView().INVISIBLE);
            linear2.setVisibility(getView().INVISIBLE);
            tv_title.setVisibility(getView().INVISIBLE);
            tv_info_gone.setVisibility(getView().VISIBLE);
        }
    }



}


