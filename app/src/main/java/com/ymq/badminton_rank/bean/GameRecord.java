package com.ymq.badminton_rank.bean;

/**
 * Created by chenlixiong on 2016/11/26.
 */

public class GameRecord {
//    private int id;
    private int mode;
    private String usernames;
    private String date;
    private int turn;

    public GameRecord() {
    }

    public GameRecord( int mode, String usernames, String date,int turn) {

        this.mode = mode;
        this.usernames = usernames;
        this.date = date;
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public int getMode() {
        return mode;
    }

    public String getUsernames() {
        return usernames;
    }

    public String getDate() {
        return date;
    }
}
