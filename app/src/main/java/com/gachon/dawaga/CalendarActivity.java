package com.gachon.dawaga;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.gachon.dawaga.util.Auth;
import com.gachon.dawaga.base.BaseActivity;
import com.gachon.dawaga.databinding.ActivityCanlendarBinding;
import com.gachon.dawaga.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import android.graphics.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;

import android.os.AsyncTask;

public class CalendarActivity extends BaseActivity<ActivityCanlendarBinding> {
    ArrayList<String> infoDateList;
    MaterialCalendarView materialCalendarView;
    @Override
    protected ActivityCanlendarBinding getBinding() {
        return ActivityCanlendarBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canlendar);
        infoDateList = new ArrayList<>();
        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());

        getAllData();
        Log.d("CalendarActivity_Data",infoDateList.toString());
        new ApiSimulator(infoDateList).executeOnExecutor(Executors.newSingleThreadExecutor());

        // 날짜를 선택한다면
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String str_date = Integer.toString(date.getYear()) + "-" + Integer.toString(date.getMonth()+1) + "-" + Integer.toString(date.getDay());
                BoardFragment boardFragment = BoardFragment.getInstance(str_date);
                Log.d("날짜 선택",str_date);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(binding.flBoard.getId(), boardFragment).commit();
            }
        });
    }

    // 유저의 모든 약속 날짜 (ex.2021-5-19) String 타입으로 ArrayList에 저장
    private void getAllData(){
        Firestore.getAllInfo(Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        for(DocumentSnapshot doc : task.getResult()){
                            myAppointment info = doc.toObject(myAppointment.class);
                            infoDateList.add(info.date);
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;

        ApiSimulator(ArrayList<String> Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.size() ; i ++){
                String[] time = Time_Result.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                Log.d("CalendarActivity_year",time[0]);
                Log.d("CalendarActivity_month",time[1]);
                Log.d("CalendarActivity_dayy",time[2]);
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,CalendarActivity.this));
        }
    }


}
