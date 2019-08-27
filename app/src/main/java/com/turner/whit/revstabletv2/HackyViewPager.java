package com.turner.whit.revstabletv2;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

//import android.support.v4.view.ViewPager;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


//Tried this but no improvement
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        try {
            return super.dispatchTouchEvent(ev);
        }catch (Exception e){
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (ArrayIndexOutOfBoundsException e) {
            //e.printStackTrace();
            return false;
//tried this to catch all exceptions - did not help
            //        } catch (Exception e) {
                   } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
