package com.android.caij.autoslideviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * -----------------------------------------------------------------
 *
 * Copyright (C) 2014  ChunChen
 *
 * AutoSlideViewPager
 * 
 * @version: v1.0.0 
 *
 * @author: Caij
 *
 * Create Date: 2014.08.22
 *
 * -----------------------------------------------------------------
 */
public class AutoSlideViewPager extends RelativeLayout implements OnPageChangeListener {
	
	private static final int WHAT = 1;
	private static final int INTERVAL_TIME = 2000;
	
	private Context      	context;
	private ViewPager    	mViewPager;
	/** Describe the title of the current page */
	private TextView 		mDescriptionTextView;
	/** Point layout of the current page*/
	private LinearLayout 	mPointLinearLayout;
	
	/**Whether automatic scroll*/
	private boolean 		isAuto = false;
	private int 			mIntervalTime = INTERVAL_TIME;
	/** whether stop auto scroll when touching, default is true **/
	private boolean			stopScrollWhenTouch = true;
	private boolean			isStopByTouch = false;

	private Handler 		mHandler;
	
	/**point image resource id*/
	private int 			mNormalPointImageResid;
	private int 			mSelectPointImageResid;

	private boolean 		isShowPoint = true;
	private boolean 		isShowTitle = true;
	
	/**previous page index*/
	private Integer				mPreviousItem = null;
	
	private AutoSlideViewPageAdapter mAdapter;
	
	public AutoSlideViewPager(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public AutoSlideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	private void initView() {
		View.inflate(context, R.layout.auto_scroll_viewpage, this);
		mViewPager = (ViewPager) this.findViewById(R.id.item_viewpage);
		mDescriptionTextView = (TextView) this.findViewById(R.id.item_tv_desc);
		mPointLinearLayout = (LinearLayout) this.findViewById(R.id.item_llayout_point);
		mNormalPointImageResid = R.drawable.point_normal;
		mSelectPointImageResid = R.drawable.point_select;
		
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void setAdapter(AutoSlideViewPageAdapter adapter) {
		this.mAdapter = adapter;
		mViewPager.setAdapter(adapter);
		if (isShowPoint) {
			mPointLinearLayout.removeAllViews();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(4, 4, 4, 4);
			for(int i = 0; i < adapter.getPageCount(); i++) {
				ImageView pointImage = new ImageView(context);
				pointImage.setLayoutParams(params);
				pointImage.setBackgroundResource(mNormalPointImageResid);
				mPointLinearLayout.addView(pointImage);
			}
		}
		mViewPager.setCurrentItem(mAdapter.getPageCount()* 100);
	}
	
	/**
	 * point image resource id
	 * @param normal
	 * @param select
	 */
	public void setPointImageResId(int normal, int select) {
		this.mNormalPointImageResid = normal;
		this.mSelectPointImageResid = select;
	}
	
	/**
	 * Set title of the current page
	 * @param charSequence
	 */
	public void setCurrentPageDescription(CharSequence charSequence) {
		mDescriptionTextView.setText(charSequence);
	}
	
	/**
	 * Set page interactive time interval
	 * @param mIntervalTime
	 */
	public void setIntervalTime(int intervalTime) {
		this.mIntervalTime = intervalTime;
	}

	/**
	 * start auto slide
	 * @param delaytime  slide  interval time
	 */
	public void startSlide() {
		isAuto = true;
		if (mHandler == null) {
			mHandler = new MHandler();
		}
		mHandler.removeMessages(WHAT);
		mHandler.sendEmptyMessageDelayed(WHAT, mIntervalTime);
	}
	
	/**
	 * stop auto slide
	 * Suggest to destroy at the interface between the time to stop
	 */
	public void stopSlide() {
		isAuto = false;
		if (mHandler != null) {
			mHandler.removeMessages(WHAT);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (stopScrollWhenTouch) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN && isAuto) {
				isStopByTouch = true;
				stopSlide();
			}
			else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
				startSlide();
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	/**
     * set whether stop auto scroll when touching, default is true
     * 
     * @param stopScrollWhenTouch
     */
	public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
		this.stopScrollWhenTouch = stopScrollWhenTouch;
	}

	public void setCurrentItem(int item) {
		mViewPager.setCurrentItem(item);
	}
	
	public int getCurrentItem() {
		return mViewPager.getCurrentItem() % mAdapter.getPageCount();
	}
	
	
	/**
	 * Whether the display point images
	 * @param isShowPoint
	 */
	public void setShowPoint(boolean isShowPoint) {
		this.isShowPoint = isShowPoint;
	}
	
	public void setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int i, float v, int j) {
	}

	@Override
	public void onPageSelected(int position) {
		if (isShowPoint) {
			changePointImageState(mAdapter.getPosition(position), true);
			if(null != mPreviousItem)
				changePointImageState(mAdapter.getPosition(mPreviousItem), false);
			mPreviousItem = position;
		}
		if(isShowTitle && null != mAdapter.getPageTitle(position)) {
			setCurrentPageDescription(mAdapter.getPageTitle(position));
		}
	}
	
	private void changePointImageState(int index, boolean isSelect) {
		ImageView poiImageView = (ImageView) mPointLinearLayout.getChildAt(index);
		int resId = isSelect ? mSelectPointImageResid : mNormalPointImageResid;
		poiImageView.setBackgroundResource(resId);
	}
	

	@SuppressLint("HandlerLeak")
	private class MHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			if (isAuto) {
				setCurrentItem(mViewPager.getCurrentItem() + 1);
				mHandler.sendEmptyMessageDelayed(WHAT, mIntervalTime);
			}
		}
	}

}
