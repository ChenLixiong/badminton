package com.ymq.badminton_rank.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.swissrule.Member;
import com.ymq.badminton_rank.swissrule.SwissRule;
import com.ymq.badminton_rank.view.MemberPK;

import java.util.ArrayList;

public class MatchActivity extends BaseActivity {

    private static final String TAG = "MatchActivity";
    private LinearLayout mScrollview_wrappper;
    private Context mContext = MatchActivity.this;
    private int currentCount = 0;
    private Button mBtn_ok;
    private ArrayList<Member[]> mVsList;
    private SwissRule mSwissrule;
    private boolean mIsDouble;
    private int mTurn;
    private MyApplication mApp;
    private Button mBtn_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (MyApplication)getApplication();
        final Intent intent = getIntent();

//---------------------------------------------------------------------------------------------------------------
//        final ArrayList<Member> list = (ArrayList<Member>) intent.getSerializableExtra("data");
        final ArrayList<Member> list = new ArrayList<Member>();
        list.addAll(MyApplication.mSingle_collect_member_list);
        MyApplication.mSingle_collect_member_list.clear();
//        final ArrayList<Member> list = MyApplication.mSingle_collect_member_list;

        mIsDouble = intent.getBooleanExtra("isDouble", false);
        currentCount =intent.getIntExtra("currentcount",0);
        mTurn = getSharedPreferences("turns",MODE_PRIVATE).getInt("turn",1);

        mScrollview_wrappper = (LinearLayout) findViewById(R.id.scrollview_wrapper);
        cleanAllChildrenView(mScrollview_wrappper);

        mBtn_ok = (Button) findViewById(R.id.btn_finish);
        mBtn_score = (Button) findViewById(R.id.btn_score);
        mBtn_ok.setVisibility(View.INVISIBLE);
        TextView tv_turn = (TextView) findViewById(R.id.tv_turn);
        tv_turn.setText("当前是第"+(currentCount+1)+"轮");
        initGame(list,mIsDouble);
        //判断用户是否输入完成
        checkEditFinish(list);
        mBtn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MatchActivity.this, ScoreActivity.class);
                if (MyApplication.mSingle_tempList  != null && MyApplication.mSingle_tempList.size()>0){
                    MyApplication.mSingle_tempList.clear();
                }

                MyApplication.mSingle_tempList = list;

                intent.putExtra("currentCount",currentCount);
                intent.putExtra("isDouble",false);
                startActivity(intent);
            }
        });
        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //提交用户输入的积分
                int count  = mScrollview_wrappper.getChildCount();
                for (int i = 0;i<count; i++){
                    MemberPK memberview = (MemberPK) mScrollview_wrappper.getChildAt(i);
                    EditText[] editTexts = memberview.getEditText();
                    String score_1 = editTexts[0].getText().toString();
                    String score_2 = editTexts[1].getText().toString();

                    Member[] members = mVsList.get(i);
                    if (!TextUtils.isEmpty(score_1)){
                        if (members[0]!=null){
                            members[0].currenscore = Integer.parseInt(score_1);
                        }
                    }
                    if (!TextUtils.isEmpty(score_2)){
                        if (members[1] !=null){
                            members[1].currenscore = Integer.parseInt(score_2);
                        }
                    }
                }
                //是否比赛排名完成
                ArrayList<Member> members1 = mSwissrule.reloadSingleList(mVsList);
                currentCount += 1;
                mSwissrule.resetMap(members1,currentCount,0,0);
                if(currentCount>mTurn -1 && mSwissrule.isFullOrder(getGameTimes(list.size()),MyApplication.mode,currentCount)) {	//检查是否可以全部排出胜负，如果ok则停止比赛
                        //跳转到显示最终排名的界面
                        Intent intent = new Intent(MatchActivity.this,ResultActivity.class);
//                        intent.putExtra("data", members1);
                    MyApplication.mSingle_members1_intent = members1;
                        intent.getBooleanExtra("isDouble", false);
                        intent.putExtra("currentcount", currentCount);
                        startActivity(intent);
                        finish();

                    }else {
                        //进入下一轮比赛，显示下一轮对阵界面
//                        ArrayList<Member> members1 = mSwissrule.reloadSingleList(mVsList);
                        Intent intent = new Intent(mContext, MatchActivity.class);
//                        intent.putExtra("data", members1);
                    MyApplication.mSingle_collect_member_list = members1;
                        intent.getBooleanExtra("isDouble", false);
                        intent.putExtra("currentcount", currentCount);
                        startActivity(intent);
                        finish();
                    }
            }
        });
    }

    private int getGameTimes(int size) {
        return size/8+6;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_match;
    }

    private void checkEditFinish(ArrayList<Member> list) {

        int length = list.size();
        boolean state = showButton();
        if (state){
            mBtn_ok.setVisibility(View.VISIBLE);
        }

    }


    private boolean showButton() {
        int count = mScrollview_wrappper.getChildCount();
        ArrayList<Boolean> state = new ArrayList<>();
        for (int i = 0 ; i <count ;i++){
            MemberPK memberPk = (MemberPK) mScrollview_wrappper.getChildAt(i);
            Button button = memberPk.getButton();
            if (button.isEnabled()){
                Boolean isStore = (Boolean) button.getTag();

                if (isStore !=null){
                   /* if (isStore){
                        state.add(true);
                    }else {
                        state.add(false);
                    }*/
                    state.add(true);
                }else {
                    state.add(false);
                }
            }
        }
        for (int i = 0 ; i < state.size() ;i++){
            if (!state.get(i)){
                return false;
            }
        }
        return true;

    }
    private void initGame(ArrayList<Member> list,  boolean isDouble) {
        mSwissrule = MyApplication.mSwissRule;
        mVsList = null;
        mSwissrule.resetMap(list,currentCount,getGameTimes(list.size()),MyApplication.mode);
        mSwissrule.printMap(currentCount);

        mVsList = mSwissrule.getSingleVsList();
       mSwissrule.printVsList(mVsList,currentCount);//打印对阵关系

        initView(mVsList);

    }

    /**
     * 初始化对阵界面
     * @param vsList
     */
    private void initView(ArrayList<Member[]> vsList) {
        int length = mVsList.size();
        for (int i = 0;i<length;i++){
                //单打
                MemberPK memberpk = new MemberPK(mContext);
                TextView[] textView = memberpk.getTextView();
                EditText[] editText = memberpk.getEditText();
            final Button button = (Button) memberpk.findViewById(R.id.record_score);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MatchActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                    button.setTag(true);
                    button.setTextColor(Color.BLUE);
                    boolean b = showButton();
                    if(b){
                        mBtn_ok.setVisibility(View.VISIBLE);
                    }
                }
            });
                Member[] members = mVsList.get(i);

                if (members[0] != null){
                    textView[0].setText(members[0].username);
                }else{
                    textView[0].setText("");
                    editText[0].setEnabled(false);
                }
                if (members[1] != null){
                    textView[1].setText(members[1].username);
                }else {
                    textView[1].setText("");
                    editText[1].setVisibility(View.INVISIBLE);
                    memberpk.getImageb().setVisibility(View.INVISIBLE);
                    memberpk.getLine().setVisibility(View.INVISIBLE);
                }

                if (members[0] !=null && members[1]==null){
                    editText[0].setVisibility(View.GONE);
                    button.setEnabled(false);
                }
                mScrollview_wrappper.addView(memberpk);

        }
    }

    /**
     * 清除所有的子控件
     * @param scrollview_wrappper
     */
    private void cleanAllChildrenView(LinearLayout scrollview_wrappper) {
        int childCount = scrollview_wrappper.getChildCount();
        for(int i = 0; i<childCount ; i++){
            MemberPK view = (MemberPK) scrollview_wrappper.getChildAt(i);
           view.setVisibility(View.GONE);
        }
    }
}
