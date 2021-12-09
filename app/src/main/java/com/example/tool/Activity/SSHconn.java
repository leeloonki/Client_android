package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tool.Bean.Cmds;
import com.example.tool.Bean.Host;
import com.example.tool.Bean.RespCode;
import com.example.tool.R;
import com.example.tool.SSHTools.Shell;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SSHconn extends AppCompatActivity {

    TextView sshinfo,conninfo,hostinfo;
    Button btn_conn,btn_exec,btn_cancel,btn_clear;
    EditText edt_inputcmd;
    Host host;
    String execLog;
    String histroy="";
    Cmds cmds = new Cmds();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sshconn);

        Intent intent = getIntent();
        String JsonData = intent.getStringExtra("host");
        host= new Gson().fromJson(JsonData,Host.class);
        System.out.println(host.toString());

        cmds.host=host.host;
        cmds.uid=host.uid;

        initView();

        setView();

    }

    private void initView(){
        hostinfo = findViewById(R.id.sshconn_txt_hostinfo);
        hostinfo.setText("主机:"+ host.host+ '\n' + "端口:"+ host.port+ '\n' + "账号:"+ host.username+ '\n');


        conninfo =findViewById(R.id.sshconn_txt_conninfo);
        sshinfo =findViewById(R.id.sshconn_txt_sshinfo);
        btn_cancel=findViewById(R.id.sshconn_btn_cancel);
        btn_conn =findViewById(R.id.sshconn_btn_conn);
        btn_exec =findViewById(R.id.sshconn_btn_exec);
        btn_clear =findViewById(R.id.sshconn_btn_clear);
        edt_inputcmd =findViewById(R.id.sshconn_edt_cmd);
    }

    private void setView() {
        MyClickListener onclick = new MyClickListener();
        btn_cancel.setOnClickListener(onclick);
        btn_exec.setOnClickListener(onclick);
        btn_conn.setOnClickListener(onclick);
        btn_clear.setOnClickListener(onclick);
    }

    public class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sshconn_btn_conn:
                    conn();
                    break;
                case R.id.sshconn_btn_exec:
                    exec();
                    break;

                case R.id.sshconn_btn_cancel:
                    cancel();
                    break;
                case R.id.sshconn_btn_clear:
                    clear();
                    break;
                default:
                    break;
            }
        }

    }
    private void conn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = "w";
                Shell shell = new Shell(host.host, host.username, host.password);
                execLog = shell.execCommand(cmd);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(execLog);
                        sshinfo.setText(execLog);
                        Toast.makeText(SSHconn.this,"连接成功!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void exec(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = edt_inputcmd.getText().toString();
                System.out.println(cmd);
                cmds.command=cmd;
                Shell shell = new Shell(host.host, host.username, host.password);
                execLog = shell.execCommand(cmd);
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                final String dateTime = df.format(date); // Formats a Date into a date/time string.
                cmds.cmdtime =  dateTime;
                sendRequestWithOkHttp(cmd);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 一、获取当前系统时间和日期并格式化输出:
                        histroy = histroy + "\n"+ dateTime + "\n"+ execLog;
                        conninfo.setText(histroy);
//                        System.out.println("*****************"+cmds.toString());
                        Toast.makeText(SSHconn.this,"执行成功!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).start();
    }

    private void cancel(){
        Intent intent = new Intent(SSHconn.this,AddedHost.class);
        startActivity(intent);
    }

    private void clear(){
        conninfo.setText("");
    }


    private void sendRequestWithOkHttp(String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uid", cmds.uid);
                    obj.put("host", cmds.host);
                    obj.put("command", cmds.command);
                    obj.put("cmdtime", cmds.cmdtime);
                    System.out.println("obj:"+obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody Request = RequestBody.create(type, "" + obj.toString());
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url(Utils.URL + "/cmd/addcmds").post(Request)
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
                    Toast.makeText(SSHconn.this,"添加成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SSHconn.this,"失败错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
