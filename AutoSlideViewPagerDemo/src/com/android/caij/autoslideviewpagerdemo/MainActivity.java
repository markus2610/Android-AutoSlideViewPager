package com.android.caij.autoslideviewpagerdemo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
		viewPager.startSlide();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		viewPager.stopSlide();
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
