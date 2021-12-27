package com.gachon.MyPlanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.MyPlanner.databinding.ItemBoardBinding;
import com.gachon.MyPlanner.util.Util;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder>{
    private Context context;
    private ArrayList<myAppointment> infoList;

    public interface onItemClickListener {
        void onClick(View v, myAppointment infoList);
    }

    private onItemClickListener listener = null;

    public BoardAdapter(Context context, ArrayList<myAppointment> infoList, onItemClickListener listener) {
        this.context = context;
        this.infoList = infoList;
        this.listener = listener;
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private ItemBoardBinding itemBoardBinding;

        public BoardViewHolder(@NonNull ItemBoardBinding binding) {
            super(binding.getRoot());
            this.itemBoardBinding = binding;

            itemBoardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, infoList.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public BoardAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.BoardViewHolder holder, int position) {
        ItemBoardBinding binding = holder.itemBoardBinding;
        myAppointment currentInfo = infoList.get(position);
        binding.tvTitle.setText(currentInfo.getTitle());
        binding.tvDatetime.setText(Util.splitDateTime(currentInfo.getDateTime(),1));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void addItem(myAppointment info) {
        infoList.add(info);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<myAppointment> info) {
        infoList.clear();
        infoList.addAll(info);
        notifyDataSetChanged();
    }

    public void clear() {
        infoList.clear();
        notifyDataSetChanged();
    }
}
