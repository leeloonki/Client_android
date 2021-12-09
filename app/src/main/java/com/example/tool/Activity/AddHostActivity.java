package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tool.Bean.RespCode;
import com.example.tool.R;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddHostActivity extends AppCompatActivity {

    Button btn_add,btn_cancel;
    EditText edt_host,edt_port,edt_username,edt_password;


    String host,username,password;
    int port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_host);

        //绑定控件
        initView();
        //保存按钮功能的实现
        btnSave();
        //取消按钮功能的实现
        btnCancel();
    }

     //绑定控件
    private void initView(){
        btn_add = findViewById(R.id.addhost_btn_addhost);
        btn_cancel = findViewById(R.id.addhost_btn_cancel);
        edt_host = findViewById(R.id.addhost_edt_host);
        edt_port = findViewById(R.id.addhost_edt_port);
        edt_username = findViewById(R.id.addhost_edt_username);
        edt_password = findViewById(R.id.addhost_edt_password);
     }
     //保存按钮功能的实现
    private void btnSave(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addhost();
            }
        });
    }

    private void addhost(){
        host = edt_host.getText().toString().trim();
        port = Integer.parseInt(edt_port.getText().toString().trim());
        username =edt_username.getText().toString().trim();
        password =edt_password.getText().toString().trim();
        sendRequestWithOkHttp();
    }


     //取消按钮功能的实现
    private void btnCancel(){
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddHostActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
    }


    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uid", Utils.user.id);
                    obj.put("host", host);
                    obj.put("port", port);
                    obj.put("username",username);
                    obj.put("password",password);
                    System.out.println(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody Request = RequestBody.create(type, "" + obj.toString());
                try {
                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url(Utils.URL + "/host/addhost").post(Request)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsonData) {

        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<RespCode>() {}.getType();
        final RespCode respCode = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(respCode);
        System.out.println(jsonInString);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(respCode.respCode_code==200){
                    Toast.makeText(AddHostActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(AddHostActivity.this,UserActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AddHostActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
