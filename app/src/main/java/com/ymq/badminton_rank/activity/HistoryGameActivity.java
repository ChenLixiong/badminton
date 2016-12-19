package com.ymq.badminton_rank.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.bean.GameRecord;
import com.ymq.badminton_rank.db.RecordDAO;
import com.ymq.badminton_rank.utils.CommonUtils;

import java.util.ArrayList;

public class HistoryGameActivity extends BaseActivity {


    private ArrayList<GameRecord> mQueryRecord;
    private GridView mHistoryGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecordDAO recordDAO = new RecordDAO(HistoryGameActivity.this);
        mQueryRecord = recordDAO.queryRecord();
        mHistoryGridView = ((GridView) findViewById(R.id.gridview_history));
        mHistoryGridView.setAdapter(new HistoryAdapter(mQueryRecord,HistoryGameActivity.this));

    }

    @Override
    public int getLayout() {
        return R.layout.activity_history_game;
    }

    public void backToMain(View view){
       /* Intent intent =new Intent(HistoryGameActivity.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }
    class HistoryAdapter extends BaseAdapter{

        private final ArrayList<GameRecord> list;
        private  Context context;
        public HistoryAdapter(ArrayList<GameRecord> queryRecord, HistoryGameActivity historyGameActivity) {
            this.list = queryRecord;
            context = historyGameActivity;
        }

        @Override
        public int getCount() {
            return list == null ? 0 :list.size();
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
            GameRecord gameRecord = list.get(position);
            String usernames = gameRecord.getUsernames();
            String[] members = usernames.split(",");
            convertView = View.inflate(context,R.layout.gridview_history_item,null);
            LinearLayout ll_wrap = (LinearLayout) convertView.findViewById(R.id.ll_wrap);
            TextView timeAndMode = (TextView) convertView.findViewById(R.id.tv_game_time_mode);
            int mode_num = gameRecord.getMode();
            String mode = mode_num == 0 ? "单打" :"双打";
            String date = gameRecord.getDate();
            int turn  = gameRecord.getTurn();
            String nowDate = CommonUtils.refFormatNowDate(Long.parseLong(date));

            timeAndMode.setText(nowDate +"\n"+mode + "  共"+turn+"轮");
            for (int i = 0 ;i <members.length;i++ ){
                TextView item = new TextView(context);
                item.setText("第 "+ (i+1) +" 名： " +members[i]);
                item.setGravity(Gravity.CENTER_HORIZONTAL);
                item.setPadding(5,2,5,2);
                ll_wrap.addView(item);
            }
            return convertView;
        }

    }

}
