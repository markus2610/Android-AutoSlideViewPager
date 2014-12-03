package com.android.caij.autoslideviewpagerdemo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.caij.autoslideviewpager.AutoSlideViewPageAdapter;
import com.android.caij.autoslideviewpager.AutoSlideViewPager;

public class MainActivity extends Activity {
	
	private int[] ids = new int[]{R.drawable.a1,R.drawable.a2,
			R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6};

			private final String[] imageDescriptions = {
					"hello github developer one",
					"hello github developer two",
					"hello github developer three",
					"hello github developer four",
					"hello github developer five",
					"hello github developer six"
			};

			private AutoSlideViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (AutoSlideViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new MyAdapter());
		viewPager.setIntervalTime(3000);
		viewPager.setAutoScrollDurationFactor(8);
		viewPager.setSwipeScrollDurationFactor(1);
		
//		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		param.bottomMargin = 20;
//		viewPager.setPointLinearLayoutParam(param);
		
//		param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		param.addRule(RelativeLayout.LEFT_OF, R.id.item_llayout_point);
//		param.bottomMargin = 20;
//		param.rightMargin = 20;
//		viewPager.setDescriptionTextViewParam(param);
		
//		LinearLayout.LayoutParams mPointImageLayoutParam = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		mPointImageLayoutParam.setMargins(10, 10, 10, 10);
//		viewPager.setPointImageLayoutParam(mPointImageLayoutParam);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		viewPager.stopSlide();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		viewPager.startSlide();
	}
	
	private class MyAdapter extends AutoSlideViewPageAdapter{

		@Override
		public int getPageCount() {
			return ids.length;
		}

		@Override
		public View instantiatePageItem(int position) {
			ImageView image = new ImageView(MainActivity.this);
			image.setBackgroundResource(ids[position]);
			return image;
		}

		@Override
		public CharSequence getCurrentPageTitle(int position) {
			return imageDescriptions[position];
		}
		
	}
}
