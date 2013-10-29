package jap.gr.java_conf.dateroid;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipeableSwitchViewPager extends ViewPager {

	private boolean swipeable;
	
	public SwipeableSwitchViewPager(Context context) {
		super(context);
		this.swipeable = true;
	}

	public SwipeableSwitchViewPager(Context context, AttributeSet attrs){
		super(context);
		this.swipeable = true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(this.swipeable){
			return super.onTouchEvent(arg0);
		}
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(this.swipeable){
			return super.onInterceptTouchEvent(arg0);
		}
		return false;
	}

	public void switchSwipeable(boolean swipeable){
		this.swipeable = swipeable;
	}
	
}
