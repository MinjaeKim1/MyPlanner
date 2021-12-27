package com.gachon.MyPlanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gachon.MyPlanner.base.BaseFragment;
import com.gachon.MyPlanner.databinding.FragmentMainBinding;
import com.gachon.MyPlanner.util.Auth;
import com.gachon.MyPlanner.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.gachon.MyPlanner.util.Util.calculateTime;

public class MainFragment extends BaseFragment<FragmentMainBinding> implements MainAdapter.onItemClickListener {

    private MainAdapter mainAdapter;
    private DocumentSnapshot last;
    private Boolean isScrolling = false;
    private Boolean isLastItemReached = false;
    private SwipeRefreshLayout swipeBoard = null;

    private ArrayList<myAppointment> infoList; // title, date
    private ArrayList<Integer> calLeftTime;

    @Override
    protected FragmentMainBinding getBinding() {
        return FragmentMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setAdapter();
        setRefresh();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfoData();
    }

    private void setAdapter(){
        Log.d("MainFragment", "Set Adapter Run");
        infoList = new ArrayList<>(); // 약속 정보 (제목,날짜)
        calLeftTime = new ArrayList<>(); // 약속 남은 시간

        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainAdapter = new MainAdapter(getContext(), infoList, calLeftTime,this);
        binding.rvMain.setAdapter(mainAdapter);

        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                Log.d("MainFragment", "onScrollStateChanged");
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("MainFragment", "onScrolled");
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                    isScrolling = false;

                    // 사용자의 약속 정보를 가져온다
                    Query nextQuery = Firestore.getAllInfo(Auth.getCurrentUser().getUid()).startAfter(last);
                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().size() > 0){
                                    for(DocumentSnapshot doc : task.getResult()){
                                        myAppointment info = doc.toObject(myAppointment.class);
                                        infoList.add(info);
                                        calLeftTime.add(calculateTime(info.getDateTime()));
                                        Log.d("MainFragment_date",info.getDate());
                                        Log.d("MainFragment_title",info.getTitle());
                                        Log.d("MainFragment_LeftTime",Integer.toString(calculateTime(info.getDateTime())));
                                    }
                                    mainAdapter.notifyDataSetChanged();
                                    last = task.getResult().getDocuments().get(task.getResult().size()-1);
                                    // #SH firestore 잘 읽어오는지 확인
                                    Log.d("MainFragment","CalLeftTime: " + calLeftTime.size());
                                }
                                if (task.getResult().size() < 10) {
                                    isLastItemReached = true;
                                }
                            }else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                Log.d("MainFragment", "Get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        };
        binding.rvMain.addOnScrollListener(onScrollListener);
    }

    private void setRefresh() {
        binding.swipeBoard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfoData();
            }
        });
    }

    private void getInfoData() {
        mainAdapter.clear();
        // TODO : TYPE 설정

        // 사용자의 약속 정보를 가져온다.
        Firestore.getAllInfo(Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        for(DocumentSnapshot doc : task.getResult()){
                            myAppointment info = doc.toObject(myAppointment.class);
                            infoList.add(info);
                            calLeftTime.add(calculateTime(info.getDateTime()));
                            Log.d("MainFragment_date",info.getDate());
                            Log.d("MainFragment_title",info.getTitle());
                            Log.d("MainFragment_LeftTime",Integer.toString(calculateTime(info.getDateTime())));
                        }
                        mainAdapter.notifyDataSetChanged();
                        last = task.getResult().getDocuments().get(task.getResult().size()-1);
                        // #SH firestore 잘 읽어오는지 확인
                        Log.d("MainFragment","CalLeftTime: " + calLeftTime.size());
                    }
                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("MainFragment", "Get failed with ", task.getException());
                }
                binding.swipeBoard.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v, myAppointment infoList) {

    }
}
