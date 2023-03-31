package android.widget;

import android.content.Context;
import android.util.AttributeSet;

public class CustomViewFlipper extends ViewFlipper {

    int lastChild = -1;

    public CustomViewFlipper(Context context) {
        super(context);
    }

    public CustomViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param whichChild the index of the child view to display
     */
    @Override
    public void setDisplayedChild(int whichChild) {
        if (whichChild != lastChild) {
            lastChild = whichChild;
            super.setDisplayedChild(whichChild);
        }
    }

}
