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

public class Main_fragment4 extends Fragment {
    private String date;
    private String title;
    private String[] dateArr;
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
        Log.d("super","onCreate()");
        if(getArguments() != null) {
            leftSec = getArguments().getInt("leftSec");
            date = getArguments().getString("date");
            title = getArguments().getString("title");
            System.out.println("fragment4_leftSec : " + leftSec);
            System.out.println("fragment4_date : " + date);
            System.out.println("fragment4_title : " + title);

            Log.d("fragment4_titleOnCreate", title + ""); //

            Log.d("fragment4", "Bundle Not null element 11");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("super","onCreateView()");
        View root = inflater.inflate(R.layout.fragment_main_fragment4,container,false);
        linear1 = (LinearLayout) root.findViewById(R.id.fragment4_linear1);
        linear2 = (LinearLayout) root.findViewById(R.id.fragment4_linear2);
        tv_year = (TextView) root.findViewById(R.id.tv_year4);
        tv_month = (TextView) root.findViewById(R.id.tv_month4);
        tv_day = (TextView) root.findViewById(R.id.tv_day4);
        tv_left_year = (TextView) root.findViewById(R.id.tv_leftYear4);
        tv_left_day = (TextView) root.findViewById(R.id.tv_leftDay4);
        tv_left_hour = (TextView) root.findViewById(R.id.tv_leftTime4);
        tv_left_min = (TextView) root.findViewById(R.id.tv_leftMin4);
        tv_left_sec = (TextView) root.findViewById(R.id.tv_leftSec4);
        tv_info_gone = (TextView) root.findViewById(R.id.tv_infoGone4);
        tv_title = (TextView) root.findViewById(R.id.tv_title4);
        return root;
    }

    // #SH onresume에서 실행시 setVisibility(invisible)계속 실행되는 듯 -> 약속이없습니다 화면

    @Override
    public void onStart() {
        super.onStart();
        Log.d("super","onStart()");
        if(getArguments() != null){
            Log.d("fragment4", "Bundle Not null element 22");
            new CountDownTask().execute(); // Starts the CountDownTask
            Log.d("fragment4_titleOnStart", title + ""); //
        }else{
            tv_title.setText(title + "마감시간까지");
        }
    }


     // 남은 시간과 날짜, 약속 타이틀값을 받아온다 (성공)
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(getArguments() != null) {
//            leftSec = getArguments().getInt("leftSec");
//            date = getArguments().getString("date");
//            title = getArguments().getString("title");
//            System.out.println("fragment4_leftSec : " + leftSec);
//            System.out.println("fragment4_date : " + date);
//            System.out.println("fragment4_title : " + title);
//            new CountDownTask().execute(); // Starts the CountDownTask
//        }
//        else{
//            linear1.setVisibility(getView().INVISIBLE);
//            linear2.setVisibility(getView().INVISIBLE);
//            tv_title.setVisibility(getView().INVISIBLE);
//            tv_info_gone.setVisibility(getView().VISIBLE);
//            tv_info_gone.setText("약속이 없습니다.");
//            System.out.println("no arguments");
//        }
//    }

    public void setTitle(String title1){
        title = title1;
    }

    public static Main_fragment4 getInstance(int leftSec, String date, String title) {
        Main_fragment4 main_fragment4 = new Main_fragment4();
        Bundle args = new Bundle();
        args.putInt("leftSec", leftSec);
        args.putString("date", date);
        args.putString("title", title);
        main_fragment4.setArguments(args);
        return main_fragment4;
    }

    private class CountDownTask extends AsyncTask<Void, Integer, Void> {
        int year = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        int saveSec = leftSec;

        @Override
        protected void onPreExecute() {
            Log.d("fragment4_titleThread", title + ""); //
            dateArr = Util.splitDate(date);
            // 년 단위로 나눌 경우
            if (leftSec >= 31536000) {
                year = (int) (leftSec / 31536000);
                leftSec = leftSec % 31536000;   // 남은 초 계산
            }// 하루 단위로 나눌 경우
            if (leftSec >= 86400) {
                day = (int) (leftSec / 86400);
                leftSec = leftSec % 86400;      // 남은 초 계산
            }// 시간 단위로 나눌 경우
            if (leftSec >= 3600) {
                hour = (int) (leftSec / 3600);
                leftSec = leftSec % 3600;       // 남은 초 계산
            }// 분 단위로 나눌 경우
            if (leftSec >= 60) {
                min = (int) (leftSec / 60);
                leftSec = leftSec % 60;         // 남은 초 계산
            }// 남은 초가 1분도 안된다면
            if (leftSec < 60) {
                sec = (int) leftSec;
            }
            // 값 잘 넘어오는지 테스트 (성공 확인)
            System.out.println("fragment4_year : " + year);
            System.out.println("fragment4_day : " + day);
            System.out.println("fragment4_hour : " + hour);
            System.out.println("fragment4_min : " + min);
            System.out.println("fragment4_sec : " + sec);

            System.out.println("fragment4_dateArr[0] : " + dateArr[0]);
            System.out.println("fragment4_dateArr[1] : " + dateArr[1]);
            System.out.println("fragment4_dateArr[2] : " + dateArr[2]);


            // ************** 여기서부터 화면에 제대로 참조가 안되는 문제 ****************
            tv_year.setText(dateArr[0] + "년");
            tv_month.setText(dateArr[1] + "월");
            tv_day.setText(dateArr[2] + "일");
            tv_title.setText(title + "마감시간까지");

            if (year == 0) {
                tv_left_year.setVisibility(getView().INVISIBLE);
            }
            if (day == 0) {
                tv_left_day.setVisibility(getView().INVISIBLE);
            }
            if (hour == 0) {
                tv_left_hour.setVisibility(getView().INVISIBLE);
            }
            if (min == 0) {
                tv_left_min.setVisibility(getView().INVISIBLE);
            }

//            tv_left_year.setText(year + "년");
//            tv_left_day.setText(day + "일");
//            tv_left_hour.setText(hour + "시간");
//            tv_left_min.setText(min + "분");
//            tv_left_sec.setText(sec + "초 남았습니다.");
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doInBackground!!!!!!!!!!!");
            // 1초마다 다음과 같은 연산을 수행하며 남은 시간 감소 시키기
            // #SH loop 안돌길래  year == 0 && sec == 0 --> or 연산으로 임시적으로 대체했습니다.
           while (year == 0 || sec == 0) {
                System.out.println("doInBackground - looping test");
                try {
                    Thread.sleep(1000);
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

                } catch (Exception e) {
                    Log.d("publishprogress test","error");
                }
                publishProgress(year, day, hour, min, sec);
                System.out.println("progress is pushed to onprogressupdate.");
            }
            return null;
        }

        // 1초마다 year, day, hour, min, sec값을 values배열에 받아와서 실시간으로 setText.. setText에 문제가 있음
        @Override
        protected void onProgressUpdate(Integer... values) {
            System.out.println("called fragment4 onProgressUpdate()");
            Log.d("Main_fragment4",values[0]+"년 "+values[1]+"일 "+values[2]+"시간 "+values[3]+"분 "+values[4]+"초 남았습니다.");

            LayoutInflater layoutInFlater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = layoutInFlater.inflate(R.layout.fragment_main_fragment4, null);
            if(rootView != null) {
                tv_left_year = (TextView) rootView.findViewById(R.id.tv_leftYear4);
                tv_left_day = (TextView) rootView.findViewById(R.id.tv_leftDay4);
                tv_left_hour = (TextView) rootView.findViewById(R.id.tv_leftTime4);
                tv_left_min = (TextView) rootView.findViewById(R.id.tv_leftMin4);
                tv_left_sec = (TextView) rootView.findViewById(R.id.tv_leftSec4);
                Log.d("fragment4","rootView != null");
                tv_left_year.setText(values[0] + "년");
                tv_left_day.setText(values[1] + "일");
                tv_left_hour.setText(values[2] + "시간");
                tv_left_min.setText(values[3] + "분");
                tv_left_sec.setText(values[4] + "초 남았습니다.");
            }else{
                Log.d("fragment4","rootView == null");
            }
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("super","onResume()");
        Log.d("fragment4_titleOnResume", title + ""); //
    }
}