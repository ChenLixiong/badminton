package com.ymq.badminton_rank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.fragment.DoubleFragment;
import com.ymq.badminton_rank.fragment.SingleFragment;
import com.ymq.badminton_rank.view.SegmentButton;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private SingleFragment mSingleFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> list = new ArrayList<>();
//        list.add("精确");
//        list.add("默认");
        list.add("V1");
        list.add("V2");
        list.add("V0");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView tv_mode = (TextView) findViewById(R.id.tv_mode);
//        tv_mode.setVisibility(View.GONE);
//        spinner.setVisibility(View.INVISIBLE);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

//                        version = V0 第一候选人的对手必须都没打过  0
//                        version = V1 对手优先选择第一候选人都没打过的对手 1
//                        version = V2 对手只要没合作过就行了 2
                String item = (String) adapter.getItem(arg2);
                if ("V2".equals(item)) {
                    MyApplication.version = 2;
                } else if ("V1".equals(item)) {
                    MyApplication.version = 1;
                } else if ("V0".equals(item)) {
                    MyApplication.version = 0;
                }
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
        SegmentButton segmentButton = (SegmentButton) findViewById(R.id.view_segmentbutton);

        final DoubleFragment doubleFragment = new DoubleFragment();
        initview(doubleFragment);

        segmentButton.setOnSegmentButtonSelectedListener(new SegmentButton.OnSegmentButtonSelectedListener() {
            @Override
            public void onSelected(boolean isLeftSelected) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (!isLeftSelected) {

                    if (doubleFragment.isHidden()) {
                        fragmentTransaction.show(doubleFragment);
                        fragmentTransaction.hide(mSingleFragment);
                    }
                } else {

                    if (mSingleFragment == null) {
                        mSingleFragment = new SingleFragment();
                        initview(mSingleFragment);
                    }
                    fragmentTransaction.show(mSingleFragment);
                    fragmentTransaction.hide(doubleFragment);
                }
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    public void check_history(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryGameActivity.class);
        startActivity(intent);
    }

    private void initview(Fragment fragment) {
        mSupportFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mSupportFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fl_position, fragment);
        mFragmentTransaction.commit();
    }
}
