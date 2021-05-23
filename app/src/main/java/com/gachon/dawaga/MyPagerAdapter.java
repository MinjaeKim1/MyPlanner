package com.gachon.dawaga;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gachon.dawaga.util.Auth;
import com.gachon.dawaga.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.gachon.dawaga.util.Util.calculateTime;

public class MyPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs; //tab의 갯수

    public ArrayList<String> date;
    public ArrayList<String> title;
    public ArrayList<Integer> calLeftTime;

    public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

        date = new ArrayList<>();
        title = new ArrayList<>();
        calLeftTime = new ArrayList<>();

        Firestore.getInfoFour(Auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            myAppointment info = doc.toObject(myAppointment.class);
                            date.add(info.getDate());
                            title.add(info.getTitle());
                            calLeftTime.add(calculateTime(info.getDateTime()));
                            Log.d("MainActivity_date", info.getDate());
                            Log.d("MainActivity_title", info.getTitle());
                            Log.d("MainActivity_LeftTime", Integer.toString(calculateTime(info.getDateTime())));
                        }
                        // #SH firestore 잘 읽어오는지 확인
                        Log.d("MyPagerAdapter","CalLeftTime: " + calLeftTime.size());
                    } else { }
                }
            }
        });
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Main_fragment1 tab1 = new Main_fragment1();
                // 여기서 데이터를 전달해야할까?
                return tab1;
            case 1:
                Main_fragment2 tab2 = new Main_fragment2();
                return tab2;
            case 2:
                Main_fragment3 tab3 = new Main_fragment3();
                return tab3;
            case 3:
                Main_fragment4 tab4 = new Main_fragment4();

                return tab4;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    // #SH
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
