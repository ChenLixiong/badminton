package com.ymq.badminton_rank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.swissrule.Member;
import com.ymq.badminton_rank.swissrule.SwissRule;

import java.util.ArrayList;
import java.util.HashMap;


public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = "ScoreActivity";
    private SwissRule mSwissRule;
    private MyApplication mApp;
    private ArrayList<Member[]> mMList;
    private ListView mListview_score;
//    private ScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mApp = (MyApplication) getApplicationContext();
        mSwissRule = MyApplication.mSwissRule;

        Intent intent = getIntent();
//        final ArrayList list = (ArrayList) intent.getSerializableExtra("list");
        final ArrayList list = MyApplication.templist;
        final ArrayList single_list = MyApplication.mSingle_tempList;

        int currentCount = intent.getIntExtra("currentCount", 1);
        boolean isDouble = intent.getBooleanExtra("isDouble",true);
        if (isDouble){
            mSwissRule.resetMap(list,currentCount,getGameTimes(list.size()),MyApplication.mode);

        }else {
            mSwissRule.resetMap(single_list,currentCount,getGameTimes(single_list.size()),MyApplication.mode);
        }
        final HashMap<Integer, ArrayList<Member>> map = mSwissRule.getMap();

      /*  mListview_score = ((ListView) findViewById(R.id.listview_score));
        mAdapter = new ScoreAdapter(map,currentCount,ScoreActivity.this);
        mListview_score.setAdapter(mAdapter);*/
        TextView tv_rank = (TextView) findViewById(R.id.tv_rank);
        initview(tv_rank,map,currentCount);
    }

    private void initview(TextView tv_rank, HashMap<Integer, ArrayList<Member>> map, int currentCount) {
        StringBuilder name = new StringBuilder();
        int length = 0;
        for (int i = currentCount ; i >= 0; i--){
            ArrayList<Member> members = map.get(i);
            if (members== null ){
                continue;
            }else{
                name.append("第"+(length+1)+"名: ");
                for (Member member : members) {
                    name.append(member.username+", ");
                    length++;
                }
                name.append("\n");
            }
        }
        tv_rank.setText(name.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mAdapter.notifyDataSetInvalidated();

    }
    private int getGameTimes(int size) {
        return size/8+6;
    }
//    private void initView(HashMap<Integer, ArrayList<Member>> map) {
//        mMList = new ArrayList<Member[]>();
//        int size = map.keySet().size();
////        int length = map.keySet().size();
//
//        for(int i = size - 1 ;i>=0;i--){
//            ArrayList<Member> ms = map.get(i);
////            System.out.printf("%d:[",i);
//            if (ms!=null){
//                Member[] member =new Member[ms.size()];
//                for(int k=0;k<ms.size();k++) {
////                Member memeber = ms.get(k);
////                    System.out.printf("%s,",ms.get(k));
//                    Member  mm  = ms.get(k);
//                    if (mm !=null){
////                        mList.add(ms.get(k));
//                        member[k] = mm;
//                    }
//                }
//                mMList.add(member);
//            }
//
////            System.out.printf("]\n");
//        }
////        System.out.println(mMList.toString());
//
//    }

    public void backToPrev(View view){
        finish();
    }

//    class ScoreAdapter extends BaseAdapter{
//
//        private final Context context;
//        private int currentCount;
//        private final HashMap<Integer, ArrayList<Member>> map;
//        int length = 0 ;
//
//        public ScoreAdapter(HashMap<Integer, ArrayList<Member>> map, int currentCount, ScoreActivity scoreActivity) {
//            this.map = map;
//            this.currentCount = currentCount ;
//            this.context = scoreActivity;
//        }
//
//        @Override
//        public int getCount() {
////            return map== null ? 0 : map.size() ;
//            return  1;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView==null){
//                convertView = View.inflate(parent.getContext(),R.layout.listview_score_item,null);
//            }
//            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            ArrayList<Member> members = map.get(currentCount);
//            StringBuilder name = new StringBuilder();
//            if (members != null){
//                for (Member member : members) {
//                    name.append(member.username+", ");
//                }
//                tv_name.setText("第" + (length+1)+" 名 :"+name.toString());
//                length +=members.size();
//            }else {
//                TextView tv = new TextView(context);
//                tv.setVisibility(View.GONE);
//                return tv;
//            }
//            currentCount --;
//            return convertView;
//        }
//
//
//
//        private int getRank(int i) {
//            int sum = 0 ;
//            for (int j = i ; i>=0;j--){
//                sum = sum + j ;
//            }
//            return sum;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMList = null ;
    }
//    private void setName(ArrayList<Member[]> list, TextView tv_name) {
//        for (int i = 0 ; i < list.size() ; i++){
//            String usernames = "" ;
//            Member[] members = list.get(i);
//            for (int j = 0 ; j<members.length ; j ++){
//                if (j == members.length - 1 ){
//                    usernames += members[j].username;
//                }else {
//                    usernames += members[j].username + " , ";
//                }
//            }
//            if (i == 0){
//                tv_name.setText("第" + (i+1) +" 名 ：" +usernames +"\n");
//            }else {
//                int sum = 0 ;
//                for (int j = i ; i>=0;j--){
//                    sum = sum + j ;
//                }
//                tv_name.setText("第" + (sum+1) +" 名 ：" +usernames +"\n");
//            }
//        }
//    }
}
