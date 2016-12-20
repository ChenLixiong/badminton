package com.ymq.badminton_rank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.ymq.badminton_rank.R;
import com.ymq.badminton_rank.fragment.DoubleFragment;
import com.ymq.badminton_rank.fragment.SingleFragment;
import com.ymq.badminton_rank.view.SegmentButton;

public class MainActivity extends BaseActivity {

    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mFragmentTransaction;

//    private DoubleFragment mDoubleFragment;
        private SingleFragment mSingleFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ArrayList<String> list = new ArrayList<>();
//        list.add("精确");
//        list.add("默认");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView tv_mode = (TextView) findViewById(R.id.tv_mode);
        tv_mode.setVisibility(View.GONE);
        spinner.setVisibility(View.INVISIBLE);

        /*final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.spinner_text_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                // -1 精确模式  1 默认模式
                String item = (String) adapter.getItem(arg2);
                if ("精确模式".equals(item)){
                    MyApplication.mode = -1;
                }else if ("默认模式".equals(item)){
                    MyApplication.mode = 1 ;
                }
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
//                myTextView.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });*/
        SegmentButton segmentButton = (SegmentButton) findViewById(R.id.view_segmentbutton);

//        final SingleFragment singleFragment = new SingleFragment();
        final DoubleFragment doubleFragment = new DoubleFragment();
//        initview(singleFragment);
        initview(doubleFragment);

        segmentButton.setOnSegmentButtonSelectedListener(new SegmentButton.OnSegmentButtonSelectedListener() {
            @Override
            public void onSelected(boolean isLeftSelected) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (!isLeftSelected){
                    /*if (singleFragment.isHidden()){
                        fragmentTransaction.show(singleFragment);
                        fragmentTransaction.hide(mDoubleFragment);
                    }*/
                    if (doubleFragment.isHidden()){
                        fragmentTransaction.show(doubleFragment);
                        fragmentTransaction.hide(mSingleFragment);
                    }
                }else{

                    if (mSingleFragment == null){
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

    public void check_history(View view){
       // Toast.makeText(this, "我被点击了", Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(MainActivity.this,HistoryGameActivity.class);
        startActivity(intent);
    }
    private void initview(Fragment fragment) {
        mSupportFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mSupportFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fl_position,fragment);
        mFragmentTransaction.commit();
    }
}
