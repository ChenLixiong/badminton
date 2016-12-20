package com.ymq.badminton_rank.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.activity.DoubleMatchActivity;
import com.ymq.badminton_rank.activity.MyApplication;
import com.ymq.badminton_rank.adapter.GridViewAdapter;
import com.ymq.badminton_rank.swissrule.DoubleMember;
import com.ymq.badminton_rank.swissrule.SwissRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ymq.badminton_rank.R.id.username;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoubleFragment extends Fragment {


    private static final String TAG = "DoubleFragment";
    private boolean isDouble = true;
    private ArrayList<DoubleMember> mList;
    private View mView;
    private EditText mEt_username;
    private Button mBtn_add;
    private Button mBtn_ok;
    private int id = 0;
    private GridViewAdapter mAdapter;
    private GridView mGridtview_member;


    public DoubleFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_double, container, false);
        initview();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.mCollect_member_list != null && MyApplication.mCollect_member_list.size()>0 ){
            MyApplication.mCollect_member_list.clear();
        }
        mList = new ArrayList<DoubleMember>();
        mAdapter = new GridViewAdapter(mList);
        mGridtview_member.setAdapter(mAdapter);


        //保存选手信息
        mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username  = mEt_username.getText().toString().trim();

                if (TextUtils.isEmpty(username) ){
                    Toast.makeText(getActivity(),"请先填写后保存",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DoubleMember doubleMember =new DoubleMember();
                    doubleMember.username = username;
                    doubleMember.id= id;
                    id++;
                    mList.add(doubleMember);
                    Log.i(TAG,mList.size()+"");
                    mAdapter.notifyDataSetChanged();
                    mEt_username.setText("");
                    mEt_username.requestFocus();
//                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //提交选手信息，进入排序页面
        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoubleMatchActivity.class);
                int turn = SwissRule.getMinTurnCount(mList.size());
                //将数据保存为json格式
                JSONObject game_user = new JSONObject();
                JSONArray users = new JSONArray();
                for (int i = 0 ;i <mList.size(); i++){
                    JSONObject user = new JSONObject();
                    DoubleMember doubleMember = mList.get(i);
                    try {
                        user.put("user_id",doubleMember.id);
                        user.put("username",doubleMember.username);
                        users.put(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    game_user.put("game_user",users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onClick: "+game_user.toString());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("turns", Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("turn",turn).commit();
                MyApplication.mCollect_member_list.addAll(mList);
//                intent.putExtra("data",mList);
                intent.putExtra("isDouble",true);
                if (mList.size()<4 ){
                    Toast.makeText(getActivity(), "选手人数不足", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                    getActivity().finish();

                }

            }
        });
    }



    private void initview() {
        mEt_username = (EditText) mView.findViewById(username);
        mBtn_add = (Button) mView.findViewById(R.id.btn_add);
        mBtn_ok = (Button) mView.findViewById(R.id.btn_ok);
        mGridtview_member = ((GridView) mView.findViewById(R.id.gridview_member));
        TextView emptyView = new TextView(getActivity());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("还没添加选手哦！");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)mGridtview_member.getParent()).addView(emptyView);
        mGridtview_member.setEmptyView(emptyView);

    }

}
