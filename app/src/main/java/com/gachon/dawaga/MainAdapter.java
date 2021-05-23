package com.gachon.dawaga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.dawaga.databinding.ItemMainBinding;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private Context context;
    private ArrayList<myAppointment> infoList;
    public ArrayList<Integer> calLeftTime;
    // 남은 시간 계산해서 TextView에 표시해주기 위한 변수들
    int year = 0;
    int day = 0;
    int hour = 0;
    int min = 0;
    int sec = 0;

    public interface onItemClickListener {
        void onClick(View v, myAppointment infoList);
    }

    private onItemClickListener listener = null;

    public MainAdapter(Context context, ArrayList<myAppointment> infoList, ArrayList<Integer> calLeftTime, onItemClickListener listener) {
        this.context = context;
        this.infoList = infoList;
        this.calLeftTime = calLeftTime;
        this.listener = listener;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private ItemMainBinding itemMainBinding;

        public MainViewHolder(@NonNull ItemMainBinding binding) {
            super(binding.getRoot());
            this.itemMainBinding = binding;
            itemMainBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, infoList.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        // Item 뷰에 보여줄 데이터 연결
        ItemMainBinding binding = holder.itemMainBinding;
        myAppointment currentInfo = infoList.get(position);
        Integer leftSec = calLeftTime.get(position);
        calLeftTime(leftSec);

        binding.tvTitle.setText(currentInfo.getTitle());
        binding.tvDatetime.setText(currentInfo.getDateTime());
        binding.tvLefttime.setText(year+"년"+day+"일"+hour+"시간"+min+"분 남음");
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void clear() {
        infoList.clear();
        calLeftTime.clear();
        notifyDataSetChanged();
    }

    private void calLeftTime(Integer leftSec){
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
    }

}
