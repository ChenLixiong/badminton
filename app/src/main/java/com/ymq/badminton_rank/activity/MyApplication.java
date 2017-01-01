package com.ymq.badminton_rank.activity;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.squareup.okhttp.OkHttpClient;
import com.ymq.badminton_rank.swissrule.Member;
import com.ymq.badminton_rank.swissrule.SwissRule;

import java.util.ArrayList;

/**
 * Created by chenlixiong on 2016/11/23.
 */

public class MyApplication extends Application {
    public static SwissRule mSwissRule ;
    private Context mContext;
    public static ArrayList<Member> templist ;
    public static ArrayList<Member> members1_intent;
    public static ArrayList<Member> mCollect_member_list;
    public static ArrayList<Member> mSingle_tempList ;
    public static ArrayList<Member> mSingle_collect_member_list;
    public static ArrayList<Member> mSingle_members1_intent;
    public static int version = 2;
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        public WindowManager.LayoutParams getWindowParams() {
            return windowParams;
            }

    @Override
    public void onCreate() {
        super.onCreate();
        mSwissRule = new SwissRule();
        mSwissRule.vsRuleOfSameScoreGroup = SwissRule.NEIGHBORVS;
        mSwissRule.allowSameScorePercent = 0.10;
        mContext = getApplicationContext();
        mCollect_member_list =new ArrayList<Member>();
        mSingle_collect_member_list = new ArrayList<Member>();
    }
    public static OkHttpClient okHttpClient = null;
    public static OkHttpClient getOkHttpInstance(){
        if (okHttpClient == null){
            okHttpClient =new OkHttpClient();
        }
        return okHttpClient;
    }

    public SwissRule getSwissRule() {
        if (mSwissRule == null){
            mSwissRule = new SwissRule();
        }
        return mSwissRule;
    }

    public Context getContext() {
        return mContext;
    }
}
