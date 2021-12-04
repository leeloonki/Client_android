package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tool.Bean.LoginResp;
import com.example.tool.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private EditText edt_username, edt_passwd;
    protected RadioButton rdio_remember;
    protected Button btn_signin, btn_signup;

    private MyDBHelper myDBHelper;
    protected String input_username = "", input_passwd = "";
    public static final String URL = "http://10.8.254.205:8081/proj";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDBHelper = new MyDBHelper(this,"db_user.db",null,1);
        initView();
        setView();
    }

    private void initView() {
        edt_username = findViewById(R.id.login_edt_username);
        edt_passwd = findViewById(R.id.login_edt_passwd);
        rdio_remember = findViewById(R.id.login_btn_remember);
        btn_signin = findViewById(R.id.login_btn_signin);
        btn_signup = findViewById(R.id.login_btn_signup);
    }

    private void setView() {
        MyClickListener onclick = new MyClickListener();
        btn_signin.setOnClickListener(onclick);
        btn_signup.setOnClickListener(onclick);

    }

    public class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn_signin:
                    Signin();
                    break;
                case R.id.login_btn_signup:
                    Signup();
                    break;
                default:
                    break;
            }
        }

    }

    private void Signin() {
        input_username = edt_username.getText().toString().trim();
        input_passwd = edt_passwd.getText().toString().trim();
        if (input_username.length() == 0 || input_passwd.length() == 0) {
            Toast.makeText(MainActivity.this, "请输入账号或密码", Toast.LENGTH_LONG).show();
            return;
        }

//        查询本地数据库
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        System.out.println("********************************************************************");
        String SQL = " select * from tb_user where username ='" + input_username + "';";
        System.out.println(SQL);
        Cursor cursor = db.rawQuery(SQL, null);
        String sql_password;
        while(cursor.moveToNext()){
            sql_password = cursor.getString(2);//获取第二列的值
            if (input_passwd==sql_password){
                cursor.close();
                db.close();
                Jmp();
                return;
            }
        }
        sendRequestWithOkHttp();
    }

    private void Signup() {
        Intent intent = new Intent(MainActivity.this,SignUp.class);
        startActivity(intent);
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", input_username);
                    obj.put("password", input_passwd);
                    System.out.println(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody Request = RequestBody.create(type, "" + obj.toString());
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url(URL + "/user/signin").post(Request)
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
        java.lang.reflect.Type type = new TypeToken<LoginResp>() {}.getType();
        final LoginResp loginResp = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(loginResp);
        System.out.println(jsonInString);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loginResp.respCode.respCode_code==200){
                    Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    if(rdio_remember.isChecked()){
                        if(rdio_remember.isChecked()){
                            SQLiteDatabase db = myDBHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("username",input_username);
                            values.put("password",input_passwd);
                            db.insert("tb_user",null,values);
                            System.out.println("数据已存入数据库");
                        }
                    }
                    Intent intent = new Intent(MainActivity.this,UserActivity.class);
                    startActivity(intent);
                }else if(loginResp.respCode.respCode_code==300){
                    Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Jmp(){
        Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,UserActivity.class);
        startActivity(intent);
    }

}
