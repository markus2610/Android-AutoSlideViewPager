package com.android.caij.autoslideviewpager;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
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
	private static final double SCROOL_FACTOR = 1.0;
	
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
	private Integer			mPreviousItem = null;
	
	private AutoSlideViewPageAdapter mAdapter;
	private CustomDurationScroller scroller;
	
	private double 			swipeScrollFactor = SCROOL_FACTOR;
	private double 			autoScrollFactor = SCROOL_FACTOR;
	
	private RelativeLayout.LayoutParams mDescriptionTextViewParam;
	private RelativeLayout.LayoutParams mPointLinearLayoutParam;
	private LinearLayout.LayoutParams mPointImageLayoutParam;
	private OnPageChangeListener pageChangeListener;
	
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
		setViewPagerScroller();
	}
	
	public void setAdapter(AutoSlideViewPageAdapter adapter) {
		this.mAdapter = adapter;
		mViewPager.setAdapter(adapter);
		if (isShowPoint) {
			mPointLinearLayout.removeAllViews();
			if(mPointImageLayoutParam == null) {
				mPointImageLayoutParam = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				mPointImageLayoutParam.setMargins(4, 4, 4, 4);
			}
			for(int i = 0; i < adapter.getPageCount(); i++) {
				ImageView pointImage = new ImageView(context);
				pointImage.setLayoutParams(mPointImageLayoutParam);
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
		removeMessages(WHAT);
		sendMessage(WHAT, (long) (mIntervalTime + scroller.getDuration()/ autoScrollFactor * swipeScrollFactor));
	}
	
	/**
	 * stop auto slide
	 * Suggest to destroy at the interface between the time to stop
	 */
	public void stopSlide() {
		isAuto = false;
		if (mHandler != null) {
			removeMessages(WHAT);
		}
	}
	
	private void sendMessage(int what, long delayMillis) {
		mHandler.sendEmptyMessageDelayed(what, delayMillis);
	}
	
	private void removeMessages(int what) {
		mHandler.removeMessages(what);
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

	public void setCurrentItem(int item, boolean smoothScroll) {
		item = mViewPager.getCurrentItem() - getCurrentItem() + item;
		mViewPager.setCurrentItem(item, smoothScroll);
		stopSlide();
		startSlide();
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
	
	public void setDescriptionTextViewParam(
			RelativeLayout.LayoutParams mDescriptionTextViewParam) {
		this.mDescriptionTextViewParam = mDescriptionTextViewParam;
		mDescriptionTextView.setLayoutParams(this.mDescriptionTextViewParam);
	}

	public void setPointLinearLayoutParam(
			RelativeLayout.LayoutParams mPointLinearLayoutParam) {
		this.mPointLinearLayoutParam = mPointLinearLayoutParam;
		mPointLinearLayout.setLayoutParams(this.mPointLinearLayoutParam);
	}
	
	public void setPointImageLayoutParam(
			LinearLayout.LayoutParams mPointImageLayoutParam) {
		this.mPointImageLayoutParam = mPointImageLayoutParam;
		for (int i = 0; i < mPointLinearLayout.getChildCount(); i++) {
			mPointLinearLayout.getChildAt(i).setLayoutParams(this.mPointImageLayoutParam);;
		}
	}

	/**
	 * Whether the display title
	 * @param isShowTitle
	 */
	public void setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
	}
	
	/**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator)interpolatorField.get(null));
            scrollerField.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * set the factor by which the duration of sliding animation will change while swiping
     */
    public void setSwipeScrollDurationFactor(double scrollFactor) {
    	scroller.setScrollDurationFactor(scrollFactor);
        swipeScrollFactor = scrollFactor;
    }
    
    public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.pageChangeListener = listener;
	}

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling
     */
    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }
	

	@Override
	public void onPageScrollStateChanged(int state) {
		if (pageChangeListener != null) {
			pageChangeListener.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int i, float v, int j) {
		if (pageChangeListener != null) {
			pageChangeListener.onPageScrolled(i, v, j);;
		}
	}

	@Override
	public void onPageSelected(int position) {
		if (isShowPoint) {
			changePointImageState(mAdapter.getPosition(position), true);
			if(null != mPreviousItem)
				changePointImageState(mAdapter.getPosition(mPreviousItem), false);
			mPreviousItem = position;
		}
		if(isShowTitle) {
			setCurrentPageDescription(mAdapter.getPageTitle(position));
		}
		if(pageChangeListener != null) {
			pageChangeListener.onPageSelected(position % mAdapter.getPageCount());
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
				scroller.setScrollDurationFactor(autoScrollFactor);
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
				scroller.setScrollDurationFactor(swipeScrollFactor);
				AutoSlideViewPager.this.sendMessage(WHAT, mIntervalTime + scroller.getDuration());
			}
		}
	}

	public interface OnPageChangeListener {
		public void onPageScrollStateChanged(int state);
		public void onPageScrolled(int i, float v, int j);
		public void onPageSelected(int position);
	}
}
