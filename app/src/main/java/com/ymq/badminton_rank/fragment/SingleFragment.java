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
import com.ymq.badminton_rank.activity.MatchActivity;
import com.ymq.badminton_rank.activity.MyApplication;
import com.ymq.badminton_rank.adapter.GridViewAdapter;
import com.ymq.badminton_rank.swissrule.SingleMember;
import com.ymq.badminton_rank.swissrule.SwissRule;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleFragment extends Fragment {


    private static final String TAG = "SingleFragment";
    private ArrayList<SingleMember> mList;
    private EditText mEt_username;
    private View mView;
    private Button mBtn_add;
    private Button mBtn_ok;
    private int id = 0;
    private boolean isDouble = false;
    private GridView mGridtview_member;
    private GridViewAdapter mAdapter;


    public SingleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList = new ArrayList<SingleMember>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_single, container, false);
        initview();

        return mView;
    }

    @Override
    public void onResume() {
    super.onResume();
        mAdapter = new GridViewAdapter(mList);
        mGridtview_member.setAdapter(mAdapter);
        TextView emptyView = new TextView(getActivity());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("还没添加选手哦！");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)mGridtview_member.getParent()).addView(emptyView);
        mGridtview_member.setEmptyView(emptyView);
        if (MyApplication.mSingle_collect_member_list != null && MyApplication.mSingle_collect_member_list.size()>0 ){
            MyApplication.mSingle_collect_member_list.clear();
        }
        //-------------------------
        mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username  = mEt_username.getText().toString().trim();
                if (TextUtils.isEmpty(username) ){
                    Toast.makeText(getActivity(),"请先填写后保存",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                   SingleMember singleMember =new SingleMember();
                    singleMember.username = username;
                    singleMember.id= id;
                    id++;
                    mList.add(singleMember);
                    Log.i(TAG,mList.size()+"");
                    mEt_username.setText("");
                    mEt_username.requestFocus();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MatchActivity.class);
                int turn = SwissRule.getMinTurnCount(mList.size());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("turns", Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("turn",turn).commit();
                MyApplication.mSingle_collect_member_list.addAll(mList);
//                intent.putExtra("data",mList);
                intent.putExtra("isDouble",false);
                if (mList.size()<=1){
                    Toast.makeText(getActivity(), "选手人数不够", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

    private void initview() {
        mEt_username = (EditText) mView.findViewById(R.id.username);
        mBtn_add = (Button) mView.findViewById(R.id.btn_add);
        mBtn_ok = (Button) mView.findViewById(R.id.btn_ok);
        mGridtview_member = ((GridView) mView.findViewById(R.id.gridview_member));

    }


}
