package com.ymq.badminton_rank.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ymq.badminton_rank.bean.GameRecord;

import java.util.ArrayList;

/**
 * Created by chenlixiong on 2016/11/26.
 */

public class RecordDAO {
    private static final String TABLE_NAME = "gamerecord";
    Context mContext;

    private final SQLiteDatabase mDb;

    public RecordDAO(Context context) {
        this.mContext = context;
        GameRecordDBHelper mHelper = new GameRecordDBHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    /**
     * 查询所有的数据
     * @return
     */
    public ArrayList<GameRecord> queryRecord(){
        ArrayList<GameRecord> gameRecords =new ArrayList<GameRecord>();
        if (mDb.isOpen()){
//            String sql ="select ?,?,? from "+TABLE_NAME;
            Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, "_id DESC ", "0," + 6);
//            mDb.execSQL(sql);
//            Cursor cursor = mDb.rawQuery(sql, new String[]{"mode", "usernames", "date"});
            if (cursor !=null){
                while (cursor.moveToNext()){

                    int mode = cursor.getInt(cursor.getColumnIndex("mode"));
                    String usernames = cursor.getString(cursor.getColumnIndex("usernames"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    int turn = cursor.getInt(cursor.getColumnIndex("turn"));
                    GameRecord gameRecord =new GameRecord(mode,usernames,date,turn);
                    gameRecords.add(gameRecord);
                }
            }
            cursor.close();
            mDb.close();
        }
        return gameRecords;
    }


    public void add(GameRecord gameRecord){
        if (mDb.isOpen()){
           /* ContentValues values = new ContentValues();
            values.put("mode",gameRecord.getMode());
            values.put("usernames",gameRecord.getUsernames());
            values.put("date",gameRecord.getDate());*/
            String sql = "insert into " +TABLE_NAME +"(mode,usernames,date,turn) VALUES(?,?,?,?)";
            mDb.execSQL(sql,new Object[]{gameRecord.getMode(),gameRecord.getUsernames(),gameRecord.getDate(),gameRecord.getTurn()});
        }
        if (mDb!=null){
            mDb.close();
        }

    }
}
