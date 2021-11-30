package com.example.tool.Bean;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginResp {


    @SerializedName("respCode")
    public Code respCode;
    @SerializedName("user")
    public User user;


    public static class Code{

        public int respCode_code;
        public String respCode_msg;

    }

    public static class User {
        public int id;
        public String username;
        public int password;
        public int age;
        public int gender;
    }

}
