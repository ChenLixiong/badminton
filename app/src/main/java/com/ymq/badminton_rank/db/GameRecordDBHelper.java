package com.ymq.badminton_rank.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenlixiong on 2016/11/26.
 */

public class GameRecordDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "gamerecord.db";
    private static final int DATABASE_VERSION = 1;

    public GameRecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //  0 单打
    // 1 双打
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("CREATE TABLE IF NOT EXISTS gamerecord" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, mode INTEGER, usernames TEXT,date TEXT,turn INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
