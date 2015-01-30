package com.waupi.view;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context) {
		super(context);
		postInitViewPager();
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		postInitViewPager();
	}
	private FixedSpeedScroller mScroller = null;

	/**
	 * Override the Scroller instance with our own class so we can change the
	 * duration
	 */

	private void postInitViewPager() {
		try {
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			Field interpolator = ViewPager.class
					.getDeclaredField("sInterpolator");
			interpolator.setAccessible(true);

			mScroller = new FixedSpeedScroller(getContext(),
					(Interpolator) interpolator.get(null));
			scroller.set(this, mScroller);
		} catch (Exception e) {
		}
	}

	/**
	 * Set the factor by which the duration will change
	 */
	public void setScrollDurationFactor(double scrollFactor) {
		mScroller.setScrollDurationFactor(scrollFactor);
	}

	private class FixedSpeedScroller extends Scroller {

		private double mScrollFactor = 4;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@SuppressLint("NewApi")
		public FixedSpeedScroller(Context context, Interpolator interpolator,
				boolean flywheel) {
			super(context, interpolator, flywheel);
		}

		/**
		 * Set the factor by which the duration will change
		 */

		public void setScrollDurationFactor(double scrollFactor) {
			mScrollFactor = scrollFactor;
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			super.startScroll(startX, startY, dx, dy,
					(int) (duration * mScrollFactor));
		}
	}
}
