package com.stefan.city.ui.layout;

import com.stefan.city.ui.listener.OnViewChangeListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * REScrollLayout
 * 	用于自定义滑动,例如欢迎界面的滑动效果等
 * @author 日期：2014-6-27上午11:49:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class REScrollLayout extends ViewGroup {
	
	private static final String TAG = "ScrollLayout";

	private VelocityTracker mVelocityTracker; // 用于判断甩动手势

	private static final int SNAP_VELOCITY = 600;

	private Scroller mScroller; // 滑动控制

	private int mCurScreen;

	private int mDefaultScreen = 0;

	private float mLastMotionX;

	// private int mTouchSlop;

	// private static final int TOUCH_STATE_REST = 0;
	// private static final int TOUCH_STATE_SCROLLING = 1;
	// private int mTouchState = TOUCH_STATE_REST;

	private OnViewChangeListener mOnViewChangeListener;

	public REScrollLayout(Context context) {
		super(context);
		init(context);
	}

	public REScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public REScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mCurScreen = mDefaultScreen;
		mScroller = new Scroller(context);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();

			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
//		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	public void snapToDestination() {
		final int screenWidth = getWidth();

		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();

			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);

			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout

			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);
			}
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float x = event.getX();
//		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:

			Log.i("", "onTouchEvent  ACTION_DOWN");

			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}

			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);

			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}

				mLastMotionX = x;

				scrollBy(deltaX, 0);
			}

			break;

		case MotionEvent.ACTION_UP:

			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			break;
		}

		return true;
	}

	private boolean IsCanMove(int deltaX) {

		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}

		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
			return false;
		}

		return true;
	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}
}
