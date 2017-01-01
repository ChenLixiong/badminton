package com.ymq.badminton_rank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.bean.GameRecord;
import com.ymq.badminton_rank.db.RecordDAO;
import com.ymq.badminton_rank.swissrule.Member;
import com.ymq.badminton_rank.swissrule.SwissRule;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends BaseActivity {

    private static final String TAG = "ResultActivity";
    private MyApplication mApp;
    private boolean mIsDouble;
    private SwissRule mSwissrule;
    private int currentCount;
    private ArrayList<Member[]> mVsList;
    private ArrayList<Member> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (MyApplication) getApplicationContext();
        final Intent intent = getIntent();

//        final ArrayList<Member> list = (ArrayList<Member>) intent.getSerializableExtra("data");
        final ArrayList<Member> list = MyApplication.members1_intent;//双打的数据
        final ArrayList<Member> single_list = MyApplication.mSingle_members1_intent;//单打的数据

        mIsDouble = intent.getBooleanExtra("isDouble", false);
        currentCount = intent.getIntExtra("currentcount", 0);
        if (mIsDouble){
            initGame(list, mIsDouble);
        }else {
            initGame(single_list,mIsDouble);
        }

        ListView rank_listview = (ListView) findViewById(R.id.rank_listview);
        rank_listview.setAdapter(new RankAdapter(mList));

        Button btn_back = (Button) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordDAO recordDAO = new RecordDAO(ResultActivity.this);
                String usernames = "";
                for (Member member : mList) {
                    usernames = usernames + member.username + ",";
                }
                int mode = mIsDouble ? 1 : 0;
                String date = System.currentTimeMillis() + "";
                int turn = currentCount;
                GameRecord gameRecord = new GameRecord(mode, usernames, date,turn);
                recordDAO.add(gameRecord);
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_result;
    }

    class RankAdapter extends BaseAdapter {

        private final ArrayList<Member> list;

        public RankAdapter(ArrayList<Member> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.rank_list_item, null);
                holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
                holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Member member = list.get(position);
            holder.tv_rank.setText("第 " + (position + 1) + "名");
            holder.tv_username.setText(member.username);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_rank;
        TextView tv_username;
    }
    private int getGameTimes(int size) {
        return size/8+6;
    }

    private void initGame(ArrayList<Member> list, boolean isDouble) {
        /*for (Member member : list) {
            Log.i(TAG,member.toString());
        }*/
        mSwissrule = MyApplication.mSwissRule;
        mVsList = null;
        mSwissrule.resetMap(list, currentCount);
        mSwissrule.printMap(currentCount);
//        mSwissrule.printMap(currentCount);
        HashMap<Integer, ArrayList<Member>> map = mSwissrule.getMap();

        initView(map, list.size());
    }

    private void initView(HashMap<Integer, ArrayList<Member>> map, int size) {
        mList = new ArrayList<Member>();
//        int size = map.keySet().size();
        for (int i = size - 1; i >= 0; i--) {
            ArrayList<Member> ms = map.get(i);
            System.out.printf("%d:[", i);
            if (ms != null) {
                for (int k = 0; k < ms.size(); k++) {
//                Member memeber = ms.get(k);
                    System.out.printf("%s,", ms.get(k));
                    Member mm = ms.get(k);
                    if (mm != null) {
                        mList.add(ms.get(k));
                    }
                }
            }
            System.out.printf("]\n");
        }
        System.out.println(mList.toString());
    }
}
