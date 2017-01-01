package com.ymq.badminton_rank.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ymq.badminton_rank.activity.MyApplication.mSwissRule;

public class DoubleMatchActivity extends AppCompatActivity {
    private static final String TAG = "DoubleMatchActivity";
    private MyApplication mApp;
    private boolean mIsDouble = true;
    private int currentCount;
    private int mTurn;
    private LinearLayout mScrollview_wrappper;
    private Button mBtn_ok;
    private SwissRule mSwissrule;
    private ArrayList<Member[]> mVsList;
    private Button mBtn_score;
    private HashMap<Integer, ArrayList<Member>> mMap;
    private ArrayList<Member> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_match);

        mApp = (MyApplication) getApplicationContext();
        final Intent intent = getIntent();

//        final ArrayList<Member> list = (ArrayList<Member>) intent.getSerializableExtra("data");
        mList = new ArrayList<Member>();
        mList.addAll(MyApplication.mCollect_member_list);
        MyApplication.mCollect_member_list.clear();
        mIsDouble = intent.getBooleanExtra("isDouble", true);
        //从零开始计算
        currentCount = intent.getIntExtra("currentcount", 0);
        mTurn = getSharedPreferences("turns", MODE_PRIVATE).getInt("turn", 1);
        backupCurrentRank();
        mScrollview_wrappper = (LinearLayout) findViewById(R.id.scrollview_wrapper);
        cleanAllChildrenView(mScrollview_wrappper);
        mBtn_ok = (Button) findViewById(R.id.btn_finish);
        mBtn_score = (Button) findViewById(R.id.btn_score);
        mBtn_ok.setVisibility(View.INVISIBLE);

        TextView tv_turn = (TextView) findViewById(R.id.tv_turn);
        tv_turn.setText("当前是第" + (currentCount+1) + "轮");
        initGame(mList, mIsDouble);
        //判断用户是否输入完成
        checkEditFinish(mList);
        //查看当前排名
        mBtn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoubleMatchActivity.this, ScoreActivity.class);
                /*if (MyApplication.templist  != null && MyApplication.templist.size()>0){
                    MyApplication.templist.clear();
                }*/

                MyApplication.templist = mList;
                intent.putExtra("isDouble",true);
                intent.putExtra("currentCount",currentCount);
                startActivity(intent);
            }
        });
        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = mScrollview_wrappper.getChildCount();
                JSONObject curTurn = new JSONObject();
                try {
                    curTurn.put("curTurn",currentCount+1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray duizhen = new JSONArray();//获取对阵关系和比赛得分
                for (int i = 0; i < childCount; i++) {
                    // 获取用户界面输入的数据
                    MemberPK memberview = (MemberPK) mScrollview_wrappper.getChildAt(i);
                    TextView[] textView = memberview.getTextView();
                    EditText[] editTexts = memberview.getEditText();
                    String score_1 = editTexts[0].getText().toString();
                    String score_2 = editTexts[1].getText().toString();
                    try {
                        JSONObject race =new JSONObject();
                        race.put("group1",textView[0].getText());
                        race.put("score1",score_1);
                        race.put("group2",textView[1].getText());
                        race.put("score2",score_2);
                        duizhen.put(race);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Member[] members = mVsList.get(i);
                   /* for (int m = 0 ; m < members.length ; m++){
                        Log.d(TAG, "onClick: "+members[m].toString());
                    }*/
                    if (!TextUtils.isEmpty(score_1) && members[0] != null && members[1] != null) {
                        members[0].currenscore = Integer.parseInt(score_1);
                        members[1].currenscore = Integer.parseInt(score_1);
                    }
                    if (!TextUtils.isEmpty(score_2) && members[2] != null && members[3] != null) {
                        members[2].currenscore = Integer.parseInt(score_2);
                        members[3].currenscore = Integer.parseInt(score_2);
                    }
                }
                try {
                    curTurn.put("duizhen",duizhen);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, curTurn.toString());

                //判断比赛是否完成
                ArrayList<Member> members1 = mSwissrule.reloadDoubleList(mVsList);
                currentCount += 1;
                mSwissrule.resetMap(members1,currentCount);
                if (currentCount > mTurn - 1 && mSwissrule.isFullOrder()) {
                    //比赛完成，跳转到显示结果的页面

                    Intent intent = new Intent(DoubleMatchActivity.this, ResultActivity.class);
//                    intent.putExtra("data", members1);

                    MyApplication.members1_intent = members1;
                    intent.putExtra("isDouble", true);
                    intent.putExtra("currentcount", currentCount);
                    startActivity(intent);
                    finish();
                } else {
                    //比赛未完成，继续进入下一轮

                    Intent intent = new Intent(DoubleMatchActivity.this, DoubleMatchActivity.class);
                    MyApplication.mCollect_member_list = members1;
                    intent.putExtra("isDouble", true);
                    intent.putExtra("currentcount", currentCount);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    /**
     * 保存当前轮次的排名
     */
    private void backupCurrentRank() {
        if (MyApplication.templist  != null && MyApplication.templist.size()>0){
            MyApplication.templist.clear();
        }

        MyApplication.templist = mList;
        mSwissRule.resetMap(mList,currentCount);
        final HashMap<Integer, ArrayList<Member>> map = mSwissRule.getMap();

        for (int i = currentCount ; i>=0; i--){
            ArrayList<Member> members = map.get(currentCount);
            if (members !=null){
                for (int j = 0 ;j <members.size() ; j++){
                    Member member = members.get(j);

                    Log.d(TAG, "backupCurrentRank: "+members.get(j).toString());
                }
            }
        }
       /* StringBuilder name = new StringBuilder();
        if (members != null){
            for (Member member : members) {
                name.append(member.username+", ");
            }
            tv_name.setText("第" + (length+1)+" 名 :"+name.toString());
            length +=members.size();
        }
        currentCount --;*/
    }

    /**
     * 获得加入winBall的轮次
     * 基数为6，每增加8个人加一轮
     * @param size
     */
    private int getGameTimes(int size) {
        return size/8+6;
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

    private void initGame(ArrayList<Member> list, boolean isDouble) {
        mSwissrule = MyApplication.mSwissRule;
        mSwissrule.getVsMemberVersion = MyApplication.version;
        mVsList = null;
        mSwissrule.resetMap(list, currentCount);
         mSwissrule.printMap(currentCount);
//        mVsList = isDouble ? mSwissrule.getDoubleVsList() : mSwissrule.getSingleVsList();
        mVsList = mSwissrule.getDoubleVsList();
        mSwissrule.printVsList(mVsList, currentCount);//打印对阵关系
        initView(mVsList);
    }

    private void initView(ArrayList<Member[]> vsList) {
        int length = mVsList.size();
        for (int i = 0; i < length; i++) {
            final Member[] members = mVsList.get(i);
            MemberPK memberPK_1 = new MemberPK(DoubleMatchActivity.this);
            TextView[] textView_1 = memberPK_1.getTextView();
            final EditText[] editTexts = memberPK_1.getEditText();
            final Button button = (Button) memberPK_1.findViewById(R.id.record_score);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Toast.makeText(DoubleMatchActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                        button.setTag(true);
                    button.setTextColor(Color.BLUE);
                    boolean b = showButton();
                    if(b){
                        mBtn_ok.setVisibility(View.VISIBLE);
                    }
                }
            });
            if (members[0] != null && members[1] != null) {
                textView_1[0].setText(members[0].username + " & " + members[1].username);
            } else {
                textView_1[0].setText(members[0].username);
                editTexts[0].setVisibility(View.INVISIBLE);
                editTexts[1].setVisibility(View.INVISIBLE);
                textView_1[1].setVisibility(View.INVISIBLE);
                memberPK_1.getLine().setVisibility(View.INVISIBLE);
                memberPK_1.getImageb().setVisibility(View.INVISIBLE);
                button.setEnabled(false);
            }

            if (members[2] != null && members[3] != null) {
                textView_1[1].setText(members[2].username + " & " + members[3].username);
            } else {
                editTexts[1].setVisibility(View.INVISIBLE);
                memberPK_1.getLine().setVisibility(View.INVISIBLE);
                memberPK_1.getImageb().setVisibility(View.INVISIBLE);
                button.setEnabled(false);
            }
            mScrollview_wrappper.addView(memberPK_1);
        }

    }

    private void cleanAllChildrenView(LinearLayout scrollview_wrappper) {
        int childCount = scrollview_wrappper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            MemberPK view = (MemberPK) scrollview_wrappper.getChildAt(i);
            view.setVisibility(View.GONE);
        }
    }

}
