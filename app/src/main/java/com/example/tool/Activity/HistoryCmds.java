package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tool.Bean.Cmds;
import com.example.tool.Bean.Host;
import com.example.tool.Bean.Result;
import com.example.tool.R;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryCmds extends AppCompatActivity {

    ListView listView;

    //    下拉单 适配器ArrayList
    List<String> cmdsList = new ArrayList<>();
    int POS;
    //    数据
    List<Cmds> AllCmds;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_host);

        listView = findViewById(R.id.addedhost_listview_host);
        sendRequestWithOkHttp();
    }
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uid", Utils.user.id);
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
                            .url(Utils.URL + "/cmd/querycmds").post(Request)
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
        java.lang.reflect.Type type = new TypeToken<Result<List<Cmds>>>() {}.getType();
        final Result<List<Cmds>> cmdsResult = gson.fromJson(jsonData, type);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(cmdsResult.respCode.respCode_code==200){
                    AllCmds = cmdsResult.data;
                    int i=0;
                    for(Cmds cmds:AllCmds){
//                        cmdsList.add(cmds.toString());
                        i++;
                        cmdsList.add("----------------------"+"记录" + "["+i+"]" +"----------------------"+ "\n"+ "主机："+ cmds.host+"\n"+"命令："+cmds.command+ "\n"+"执行时间："+cmds.cmdtime);
                        adapter = new ArrayAdapter<String>(HistoryCmds.this, android.R.layout. simple_list_item_1, cmdsList);
                        //6将适配器和布局管理器加载到控件当中
                        listView.setAdapter(adapter);
                    }

                }
            }
        });
    }
}
