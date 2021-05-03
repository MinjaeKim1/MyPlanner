package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Main_fragment1 extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_main_fragment1, container, false);
        Button calendar_btn = root.findViewById(R.id.calendar_btn);
        calendar_btn.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendar_btn:
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
        }
    }
}


