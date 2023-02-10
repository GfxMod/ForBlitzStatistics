package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ExtendedScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    private boolean scrollEnabled = true;

    public ExtendedScrollView(Context context) {
        super(context);
    }

    public ExtendedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollEnabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return scrollEnabled && super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
         return scrollEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

}
