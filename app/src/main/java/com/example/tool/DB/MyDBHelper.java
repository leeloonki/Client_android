package com.example.tool.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DBNAME="terminal.db";
    private static final int VERSION=1;
    //  构造函数
    public MyDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    //1 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        db.execSQL("create table tb_user(id integer primary key autoincrement,username varchar(20),password varchar(20))");
    }

    //2 升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
