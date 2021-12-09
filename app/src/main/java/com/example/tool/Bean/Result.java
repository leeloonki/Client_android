package com.example.tool.Bean;

import com.google.gson.annotations.SerializedName;

public class Result<T>{
    @SerializedName("respCode")
    public LoginResp.Code respCode;
    @SerializedName("data")
    public  T data;
}
