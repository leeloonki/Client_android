package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tool.Bean.Host;
import com.example.tool.Bean.RespCode;
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

public class AddedHost extends AppCompatActivity {

    ListView listView;

    //    下拉单 适配器ArrayList
    List<String> HostList = new ArrayList<>();
    int POS;
    //    数据
    List<Host> AllHost;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_host);

        listView = findViewById(R.id.addedhost_listview_host);
        sendRequestWithOkHttp();

//        为item点击事件绑定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                for(int i=0;i<adapterView.getCount();i++){
                    View v=adapterView.getChildAt(i);
                    if (pos== i) {
                        POS =pos;
                        Host host = AllHost.get(POS);
                        System.out.println("已选item序号为："+POS);
                        Intent intent = new Intent(AddedHost.this,SSHconn.class);
                        intent.putExtra("host",new Gson().toJson(host));
                        startActivity(intent);
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for(int i=0;i<adapterView.getCount();i++){
                    View v=adapterView.getChildAt(i);
                    if (pos== i) {
                        Host host = AllHost.get(i);
//                        Toast.makeText(AddedHost.this, "删除成功"+ host.toString(), Toast.LENGTH_SHORT).show();
                        deleteHost(host);
                        System.out.println("删除成功"+host.toString());
                    }
                }
//                itemLongClickListener返回false时就会触发list view的点击事件，所以吧返回的false改为true即可
                return true;
            }
        });


//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                for(int i=0;i<adapterView.getCount();i++){
//                    View v=adapterView.getChildAt(i);
//                    if (pos== i) {
//                        POS =pos;
//                        Host host = AllHost.get(POS);
//                        System.out.println("已选item序号为："+POS);
//                        Intent intent = new Intent(AddedHost.this,SSHconn.class);
//                        intent.putExtra("host",new Gson().toJson(host));
//                        startActivity(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
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
                            .url(Utils.URL + "/host/queryhost").post(Request)
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
        java.lang.reflect.Type type = new TypeToken<Result<List<Host>>>() {}.getType();
        final Result<List<Host>> hostResult = gson.fromJson(jsonData, type);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(hostResult.respCode.respCode_code==200){
                    AllHost = hostResult.data;
                    int i=0;
                    for(Host host:AllHost){
//                        HostList.add(host.toString());
                        i++;
                        HostList.add("---"+"记录" + "["+i+"]" +"---"+ "\n"+ "主机："+ host.host+"\n"+ "端口："+ host.port+"\n"+ "账号："+ host.username+"\n"+ "密码："+ host.password );
                        adapter = new ArrayAdapter<String>(AddedHost.this, android.R.layout. simple_list_item_1, HostList);
                        //6将适配器和布局管理器加载到控件当中
                        listView.setAdapter(adapter);
                    }

                }
            }
        });
    }

    private void deleteHost(final Host host){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", host.id);
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
                            .url(Utils.URL + "/host/deletehost").post(Request)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parsedelete(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parsedelete(String jsonData) {

        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<RespCode>() {}.getType();
        final RespCode respCode = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(respCode);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(respCode.respCode_code==200){
                    Toast.makeText(AddedHost.this,"删除成功",Toast.LENGTH_SHORT).show();
//                    为解决删除item后页面不同步更新问题，通过重现开始活动，以便每次进入Added活动时，均是从服务器请求的数据
                    Intent intent = new Intent(AddedHost.this,AddedHost.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


}
