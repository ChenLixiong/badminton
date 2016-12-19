package com.ymq.badminton_rank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymq.badminton_rank.R;


//布局SegmentButton并抽取自定义控件
public class SegmentButton extends RelativeLayout implements OnClickListener {

	private TextView tvLeft;
	private TextView tvRight;
	private boolean isLeftSelected;


	public SegmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.view_segment_button, this);
		
		tvLeft = (TextView) findViewById(R.id.tv_left);
		tvRight = (TextView) findViewById(R.id.tv_right);

		
		tvLeft.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		
		//默认进来 选择左边
		setLeftSelected(false);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left:
			setLeftSelected(true);
			break;
		case R.id.tv_right:
			setLeftSelected(false);
			break;

		default:
			break;
		}
	}
	
	public boolean isLeftSelected() {
		return isLeftSelected; 
	}
	
	/**
	 * 当左右被点击时会调用   
	 * @param isLeftSelected  为true则是点击的左边  为false则是点击的右边
	 */
	public void setLeftSelected(boolean isLeftSelected) {
		this.isLeftSelected = isLeftSelected;

		//左右互斥

		if (isLeftSelected) {
			tvLeft.setSelected(true);
			tvRight.setSelected(false);
			//因为自定义控件很多地方都会用到  所以加载数据肯定都不一样,所以设置数据的操作 不能放到自定义控件中
//			Utils.toast(getContext(), "选择了左边");
//			if (listener != null) {
//				listener.onSelected(true);
//			}
		} else {
			tvRight.setSelected(true);
			tvLeft.setSelected(false);
//			Utils.toast(getContext(), "选择了右边");
//			if (listener != null) {
//				listener.onSelected(false);
//			}
		}
		
		if (listener != null) {
			//isLeftSelected : true 点击左边时
			//isLeftSelected : false 点击右边时
			listener.onSelected(isLeftSelected);
		}
	}
	
	OnSegmentButtonSelectedListener listener;
	
	public void setOnSegmentButtonSelectedListener(OnSegmentButtonSelectedListener listener) {
		this.listener = listener;
	}

	
	public interface OnSegmentButtonSelectedListener {
		void onSelected(boolean isLeftSelected);
	}

}
