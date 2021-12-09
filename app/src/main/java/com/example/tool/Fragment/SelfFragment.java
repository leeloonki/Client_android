package com.example.tool.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tool.Activity.MainActivity;
import com.example.tool.Activity.UserActivity;
import com.example.tool.R;
import com.example.tool.Utils.Utils;

public class SelfFragment extends Fragment {


    Button btn_modify,btn_cancel,btn_exit;
    TextView txt_username,txt_email,txt_gender;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self, container, false);

        btn_cancel = view.findViewById(R.id.self_btn_cancel);
        btn_modify = view.findViewById(R.id.self_btn_modifyinfo);
        btn_exit = view.findViewById(R.id.self_btn_exit);

        txt_username = view.findViewById(R.id.frag_self_username);
        txt_email = view.findViewById(R.id.frag_self_email);
        txt_gender = view.findViewById(R.id.frag_self_gender);

        txt_username.setText(Utils.user.username);
        txt_email.setText(Utils.user.email);
        if(Utils.user.gender==1){
            txt_gender.setText("男");
        }else{
            txt_gender.setText("女");
        }
        setView();
        return view;
    }

    private void setView(){

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
