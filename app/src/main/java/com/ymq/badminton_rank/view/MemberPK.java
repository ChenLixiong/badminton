package com.ymq.badminton_rank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.ymq.badminton_rank.R;

import static com.ymq.badminton_rank.R.id.tv_left;
import static com.ymq.badminton_rank.R.id.tv_right;

/**
 * Created by chenlixiong on 2016/11/21.
 */

public class MemberPK extends LinearLayout{

    private View mView;
    private Button mBtn;

    public MemberPK(Context context) {
       this(context,null);
    }

    public MemberPK(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MemberPK(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = LayoutInflater.from(context).inflate(R.layout.view_memberpk, this, true);
        mBtn = (Button) mView.findViewById(R.id.record_score);
    }

    public TextView[] getTextView(){
        TextView[] textViews = new TextView[2];
        TextView left_name = (TextView) mView.findViewById(tv_left);
        TextView right_name = (TextView) mView.findViewById(tv_right);
        textViews[0] = left_name;
        textViews[1] = right_name;
        return textViews;
    }
    public Button getButton (){
        return mBtn;
    }
    public void btnClick(){
        if (mOnButtonListener != null){
            mOnButtonListener.onClick();
        }
    }

    private OnButtonListener mOnButtonListener;

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        mOnButtonListener = onButtonListener;
    }

    interface OnButtonListener{
        public void onClick();
    }

    public EditText[] getEditText(){
        EditText[] editTexts =new EditText[2];
        EditText et_left_score = (EditText) mView.findViewById(R.id.left_score);
        EditText et_right_score = (EditText) mView.findViewById(R.id.right_score);
        editTexts[0] = et_left_score;
        editTexts[1] = et_right_score;
        return editTexts;
    }

    public CircularImageView getImageb(){
        return (CircularImageView) mView.findViewById(R.id.imgb);
    }

    public CircularImageView getImagea(){
        return (CircularImageView) mView.findViewById(R.id.img);
    }

    public View getLine(){
        return mView.findViewById(R.id.line);
    }

}
