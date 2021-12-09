package com.example.tool.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tool.Activity.AddHostActivity;
import com.example.tool.Activity.AddedHost;
import com.example.tool.R;
public class Mainfragment extends Fragment {

//    添加与已添加
    private Button btn_add,btn_added;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainfragment, container, false);

        btn_add = view.findViewById(R.id.main_btn_addhost);
        btn_added = view.findViewById(R.id.main_btn_addedhost);
        setView();
        return view;
    }

    private void setView(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddHostActivity.class);
//                btn_add.setVisibility(View.GONE);
                startActivity(intent);
            }
        });

        btn_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddedHost.class);
                startActivity(intent);
            }
        });
    }

}
