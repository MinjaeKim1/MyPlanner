package com.gachon.MyPlanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.gachon.MyPlanner.base.BaseFragment;
import com.gachon.MyPlanner.databinding.FragmentBoardBinding;
import com.gachon.MyPlanner.util.Auth;
import com.gachon.MyPlanner.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BoardFragment extends BaseFragment<FragmentBoardBinding> implements BoardAdapter.onItemClickListener {
    private BoardAdapter boardAdapter;
    private DocumentSnapshot last;
    private Boolean isScrolling = false;
    private Boolean isLastItemReached = false;
    private ArrayList<myAppointment> infoList;
    private SwipeRefreshLayout swipeBoard = null;
    private String str;

    @Override
    protected FragmentBoardBinding getBinding() {
        return FragmentBoardBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            str = getArguments().getString("date");
            Log.d("BoardFragment",str);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setAdapter();
        setRefresh();
        return binding.getRoot();
    }

    public static BoardFragment getInstance(String date) {
        BoardFragment boardFragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        boardFragment.setArguments(args);
        return boardFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfoData();
    }

    private void setAdapter() {
        Log.d("BoardFragment", "Set Adapter Run");

        infoList = new ArrayList<>();
        binding.rvBoard.setHasFixedSize(true);
        binding.rvBoard.setLayoutManager(new LinearLayoutManager(getActivity()));
        boardAdapter = new BoardAdapter(getContext(), infoList, this);
        binding.rvBoard.setAdapter(boardAdapter);
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d("BoardFragment", "onScrollStateChanged");
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("BoardFragment", "onScrolled");
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                    isScrolling = false;
                    // TODO : TYPE 설정

                    Query nextQuery = Firestore.getInfoDate(str, Auth.getCurrentUser().getUid()).startAfter(last);
                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> t) {
                            if (t.isSuccessful()) {
                                if(t.getResult().size() > 0){
                                    for(DocumentSnapshot doc : t.getResult()){
                                        myAppointment info = doc.toObject(myAppointment.class);
                                        infoList.add(info);
                                    }
                                    boardAdapter.notifyDataSetChanged();
                                    last = t.getResult().getDocuments().get(t.getResult().size()-1);
                                }

                                if (t.getResult().size() < 10) {
                                    isLastItemReached = true;
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "RecylerView error !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        };
        binding.rvBoard.addOnScrollListener(onScrollListener);
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
        boardAdapter.clear();
        // TODO : TYPE 설정

        Firestore.getInfoDate(str,Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        for(DocumentSnapshot doc : task.getResult()){
                            myAppointment info = doc.toObject(myAppointment.class);
                            infoList.add(info);
                        }
                        boardAdapter.notifyDataSetChanged();
                        last = task.getResult().getDocuments().get(task.getResult().size()-1);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "RecylerView error !!", Toast.LENGTH_SHORT).show();
                }
                binding.swipeBoard.setRefreshing(false);
            }
        });
    }

    // 약속 클릭 시 처리 (아직 필요하진 않음.)
    @Override
    public void onClick(View v, myAppointment infoList) {

    }
}